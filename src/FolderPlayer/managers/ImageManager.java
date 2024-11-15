package FolderPlayer.managers;

import FolderPlayer.FolderPlayer;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author  AC34
 */

public class ImageManager {
    private GeneralManager gm;
    private static String IMG_ROOT = "/img/";
    //コンストラクタ
    public ImageManager(GeneralManager general_manager){
        gm =  general_manager;
    }
    //イメージのファイル名を使ってImageクラスの画像を返す
    public Image getImage(String file_name){
        //メインの位置からの相対距離
        return new ImageIcon(FolderPlayer.class.getResource(IMG_ROOT+file_name)).getImage();
    }
}//ImageManagers