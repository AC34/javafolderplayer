package FolderPlayer.Music.players;

import FolderPlayer.Music.FailedMediaFileLoad;
import FolderPlayer.Time.MicroSecondTimeConverter;
import FolderPlayer.managers.FileManager;
import FolderPlayer.managers.GeneralManager;
import FolderPlayer.ui.MenuPanelComponents.DurationIndicatorPanel;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author  AC34
 */
public class WAVEPlayer implements Player {

    private GeneralManager gm;
    private static final int DEFAULT_FILE_TYPE = -1;//FileManagerのファイルタイプはすべて自然数であるため
    private static final long DEFAULT_CURRENT_SEEK_LONG = -1;
    private static final long DEFAULT_TIMEHOLDER_TIME = -1;

    //ファイル情報
    private String path;
    private MicroSecondTimeConverter time_holder;
    private String title;
    private String duration_str;
    private int file_type;

    //再生関連フラグ
    private Boolean is_file_loaded;
    private Boolean is_file_valid;
    private int state;//Player.PLAYER_STATE_

    //再生時に必要な変数
    private Clip my_clip;
    private AudioInputStream my_stream;

    private ClipPlayer cp;

    /**
     * コンストラクタ
     *
     * @param file_path
     */
    public WAVEPlayer(String file_path, GeneralManager general_manager) {
        gm = general_manager;
        //変数の初期化
        path = file_path;
        title = "";
        file_type = DEFAULT_FILE_TYPE;

        //フラグの初期化
        is_file_valid = false;
        is_file_loaded = false;
        state = Player.PLAYER_STATE_STOPPING;
    }//コンストラクタ

    @Override
    public Boolean loadFile() {
        //playでない限り、waveファイルを読み込まない
        if (state == PLAYER_STATE_PLAYING) {
            try {
                //Clipの準備
                my_stream = AudioSystem.getAudioInputStream(new File(path));
                my_clip = AudioSystem.getClip();
                my_clip.open(my_stream);//以降my_clip.startが呼べる
                //フラグ
                is_file_loaded = true;

                //ここまで問題なければ正常終了
                return true;
            } catch (Exception e) {
                //異常であればfalse
                is_file_loaded = false;
                is_file_valid = false;
                return false;
            }
        } else {
            return false;
        }
    }//loadFile

    @Override
    public void play() {
        //フラグの更新が優先
        state = Player.PLAYER_STATE_PLAYING;
        if (!is_file_loaded) {
            loadFile();
        }
        if (cp == null) {
            cp = new ClipPlayer(this, gm);
        }
        cp.play();
    }//play

    @Override
    public void pause() {
        state = Player.PLAYER_STATE_PAUSING;
        if (cp != null) {
            cp.pause();
        }
    }

    @Override
    public void stop() {
        if (my_clip != null) {
            my_clip.stop();
        }
        state = Player.PLAYER_STATE_STOPPING;
        if (cp != null) {
            cp.stop();
            cp = null;
        }
        unloadFile();//gcも呼ばれる
    }

    @Override
    public long getCurrentSeekMicro() {
        if (my_clip == null) {
            return DEFAULT_CURRENT_SEEK_LONG;
        } else {
            return my_clip.getLongFramePosition();
        }
    }//getCurretSeekMicro

    @Override
    public long getDurationMicrosecond() {
        if (time_holder == null) {
            return DEFAULT_TIMEHOLDER_TIME;
        } else {
            return time_holder.getTime();
        }
    }//getDurationMicrosecond

    @Override
    public String getTitle() throws FailedMediaFileLoad {
        return title;
    }

    @Override
    public Boolean isFileValid() {
        return is_file_valid;
    }

    @Override
    public Boolean loadFileInfo() {
        try {
            //Clipの準備
            AudioInputStream astream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(astream);

            //再生時間の取得
            time_holder = new MicroSecondTimeConverter(clip.getMicrosecondLength());
            //タイトルの準備
            //clipにタイトル取得メソッドはないため、パスからトリムする
            String name = new File(path).getName();
            String[] attr = FileManager.ATTRIBUTES_WAVE;
            for (int i = 0; i < attr.length; i++) {
                if (path.endsWith(attr[i])) {
                    //単純なマッチではなく、文字数でカット
                    name = name.substring(0, (name.length() - attr[i].length()));
                    i = attr.length;//ループの終了
                }
            }
            title = name;

            //int型へ直して分:秒の形へ直す
            duration_str = String.valueOf(time_holder.getMinutesWithin()) + ":"
                    + String.valueOf(time_holder.getTheRestSecsOfMinutesWithin());
            //clipの正常クロージング
            clip.drain();
            clip.close();
            clip = null;

            astream.close();
            astream = null;
            System.gc();//ここで強制しないとメモリが逼迫する

            //ここまでで問題がなければファイルタイプの登録
            file_type = FileManager.FILETYPE_WAVE;

            //問題がない場合のフラグ
            is_file_valid = true;
            //問題がなければtrue
            return true;
        } catch (Exception e) {
            //問題発生なのでfalse
            is_file_valid = false;
            return false;
        }
    }//loadFileInfo()

    @Override
    public void unloadFile() {
        if (my_clip != null) {
            try {
                cp.stop();
                cp = null;
            } catch (Exception e) {
                //Clip Playerを止められなかった
            }
        }
        is_file_loaded = false;
        System.gc();
    }//unloadFile

    @Override
    public int getFileType() {
        //このクラスはWAVEPLAYERである
        return FileManager.FILETYPE_WAVE;
    }

