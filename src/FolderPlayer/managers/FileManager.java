package FolderPlayer.managers;

/**
 *
 * @author  AC34
 */
public class FileManager {
    GeneralManager gm;

    //ファイルの拡張子の配列定数を設定
    public static final String[] ATTRIBUTES_WAVE = {".wav"};
    public static final String[] ATTRIBUTES_MP3 = {".mp3"};

    //ファイルタイプの定数を設定
    public static final int FILETYPE_NONE = -1;
    public static final int FILETYPE_WAVE = 1;
    public static final int FILETYPE_MP3 = 2;
    //コンストラクタ
    public FileManager(GeneralManager general_manager){
        gm = general_manager;
    }

    /*拡張子の配列attributesのうちの１つがfilenameの終端に対応する場合true
    filenameが空であったり、attributesが一つも対応しなかった場合
    拡張子の配列はFileManager.ATTRIBUTES_プレフィックスで
    */
    public static Boolean isFileType(String[] attributes, String filename) {
        //filenameが空である場合
        if (filename.isEmpty()) {
            return false;
        }
        //マッチング処理
        Boolean match = false;
        for (int i = 0; i < attributes.length; i++) {
            if (filename.endsWith(attributes[i])) {
                match = true;
                //ループの終了処理
                i = attributes.length;
            }
        }
        return match;
    }
    /*2つのStringの配列を1つのString配列に連結して返す
    返る配列の長さはarr1.length+arr2.length
    arr1,arr2の順番で配列の内容は連家tるされる
    */
    public static String[] joinAttributesArray(String[] arr1,String[] arr2){

        String[] back = new String[arr1.length+arr2.length];
        for(int i = 0; i < arr1.length; i++){
            back[i] = arr1[i];
        }
        for(int i = 0; i < arr2.length; i++){
            back[i+(arr1.length)] = arr2[i];
        }
        return back;
    }//joinAttributesArray

    /**
     * 渡されたパスの拡張子を文字列の最後尾で判別
     * 
     * 適合しない場合は、FILETYPE_NONEを返す
     * @param path
     * @return int
     */
    public static  int getFileType(String path){
        if(isFileType(ATTRIBUTES_MP3, path)){
            return FILETYPE_MP3;
        }else if(isFileType(ATTRIBUTES_WAVE, path)){
            return FILETYPE_WAVE;
        }else{
            return FILETYPE_NONE;
        }
    }
    
}//FileManager
