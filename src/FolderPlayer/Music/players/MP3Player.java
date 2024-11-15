package FolderPlayer.Music.players;

import FolderPlayer.Music.FailedMediaFileLoad;
import FolderPlayer.Time.MicroSecondTimeConverter;
import FolderPlayer.Music.Playable;
import FolderPlayer.managers.FileManager;
import FolderPlayer.managers.GeneralManager;
import FolderPlayer.ui.MenuPanelComponents.DurationIndicatorPanel;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.sound.sampled.*;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;


/**
 *
 * @author  AC34
 */
public class MP3Player implements Player {

    private GeneralManager gm;
    private static final int DEFAULT_FILE_TYPE = -1;//FileManagerのファイルタイプはすべて自然数であるため
    private static final long DEFAULT_CURRENT_SEEK_LONG = -1;
    private static final long DEFAULT_DURATION_TIME = -1;

    //ファイル情報
    private String path;
    private MicroSecondTimeConverter time_holder;
    private String title;
    private String duration_str;
    private int file_type;

    //再生関連フラグ
    private Boolean is_file_loaded;
    private Boolean is_file_valid;
    private int state;

    //再生時に必要な変数
    private RawPlayer raw_player;

    //コンストラクタ
    public MP3Player(String file_path, GeneralManager general_manager) {
        //GeneralManager
        gm = general_manager;
        //変数の初期化
        path = file_path;
        title = "";
        file_type = DEFAULT_FILE_TYPE;

        //フラグの初期化
        is_file_valid = false;
        is_file_loaded = false;
        state = Player.PLAYER_STATE_STOPPING;
    }//MP3Player

