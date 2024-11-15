package FolderPlayer.ui;

import FolderPlayer.managers.GeneralManager;
import FolderPlayer.ui.MainPanelComponents.MusicPanel;
import FolderPlayer.ui.MainPanelComponents.StatusPanel;
import javax.swing.JPanel;

/**
 *
 * @author  AC34
 */
public class MainPanel extends JPanel{
    private GeneralManager gm;
    //コンストラクタ
    public MainPanel(GeneralManager general_manager,int x, int y, int width, int height){
        //MainPanelには背景は無い
        setOpaque(false);//透過処理

        //レイアウトの設定
        setLayout(null);
        //マネージャの登録
        gm = general_manager;
        gm.getUiManager().setMainPanel(this);
        
        //配置
        setBounds(x,y,width,height);
    }//MainPanel
    
    public void buildUi(){
        /*ビルドの順番
        1.StatusPanel
        2.曲名パネル
        */
        //1.StatusPanelの初期化と搭載
        StatusPanel sp = new StatusPanel(gm,500,30);
        add(sp);
        //2.MusicPanelの初期化と搭載
        MusicPanel mp = new MusicPanel(gm,0,30,500,this.getHeight()-sp.getHeight());
        add(mp);
        repaint();
    }//buildUi
}//JPanel
