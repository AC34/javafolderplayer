package FolderPlayer.Music;

/**
 *
 * @author  AC34
 */
/*このインターフェースは、メディアファイルの取り扱い時に一般的なメソッドを集めたもの。
 */
public interface Playable {

    /**
     * Playableの実装クラスが、メディアファイルを再生中であることの識別番号。
     */
    public static int PLAYABLE_STATE_PLAYING = 1;
    /**
     * Playableの実装クラスが、メディアファイルを再生中でないことの識別番号。
     */
    public static int PLAYABLE_STATE_STOPPING = 2;
    /**
     * Playableの実装クラスが、メディアファイルを一時停止中であることの識別番号。
     */
    public static int PLAYABLE_STATE_PAUSING = 3;

    /**
     * 現在のシークから再生
     */
    public void play();

    /**
     * 停止メソッド シークは0に戻る
     */
    public void stop();

    /**
     * 一時停止メソッド
     */
    public void pause();

    /**
     * 現在のPlayableの状態を取得。 PLAYABLE_STATE_+状態名で固定値を得ることができる
     *
     * @return int
     */
    public int getCurrentState();

    /**
     * 現在のシーク(再生開始からの時間)を取得することができる MicroSecondsで手に入れることができる。
     *
     * @return long
     */
    public long getCurrentSeekMillis();

    /**
     * setFileNameで与えられたファイル名の文字列を返す。 setFileNameが呼ばれていない場合、空の文字列を返す。
     *
     * @return String
     */
    public String getFileName();

    /*ファイル名の絶対パス(ディレクトリ)を文字列として与える
    getPath,getFileName,isFile他のパスを必要とするメソッドは
    このメソッドの後に呼ばれることを前提としている。
     */
    public void setPath(String dir);

    /*setPathで設定された値を返す。
    setPathが設定されていない場合、空の文字列を返す。*/
    public String getPath();

    /*セットされたファイル名が存在するかどうか判定する。
    ファイル名が存在しない場合は常にfalseを返す*/
    public Boolean isFile();

    //メディアファイルを読み込み、変数へ保持する
    public void loadFile();

    /**
     * メディアファイルを保持変数から削除する
     */
    public void unloadFile();

    /**
     * 読み込んだファイルが再生可能な音楽ファイルである場合のみtrue falseの場合は以下の要因が考えられる
     * -クラスがインスタンス化されてから実際に一度もファイル名が設定れていない(setfileが呼ばれていない)
     * -クラスがインスタンス化されてから実際に一度もファイルがロードされていない(loadFile,loadFileInfoが呼ばれていない)
     * -ファイルの不在(isFileでfalseが出る場合など)
     * -ファイル読み込み時にエラーが存在した(isFileでtrueを取得してからloadFile呼び出しまでの間にファイルが消失したなど)
     * -読み込んだファイルが実装したクラスの想定していないファイル拡張子であった -自身の他のプロセス、他のプログラムなどとデッドロックを起こした
     *
     * @return Boolean
     */
    public Boolean isFileValid();

    /**
     * ファイルの内容を保持せずにメディア情報等を取得するためのメソッド。 ※再生時間、アーティスト名など 読み込み時にエラーがなければtrue
     * 読み込み時に問題があればfalse
     */
    public Boolean loadFileInfo();

    /**
     * 調整可能な時にプレイヤーのボリューム調整を試みる 曲が選択されていて、Player.PLAYER_STATE_PLAYING時のみ実際に調整される
     */
    public void adjustVolume();

    /*loadFileinfoが呼ばれていればtrue
        一度も呼ばれていなければfalse
        unloadFileが呼ばれていればfalseへ戻る
     * @return
     */
    public Boolean isFileInfoLoaded();

}//Playable
