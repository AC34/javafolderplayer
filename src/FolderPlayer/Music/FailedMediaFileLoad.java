package FolderPlayer.Music;

/**
 *メディアファイルのローディングに失敗した場合、
 * @author  AC34
 */
public class FailedMediaFileLoad extends Exception{
    /**
     *空のコンストラクタ
     */
    public FailedMediaFileLoad(){
        super();
    }
    /**
     *メッセージ付きコンストラクタ
     * @param message
     */
    public FailedMediaFileLoad(String message){
        super(message);
    }
}
