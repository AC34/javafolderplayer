package FolderPlayer.ui;

import FolderPlayer.managers.GeneralManager;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author  AC34
 */
/*
*すべてのUIコンポーネントがこの中に入る。
 */
public class MainFrame extends JFrame {

    //GeneralManagerの登録
    private GeneralManager gm;

    /*サイズ、ポジション用変数*/
    private int posx, posy;

    /*コンストラクタ*/
    public MainFrame(GeneralManager general_manager, int frame_width, int frame_height) {
        //GMの登録
        gm = general_manager;
        gm.getUiManager().setMainFrame(this);

        //setTitle
        setTitle("Folder Player");

        //DefaultCloseOperationの設定
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //レイアウトはsetBoundsで決める
        setLayout(null);
        getContentPane().setLayout(null);

        //中央寄せを計算し、配置
        centerizePos(frame_width, frame_height);
        setBounds(posx, posy, frame_width, frame_height);
        getContentPane().setPreferredSize(new Dimension(frame_width, frame_height));

        //pack();
        //リサイズ無効化
        setResizable(false);
    }

    /*メインフレームの出現位置を調整
    サイズの引数は明示的に指定すること*/
    private void centerizePos(int w, int h) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        posx = (int) (dim.getWidth() / 2) - (w / 2);
        posy = (int) (dim.getHeight() / 2) - (h / 2);
    }

    public void adjustSizeAndLocation() {
        //横幅の調整
        adjustWidthByContentPaneSize();
        //縦のサイズと位置を調整
        adjustHeightAndTop();
    }

    /*一度setVisibleが呼ばれた後にこの関数を使うこと。
    トップのバーを表示する分表示域の想定にOS独自の高さが加わる機能に対し、
        バー分の高さを付け加えること(一度表示した後)で、意図した高さでの表示をする。
        また、画面の中央寄せの値も調整する
       設定したサイズが正方形だった場合、この関数で縦長の長方形になる*/
    private void adjustHeightAndTop() {
        int top = getInsets().top;
        setSize(getWidth(), getHeight() + top);
        setLocation(getLocation().x, getLocation().y - (top / 2));
    }

    /*ContentPaneのサイズをマニュアルで調整する
    位置は調整しない。*/
    private void adjustWidthByContentPaneSize() {
        //ContentPaneのサイズは常に少し小さい
        //diff分を追加することで調整
        int diff = getWidth() - getContentPane().getWidth();
        setBounds(getX(), getY(), getWidth() + diff, getHeight());
        getContentPane().setSize(getContentPane().getWidth() + diff, getHeight());
    }
}//MainFrame extends JFrame 
