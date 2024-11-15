package FolderPlayer.Dialog;

import FolderPlayer.managers.GeneralManager;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author  AC34
 */
public class FolderChooserDialog {

    private GeneralManager gm;
    private JFileChooser dialog;
    private String selectedPath;
    private String[] files;

    //コンストラクタ
    public FolderChooserDialog(GeneralManager general_manager) {
        gm = general_manager;

        //選択パスの初期化
        selectedPath = "";
        
        //ファイル配列の初期化
        files = null;

    }//コンストラクタ

    /*フォルダ選択ダイアログを表示*/
    public void openDialog() {
        String msg = gm.getTextManager().getText(1);
        String cd = new File(".").getAbsoluteFile().getParent();//カレントディレクトリs

        dialog = new JFileChooser();//デフォルトでモーダル
        dialog.setDialogTitle(msg);
        dialog.setDialogType(JFileChooser.DIRECTORIES_ONLY);
        dialog.setFileHidingEnabled(true);//フォルダのみ表示
        dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//フォルダのみ表示

        //表示
        dialog.showDialog(null, gm.getTextManager().getText(2));

        //パスを格納
        if (dialog.getSelectedFile() != null) {
            //パスが選択されている
            selectedPath = dialog.getSelectedFile().toString()+File.separator;
        }
    }//openDialog

    /*ダイアログ表示前は常にfalse
    ダイアログ表示後、パスの有無でtrue/falseを返す*/
    public Boolean isSelected() {
        if (selectedPath.length() == 9) {
            return false;
        } else {
            return true;
        }
    }

    /*ダイアログ表示前/ダイアログがキャンセルされた場合は空の文字列が返る
     */
    public String getSelectedPath() {
        return selectedPath;
    }
    
    /*フォルダ中に存在するファイル一覧を取得
        attrで指定された拡張子のファイル一覧をStringの配列で返す
    */
    public void scanFolder(String dir,String[] attr){
        File f = new File(dir);
        ArrayList<String> result = new ArrayList<String>();
        if(f.isDirectory()){
            //ディレクトリの場合のみ処理
            File[] list = f.listFiles();
            for(int i = 0; i < list.length;i++){
                //拡張子のイテレーション
                for(int k = 0; k < attr.length;k++){
                    //拡張子に適合する場合のみリストへ追加
                    if(list[i].toString().endsWith(attr[k])){
                        result.add(list[i].toString());
                        k= attr.length;//ループの終了
                    }
                }//拡張子のイテレーション
            }//フォルダ一覧のイテレーション
            
            //resultをfilesへ格納
            files = new String[result.size()];
            for(int i=0; i< files.length;i++){
                files[i] = result.get(i);
            }
            System.gc();
        }//ディレクトリでない場合は何もしない
    }//scanFolder
    /*Stringの配列を返す。
    scanFolder後に呼ぶことを想定
    リストが存在しない場合は長さ0のStringの配列を返す。
    */
    public String[] getList(){
        if(files!=null){
            return files;
        }else{
            return new String[0];
        }
    }
    
    /*ダイアログの読み込み用に保持してるデータをクリア*/
    public void flush(){
        this.files = null;
        this.selectedPath = null;
        System.gc();
    }
}//FolderChooserDialog
