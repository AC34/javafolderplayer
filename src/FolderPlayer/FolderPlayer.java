package FolderPlayer;

import FolderPlayer.managers.GeneralManager;
import FolderPlayer.ui.MainFrame;
import FolderPlayer.ui.MainFramePanel;
import FolderPlayer.ui.MainPanel;
import FolderPlayer.ui.MenuPanel;

/**
 *
 * @author  AC34
 */
public class FolderPlayer {
    /**
     * このプログラムのmain関数
     */
    public static void main(String[] args) {
       //GeneralManagerの生成
        //他のインスタンスに先立って行う必要がある
        //gmは他のマネージャを作成する。   
        GeneralManager  gm = new GeneralManager();

       //Uiの設定を行って表示
        buildUi(gm);
    }//main
    
    private static void buildUi(GeneralManager gm){
          /*以下のUIコンポーネントのサイズや位置は
        第11回のデザインによって決まった固定値である。*/
        //メインフレームの作成
        MainFrame mf = new MainFrame(gm,500,500);
        
        //メインフレーム用パネルの作成と搭載
        MainFramePanel mfp = new MainFramePanel(gm,500,500);
        mf.getContentPane().add(mfp);
        
        //メインパネルの作成と搭載
        MainPanel mp = new MainPanel(gm,0,0,500,450);
        //メインパネル内のUi構築はメインパネルへ譲る
        mp.buildUi();
        //フレームに載せる
        mfp.add(mp);
        
        //メニューパネルの作成と搭載
        MenuPanel menu = new MenuPanel(gm,0,450,500,50);
        //メインフレーム用パネルに載る。メインパネルではない
        mfp.add(menu);
        //menuのUi構築はmenuPanelへ譲る
        menu.buildUi();
        
        //表示
        mf.setVisible(true);
        
        //表示後に縦の高さと位置の高さを調整
        //位置とサイズの表示後の調整
        mf.adjustSizeAndLocation();
        mf.revalidate();
    }//buildUi
}//Ex_11_