package FolderPlayer.ui;

import FolderPlayer.managers.GeneralManager;
import FolderPlayer.ui.MenuPanelComponents.DurationIndicatorPanel;
import FolderPlayer.ui.MenuPanelComponents.FolderChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.NextChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.PauseChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.PlayChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.PrevChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.StopChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.VolumeControlPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author  AC34
 */
public class MenuPanel extends JPanel {

    //マネージャ
    private GeneralManager gm;

    public MenuPanel(GeneralManager general_manager, int x, int y, int width, int height) {
        //マネージャへ登録
        gm = general_manager;
        gm.getUiManager().setMenuPanel(this);
        
        //位置とサイズの調整
        setBounds(x, y, width, height);
        
        //メニュの各パーツはsetBoundsで明示的に指定する
        setLayout(null);
        setBorder(null);
        
        //デバッグ用
        setBackground(Color.blue);
        
        repaint();
    }//MenuPanel

    public void paintComponent(Graphics g) {
        //背景の設定
        Image img = gm.getImageManager().getImage("main_menu_background.png");
        
        g.drawImage(img,0,0,this);
    }
    /*MenuPanel内に搭載するコンポーネント群の初期化と配置*/
    public void buildUi() {
        //画面の左から実装する
        //再生時間/最大時間のインジケータ
        DurationIndicatorPanel dp = new DurationIndicatorPanel(gm,20,7,85,23);
        add(dp);
        
        //正方形メニュー
        //フォルダ選択アイコン付きメニューアイテム
        add( new FolderChooserPanel(gm,125,0,50));
        //ひとつ前の曲選択用メニューアイテム
        add(new PrevChooserPanel(gm,175,0,50));
        //再生メニュー
        add(new PlayChooserPanel(gm,225,0,50));
        //一時停止メニュー(再生メニューと同じ位置)
        add(new PauseChooserPanel(gm,225,0,50));
        //次の曲選択用メニューアイテム
        add(new NextChooserPanel(gm,275,0,50));
        //停止用メニューアイテム
        add(new StopChooserPanel(gm,325,0,50));
        
        //ボリュームコントロールパネル
        add(new VolumeControlPanel(gm,50,395,12,85,25));

    }//buildUi

}//MenuPanel
