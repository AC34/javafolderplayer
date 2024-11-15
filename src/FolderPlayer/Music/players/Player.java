package FolderPlayer.Music.players;

import FolderPlayer.Music.FailedMediaFileLoad;

/**
 * 注意点:PlayableとPlayerインターフェースの一部は同じメソッド名である
 *
 * そもそもPlayableとPlayerクラスを区別するのは
 *
 * 再生メソッド名には共通性があるものの(play,stop,pause)
 *
 * 実際のファイル形式別の再生メソッドに伴う処理が非常に異なるためである。
 *
 * PlayableクラスはPlayerを操作するが、ファイル形式別の処理なども実装するのに対し、
 *
 * Playerインタフェースを実装するクラスは個別の処理を集中して行う。
 *
 *  * @author  AC34
 *
 */
public interface Player {

    /**
     * Playerの実装クラスが、メディアファイルを再生中であることの識別番号。
     */
    public static int PLAYER_STATE_PLAYING = 1;
    /**
     * Playerの実装クラスが、メディアファイルを再生中でないことの識別番号。
     */
    public static int PLAYER_STATE_STOPPING = 2;
    /**
     * Playerの実装クラスが、メディアファイルを一時停止中であることの識別番号。
     */
    public static int PLAYER_STATE_PAUSING = 3;

    /**
     * loadFileおよびloadFileInfoの時に正常なファイル読み込みが行われればtrue
     *
     * エラー発生や再生できない等の不具合でfalseを返す
     *
     * @return
     */
    public Boolean isFileValid();

    /**
     *
     * インスタンス化時に与えられたパスのファイルをロードする
     *
     */
    public Boolean loadFile();

    /**
     *
     * @return
     */
    public void unloadFile();

    /**
     * メディアファイルの情報を取得し変数として保持する ファイルの内容自体はメソッドの終了時にすべて破棄する
     *
     */
    public Boolean loadFileInfo();

    /**
     * 再生 現在のシークから再生を開始
     */
    public void play();

    /**
     * 現在の状態を返す
     *
     * @return int
     */
    public int getCurrentState();

    /**
     * 再生を一時停止する シークは現状を保持する
     */
    public void pause();

    /**
     * 現在のシークをマイクロ秒単位で返す
     *
     * loadFileが呼ばれていない場合他で WAVEPlayer.DEFAULT_TIMEHOLDER_TIMEを返す
     *
     * @return float
     */
    public long getCurrentSeekMicro();

    /**
     * 曲が再生中の場合はストップする 再生中のシークは最初へ戻る
     */
    public void stop();

    /**
     * マイクロ秒で曲の再生時間を返す
     *
     * 事前にprepareを呼んでいる必要がある
     *
     * ファイルの読み込みエラーやprepareを読んでいない場合は FailedMediaFileLodを投げる
     *
     * @return float
     */
    public long getDurationMicrosecond() throws FailedMediaFileLoad;

    /**
     * 曲のタイトル名を返す
     *
     * 曲の情報としてのタイトルが読み取れない場合、拡張子を省いたファイル名を返す
     *
     * 事前にprepareを呼んでいる必要がある
     *
     * ファイルの読み込みエラーやprepareを読んでいない場合はエラーを投げる
     *
     * @return String
     */
    public String getTitle() throws FailedMediaFileLoad;

    /**
     * 安全jな廃棄処理
     *
     * このクラスのインスタンスを廃棄する以前に呼び出すことが推奨される
     */
    public void dispose();

    /**
     * インスタンス化時に渡されたファイル名の拡張子を、FileManagerのFILETYPE番号として返す
     *
     * @return
     */
    public int getFileType();
    
    /**
     * ボリュームの調整を受け付ける
     */
    public void adjustVolume();
}//Player
