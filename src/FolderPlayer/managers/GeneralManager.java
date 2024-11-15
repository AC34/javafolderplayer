package FolderPlayer.managers;

/**
 *
 * @author  AC34
 */
/*他のマネージャークラスのインスタンスを取得するための汎用的マネージャ
他のマネージャークラスのインスタンスの生成はすべてこのクラスで行う。*/
public class GeneralManager {

    /*以下の変数名は、netbeansのコード生成機能を使用して
    自動的にゲッターセッターメソッドを作成する目的で
    全てキャメルケースとする
     */
    private UIManager uiManager;
    private ColorManager colorManager;
    private ImageManager imageManager;
    private TextManager textManager;
    private FileManager fileManager;

    //コンストラクタ
    public GeneralManager() {
        //すべてのマネージャー系インスタンスは、他の処理に先立ってここで記述する
        uiManager = new UIManager(this);
        colorManager = new ColorManager(this);
        imageManager = new ImageManager(this);
        textManager = new TextManager(this);
        fileManager = new FileManager(this);
    }

    /**
     * *********************************************************************
     * **********************************************************************
     * **********************************************************************
     * **********************************************************************
     * **********************************************************************
     */
    //マネージャのセッターは必要ない
    /*以下のメソッド自動生成したもの*/
    public UIManager getUiManager() {
        return uiManager;
    }

    public ColorManager getColorManager() {
        return colorManager;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    public TextManager getTextManager() {
        return textManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

}//GeneralManager
