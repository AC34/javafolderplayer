package FolderPlayer.managers;
/**
 *
 * @author  AC34
 */
public class TextManager {
    private GeneralManager gm;
    private static String[] text = new String[100];

    public TextManager(GeneralManager general_manager){
        gm = general_manager;
        //ハードコードされたテキストを初期化
        init();
    }//TextManager
    
    private void init(){
        text[1] = "フォルダを選択してください";
        text[2] ="選択";
    }
    
    public String getText(int num){
        return text[num];
    }//getText
}