    @Override
    public Boolean loadFile() {
        if (raw_player == null) {
            //raw_playerの新規作成
            try {
                //RawPlayerの作成
                raw_player = new RawPlayer(gm);
                //各種設定の繁栄
                AudioInputStream as = AudioSystem.getAudioInputStream(new File(path));
                AudioFormat base_format = as.getFormat();
                AudioFormat decoded_format = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        base_format.getSampleRate(),
                        16,
                        base_format.getChannels(),
                        base_format.getChannels() * 2,
                        base_format.getSampleRate(),
                        false);
                AudioInputStream din = AudioSystem.getAudioInputStream(decoded_format, as);
                raw_player.setAudioFormat(decoded_format);
                raw_player.setStream(din);
                raw_player.setDuration(time_holder.getTime());//再生総時間

                //ここまで処理が来ればok
                is_file_valid = true;
                return true;
            } catch (UnsupportedAudioFileException ex) {
                is_file_valid = false;
                return false;
            } catch (IOException ex) {
                is_file_valid = false;
                return false;
            }
        } else {
            //新規作成でない場合
            return true;
        }

    }//loadFile

    @Override
    public Boolean loadFileInfo() {
        //MP3ファイルの処理
        try {
            //情報取得用にファイルを読み込む
            AudioFileFormat baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(new File(path));
            Map properties = baseFileFormat.properties();

            //MsTimeクラスの作成
            time_holder = new MicroSecondTimeConverter((Long) properties.get("duration"));

            //タイトルの取得
            if (!((String) properties.get("title")).isEmpty()) {
                //ファイル名ではなく音楽情報としてのtitleが存在する場合
                title = (String) properties.get("title");
            } else {
                //ファイル名のみ存在する場合
                String file_name = (new File(path)).getName();
                title = file_name;
                String[] attr = FileManager.ATTRIBUTES_MP3;//拡張子の配列
                for (int i = 0; i < attr.length; i++) {
                    if (file_name.endsWith(attr[i])) {
                        //単純なマッチではなく、文字数で拡張子をカット
                        title = title.substring(0, (title.length() - attr[i].length()));
                        i = attr.length;//ループの終了
                    }
                }
            }

            //読み込んだファイルの廃棄
            baseFileFormat = null;
            properties = null;

            //ここまでで問題がなければファイルタイプの登録
            file_type = FileManager.FILETYPE_MP3;
            //問題がなければtrue
            return true;
        } catch (Exception e) {
            //問題発生なのでfalse
            return false;
        }//if mp3
    }//loadFileInfo

    @Override
    public void play() {
        state = Player.PLAYER_STATE_PLAYING;
        if (raw_player == null) {
            this.loadFile();
        }
        raw_player.start();
    }

    @Override
    public void pause() {
        state = Player.PLAYER_STATE_PAUSING;
        raw_player.pause();
    }

    @Override
    public long getCurrentSeekMicro() {
        if (raw_player != null) {
            return raw_player.getCurrentSeek();
        } else {
            return DEFAULT_CURRENT_SEEK_LONG;
        }
    }

    @Override
    public void stop() {
        if (raw_player != null) {
            try {
                raw_player.stop();
                //一度廃棄してしまう
                raw_player.dispose();
                raw_player = null;
            } catch (Exception ex) {
                //デバッグ用
            }
        }
        state = Player.PLAYER_STATE_STOPPING;
        System.gc();
    }//stop

    @Override
    public long getDurationMicrosecond() throws FailedMediaFileLoad {
        if (time_holder != null) {
            return time_holder.getTime();
        } else {
            return DEFAULT_DURATION_TIME;
        }
    }

    @Override
    public String getTitle() throws FailedMediaFileLoad {
        return title;
    }

    @Override
    public Boolean isFileValid() {
        return is_file_valid;
    }

    @Override
    public void unloadFile() {
        if (raw_player != null) {
            raw_player.dispose();
            raw_player = null;
        } else {
            raw_player = null;
        }
        System.gc();
    }

    @Override
    public void dispose() {
        if (time_holder != null) {
            time_holder = null;
        }
        if (raw_player != null) {
            raw_player.dispose();
        }
        raw_player = null;
        System.gc();
    }

    @Override
    public int getFileType() {
        //このクラスはMP3Playerである
        return FileManager.FILETYPE_MP3;
    }

    @Override
    public int getCurrentState() {
        return state;
    }

    @Override
    public void adjustVolume() {
        //再生時にのみ調整可能なため、raw_player存在時にのみ呼ぶ
        if (raw_player != null && state == Player.PLAYER_STATE_PLAYING) {
            raw_player.adjustVolume();
        }
    }

    /*
    廃棄の際にはdisposeを呼ぶこと
     */
    private class RawPlayer implements Runnable {

        private static final long sleepDelay = 1;

        private GeneralManager gm;
        private AudioFormat format;
        private AudioInputStream stream;
        private SourceDataLine line;
        private Thread thread;
        private byte[] data;
        private long duration;
        int n_bytes_read = 0, n_bytes_written = 0;
        private MicroSecondTimeConverter mstc;//シーク
        private int state;//Playerの状態を利用する
        private int vol;

        //コンストラクタ
        public RawPlayer(GeneralManager general_manager) {
            gm = general_manager;
            //読み込みデータの初期化
            n_bytes_read = 0;
            n_bytes_written = 0;
            //初期状態は停止
            state = Player.PLAYER_STATE_STOPPING;
        }//コンストラクタ

        public void setAudioFormat(AudioFormat audio_format) {
            format = audio_format;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public void setStream(AudioInputStream input_stream) {
            stream = input_stream;
        }

        /*このクラスの廃棄の前に呼ばれることが推奨される*/
        public void dispose() {
            format = null;
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    //finallyへ処理を移譲
                } finally {
                    stream = null;
                }
            }
            if (line != null) {
                try {
                    line.close();
                } finally {
                    line = null;
                }
            }
            System.gc();
        }

        private SourceDataLine getLine() throws LineUnavailableException {
            SourceDataLine res = null;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            res = (SourceDataLine) AudioSystem.getLine(info);
            return res;
        }//getLine()

        public long getCurrentSeek() {
            return mstc.getTime();
        }

        public int getCurrentState() {
            return state;
        }

        public void pause() {
            //状態を更新してjoin
            state = Player.PLAYER_STATE_PAUSING;

            try {
                thread.join();
            } catch (InterruptedException ex) {
                //joinに失敗
            }
        }

        public void stop() {
            //状態を更新してjoin
            state = Player.PLAYER_STATE_STOPPING;//状態の更新
            if (thread != null) {
                try {
                    thread.join();
                    thread = null;
                } catch (InterruptedException ex) {
                    //joinに失敗
                }
            }//join
            //各種コールで終了処理
            try {
                //stop
                if (line != null) {
                    line.drain();
                    line.stop();
                    line.close();
                    //line = null;
                }
                if (stream != null) {
                    stream.close();
                } 
            } catch (Exception ex) {
                //finallyへ移譲
            } finally {
                line = null;
            }
            if (mstc != null) {
                mstc = new MicroSecondTimeConverter(0);//シークの廃棄
                //インジケータの更新
                DurationIndicatorPanel dip = gm.getUiManager().getDurationIndicatorPanel();
                dip.setCurrentTime(mstc.getTime());
            }

            //ボタンをプレイ表示に変更(pasueをdeactivate)
            gm.getUiManager().getPauseChooserPanel().deactivatePanel();
            gm.getUiManager().getPlayChooserPanel().activatePanel();
            
            System.gc();
        }//stop

        /*resumeするかどうかの判断は、内部的な判断に任せる
        具体的にはstart以外からはない*/
        private void resume() {
            //状態の更新をしてjoin
            state = Playable.PLAYABLE_STATE_PLAYING;
            try {
                thread.join();
            } catch (InterruptedException ex) {
                //joinに失敗
            }

            //ボリュームの調整
            this.adjustVolume();

            //新規タイマーを開始
            //再生開始
            thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();

            //再生ボタンをPauseボタンへ変更
            gm.getUiManager().getPlayChooserPanel().deactivatePanel();
            gm.getUiManager().getPauseChooserPanel().activatePanel();

        }

        /*playNewlyするかどうかの判断は、内部的な判断に任せる
        具体的にはstart以外からはない*/
        private void playNewly() {
            //状態の更新
            state = Playable.PLAYABLE_STATE_PLAYING;
            //新規に再生を開始するs
            try {
                //データの初期化
                data = new byte[4096];

                line = getLine();
                line.open(format);
                //シークの初期化
                mstc = new MicroSecondTimeConverter(0);

                //ボリュームの調整
                adjustVolume();

                //インジケータの更新
                DurationIndicatorPanel dip = gm.getUiManager().getDurationIndicatorPanel();
                dip.setMaximumTime(duration);//総再生時間
                dip.setCurrentTime(mstc.getTime());

                //start
                line.start();
                //読み込みの初期化
                n_bytes_read = 0;
                n_bytes_written = 0;

                //初期のボリュームの取得
                vol = 0;//変更を促す為あえて一度値を変更
                adjustVolume();

                //再生開始
                thread = new Thread(this);
                thread.setDaemon(true);
                thread.start();

                //再生ボタンをPauseボタンへ変更
                gm.getUiManager().getPlayChooserPanel().deactivatePanel();
                gm.getUiManager().getPauseChooserPanel().activatePanel();

            } catch (Exception e) {
            }
        }

        public void start() {
            if (format != null && stream != null) {
                //stateによって分岐
                switch (state) {
                    case Player.PLAYER_STATE_PAUSING:
                        resume();//一時停止後の再開
                        break;
                    case Player.PLAYER_STATE_STOPPING:
                        playNewly();
                        break;
                }
            }
        }//start

        public void adjustVolume() {
            //mp3のボリュームの調整はSourceDataLineを引数として与える必要がある
            //lineが無い場合には調整はできない
            if (this.line != null) {
                int val = gm.getUiManager().getVolumeControlPanel().getCurtrentVolume();
                if (vol != val) {
                    //変更時のみ
                    VolumeHandler.adjustVolume(line, val);
                    vol = val;//volの更新
                }
            }
        }


        /*直接呼ばずにstartでスタートすること*/
        @Override
        public void run() {

            //再生フラグ時のみ処理を走らせる
            if (state == Player.PLAYER_STATE_PLAYING) {
                //1読み込みごとの処理
                try {
                    //ボリュームの調整
                    adjustVolume();

                    if (n_bytes_read != -1) {
                        n_bytes_read = stream.read(data, 0, data.length);
                        if (n_bytes_read != -1) {
                            n_bytes_written = line.write(data, 0, n_bytes_read);
                        }
                        mstc.setTime(line.getMicrosecondPosition());//シークの更新
                        //インジケータの更新
                        DurationIndicatorPanel dip = gm.getUiManager().getDurationIndicatorPanel();
                        dip.setMaximumTime(duration);//総再生時間
                        dip.setCurrentTime(mstc.getTime());
                    } else {
                        state = Player.PLAYER_STATE_STOPPING;
                    }
                } catch (IOException e) {
                }

                //ループ処理
                thread = new Thread(this);
                thread.setDaemon(true);
                thread.start();
            }
        }//run
    }//RawPlayer
}//MP3Player
