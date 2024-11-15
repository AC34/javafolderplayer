package FolderPlayer.Music;

import FolderPlayer.Time.MicroSecondTimeConverter;
import FolderPlayer.Music.players.MP3Player;
import FolderPlayer.Music.players.Player;
import FolderPlayer.Music.players.WAVEPlayer;
import FolderPlayer.String.ToStringConverter;
import FolderPlayer.managers.FileManager;
import FolderPlayer.managers.GeneralManager;
import FolderPlayer.ui.MainPanelComponents.MusicPanelItem;
import java.io.File;
import java.util.regex.Pattern;

/**
 * 音楽ファイルを実際に扱うクラス。 Playableインターフェースのメソッドを実装する。
 *
 * @author  AC34
 */
public class MusicItem implements Playable {

    private static final int DEFAULT_FILE_TYPE = 01;
    private int current_state;
    private String file_name;
    private String path;
    private GeneralManager gm;
    private MicroSecondTimeConverter time_holder;
    private String duration_str;
    private Boolean is_info_loaded;
    //拡張子なしのファイル名
    private String title;
    //拡張子のタイプ(FileManagerの示すタイプ)
    private int file_type;

    //再生に使用するPlayerの実装クラスのインスタンスは1つ
    private Player my_player;

    /**
     * コンストラクタ このクラス自体は配列として扱われることが前提
     */
    public MusicItem(GeneralManager general_manager) {
        gm = general_manager;
        //フラグの初期化
        current_state = PLAYABLE_STATE_STOPPING;
        //空の文字列で初期化
        path = "";
        file_name = "";
        file_type = DEFAULT_FILE_TYPE;
        is_info_loaded= false;
    }

    @Override
    public void play() {
        this.loadFile();

        //ファイルタイプが更新されていない場合は、loadfileinfoが呼ばれていないs
        if (file_type == FileManager.FILETYPE_NONE) {
            return;//処理を中断
        }
        /*で再生準備を確認*/
        if (my_player == null) {
            return;//処理を中断
        }

        if (!my_player.isFileValid()) {
            return;//処理を中断
        }

        //このItem以外を停止(単一の再生のみ許可する)
        MusicPanelItem[] items = gm.getUiManager().getMusicPanel().getAllMusicPanelItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getMusicItem() != this) {
                items[i].getMusicItem().stop();
            }
        }

        //再生中でない場合のみ再生
        if (my_player.getCurrentState() != Player.PLAYER_STATE_PLAYING) {
            my_player.play();
        }

        //ここまで処理が渡れば状態を更新
        current_state = PLAYABLE_STATE_PLAYING;

        //Uiの変更
        gm.getUiManager().getPlayChooserPanel().deactivatePanel();
        gm.getUiManager().getPauseChooserPanel().activatePanel();
    }//play

    @Override
    public void stop() {
        current_state = PLAYABLE_STATE_STOPPING;
        my_player.stop();
    }

    @Override
    public void pause() {
        current_state = PLAYABLE_STATE_PAUSING;
        my_player.pause();
    }

    @Override
    public int getCurrentState() {
        return current_state;
    }

    @Override
    public long getCurrentSeekMillis() {

        return (long) -1;
    }

    @Override
    public String getFileName() {
        return file_name;
    }

    @Override
    public Boolean isFile() {
        if (getPath() != "") {
            return false;
        }
        try {
            File f = new File(getPath());
            return f.isFile();
        } catch (Exception e) {
            //デバッグ用
            return false;//Exceptionを受けている場合、ファイルが存在しても有効でないものと判断したほうがいい
        }
    }//isFile

    @Override
    public void loadFile() {
        if (my_player != null) {
            //my_playerがすでに作成されている場合
            my_player.loadFile();
        } else {
            //my_playerの新規作成
            //Playerインターフェースのインスタンス化
            if (FileManager.isFileType(FileManager.ATTRIBUTES_WAVE, file_name)) {
                //WAVE形式の場合
                my_player = new WAVEPlayer(getPath(), gm);
            }
            if (FileManager.isFileType(FileManager.ATTRIBUTES_MP3, file_name)) {
                //MP3形式のファイルの処理
                my_player = new MP3Player(getPath(), gm);
            }//MP3

            my_player.loadFile();
        }
    }//loadFile

    @Override
    public void unloadFile() {
        if (my_player != null) {
            my_player.unloadFile();
            my_player = null;
        } else {
            my_player = null;
        }
        is_info_loaded= false;
    }

    @Override
    public Boolean isFileValid() {
        if (my_player != null) {
            return my_player.isFileValid();
        } else {
            return false;
        }
    }

    @Override
    public void setPath(String dir) {
        path = dir;
        //ファイル名を抽出して格納する
        String[] split = path.split(Pattern.quote(File.separator));
        if (split.length > 0) {
            file_name = split[split.length - 1];
        } else {
            //与えられているファイル名にパスセパレータが1つも無い場合、絶対パスではない
        }
    }//setPath

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Boolean loadFileInfo() {
        //Playerインターフェースのインスタンス化
        if (FileManager.isFileType(FileManager.ATTRIBUTES_WAVE, file_name)) {
            //WAVE形式の場合
            my_player = new WAVEPlayer(getPath(), gm);
        }
        if (FileManager.isFileType(FileManager.ATTRIBUTES_MP3, file_name)) {
            //MP3形式のファイルの処理
            my_player = new MP3Player(getPath(), gm);
        }//MP3

        Boolean success = false;
        success = my_player.loadFileInfo();//インスタンスにロードさせる

        try {
            this.title = my_player.getTitle();
            this.time_holder = new MicroSecondTimeConverter(my_player.getDurationMicrosecond());
            this.file_type = my_player.getFileType();
            //int型へ直して分:秒の形へ直す
            //桁数を00:00へ修正(2桁)
            String min = ToStringConverter.fillHeadWithZero(time_holder.getMinutesWithin(), 2);
            String sec = ToStringConverter.fillHeadWithZero(time_holder.getTheRestSecsOfMinutesWithin(), 2);
            duration_str = min + ":"+ sec;
        } catch (Exception e) {
            //タイトルの取得、もしくは再生時間の取得に失敗
            success = false;
        }

        //loadFileInfo時点ではunloadしておく
        my_player.unloadFile();

        is_info_loaded= true;
        //処理の成果を返す
        return success;
    }//loadFileInfo

    public String getDurationText() {
        return duration_str;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void adjustVolume() {
        if(my_player!=null){
            my_player.adjustVolume();
        }
    }

    @Override
    public Boolean isFileInfoLoaded() {
        return is_info_loaded;
    }

}//PlayableItem