    @Override
    public void dispose() {
        //AudioInputStreamの廃棄
        if (my_stream != null) {
            try {
                my_stream.close();
            } catch (IOException e) {
                //finallyへ移譲
            } finally {
                my_stream = null;
            }
        }
        //Clipの廃棄
        if (my_clip != null) {
            my_clip.close();
            my_clip = null;
        }
        //ClipPlayerの廃棄
        cp = null;
        System.gc();
    }

    @Override
    public int getCurrentState() {
        return state;
    }

    /**
     * 主に子クラスへClipを渡す用
     *
     * @return
     */
    protected Clip getClip() {
        return my_clip;
    }

    @Override
    public void adjustVolume() {
        //再生時にしか調整はできない
        if (cp != null && state == Player.PLAYER_STATE_PLAYING) {
            cp.adjustVolume();
        }
    }

    /*おもに再生の監視と前後の挙動制御向けに設定*/
    private class ClipPlayer implements Runnable {

        private Clip clip;
        private Thread thread;
        private WAVEPlayer wp;
        private GeneralManager gm;
        private MicroSecondTimeConverter mstc;
        private int state;
        private int vol;

        //コンストラクタ
        public ClipPlayer(WAVEPlayer wave_player, GeneralManager general_manager) {

            gm = general_manager;
            wp = wave_player;
            clip = wp.getClip();
            thread = null;
            mstc = new MicroSecondTimeConverter(0);

            //ボリュームの初期化
            vol = gm.getUiManager().getVolumeControlPanel().getCurtrentVolume();

            //状態の更新
            state = Player.PLAYER_STATE_STOPPING;
        }

        public void play() {
            if (state == Player.PLAYER_STATE_STOPPING) {
                startNewly();
            } else if (state == Player.PLAYER_STATE_PAUSING) {
                resume();
            }
        }//play

        /*再生を新しく始める*/
        private void startNewly() {
            //状態の更新
            state = Player.PLAYER_STATE_PLAYING;

            //シークの設定
            mstc = new MicroSecondTimeConverter(0);

            //インジケータの設定
            DurationIndicatorPanel dip = gm.getUiManager().getDurationIndicatorPanel();
            dip.setMaximumTime(wp.getDurationMicrosecond());
            dip.setCurrentTime(0);

            //初期のボリュームの取得
            vol = 0;//変更を促す為あえて一度値を変更
            adjustVolume();

            //Clipの再生
            clip.start();

            //スレッドの開始
            thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();

            //表示を一時停止パネルへ切り替え
            gm.getUiManager().getPauseChooserPanel().activatePanel();
            gm.getUiManager().getPlayChooserPanel().deactivatePanel();
        }

        /*再生の再開*/
        private void resume() {
            //状態の更新
            state = Player.PLAYER_STATE_PLAYING;
            //インジケータの設定
            DurationIndicatorPanel dip = gm.getUiManager().getDurationIndicatorPanel();
            dip.setCurrentTime(mstc.getTime());

            //ボリュームの調整
            adjustVolume();

            //Clipの再生
            clip.start();

            //スレッドの再開
            thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();

            //表示を一時停止パネルへ切り替え
            gm.getUiManager().getPauseChooserPanel().activatePanel();
            gm.getUiManager().getPlayChooserPanel().deactivatePanel();
        }

        public void pause() {
            //状態の更新
            state = Player.PLAYER_STATE_PAUSING;

            //Clipの停止
            clip.stop();
            //表示を再生パネルへ切り替え
            gm.getUiManager().getPauseChooserPanel().deactivatePanel();
            gm.getUiManager().getPlayChooserPanel().activatePanel();
        }

        public void stop() {
            //状態の更新
            state = Player.PLAYER_STATE_STOPPING;

            //Clipの停止
            clip.stop();
            clip.setFramePosition(0);//リセット
            clip.close();

            //mstcのリセット
            mstc = new MicroSecondTimeConverter(0);

            //停止用処理
            DurationIndicatorPanel dip;
            dip = gm.getUiManager().getDurationIndicatorPanel();
            dip.setCurrentTime(0);//現在シーク表示を0へ         

            //一時停止ボタンが表示されている場合再生ボタンへ
            gm.getUiManager().getPauseChooserPanel().deactivatePanel();
            gm.getUiManager().getPlayChooserPanel().activatePanel();
            System.gc();
        }//stop

        /*再生時に毎回Clipを渡して調整する必要がある*/
        public void adjustVolume() {
            if (clip != null) {
                int val = gm.getUiManager().getVolumeControlPanel().getCurtrentVolume();
                if (vol != val) {
                    //変更時のみ調整
                    VolumeHandler.adjustVolume(clip, val);
                    vol = val;//volの更新
                }
            }
        }//adjustVolume

        @Override
        public void run() {
            if (state == Player.PLAYER_STATE_PLAYING) {
                if (clip.isActive()) {
                    //ボリュームの調整
                    adjustVolume();
                    //再生時の挙動
                    mstc.setTime(clip.getMicrosecondPosition());

                    //シーク表示を更新
                    DurationIndicatorPanel dip = gm.getUiManager().getDurationIndicatorPanel();
                    dip.setCurrentTime(mstc.getTime());

                    try {
                        //ループ処理
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                    thread = new Thread(this);
                    thread.setDaemon(true);
                    thread.start();
                } else {
                    //再生の終了
                    stop();//終了処理
                }
            }//current state== playing
        }//run
    }
}//WAVEPlayer
