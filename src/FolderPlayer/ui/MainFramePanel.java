package FolderPlayer.ui;

import FolderPlayer.managers.ColorManager;
import FolderPlayer.managers.GeneralManager;
import javax.swing.JPanel;

/**
 *
 * @author  AC34
 */
/*

MainFrameではできないこと、多すぎること(背景の設定など）を担当するクラス
また、実際にすべてのメニューが搭載されるのはこのクラスである。
*/
public class MainFramePanel extends JPanel{
        private GeneralManager gm;
        //コンストラクタ
        public MainFramePanel(GeneralManager general_manager,int width,int height){
            //マネージャへ登録
            gm = general_manager;
            gm.getUiManager().setMainPanel(this);
            
            //MainPanel上のコンポネントはすべてsetBoundsで設定する
            setLayout(null);
            //サイズは親のMainFrameと同じ
            setBounds(0,0,width,height);
            setBorder(null);
            //背景色の設定
            setBackground(gm.getColorManager().getColor(ColorManager.MAIN_PANEL_BACKGROUND));
           repaint();
        }//MainFramePanel
        
}//MainFramePanel
