package FolderPlayer.ui.MenuPanelComponents;

import FolderPlayer.managers.GeneralManager;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * @author  AC34
 */
public class VolumeControlPanel extends JPanel implements MouseMotionListener, MouseListener {

    private GeneralManager gm;
    private volatile float current_volume;//floatで保持
    private VolumeHandle vh;
    private int range_max;
    private int range_min;
    private int range;
    private float range_unit;//floatで保持

    /*ボリューム操作用パネル
    JSliderよりも簡易的な設定をするものとして設定*/
    public VolumeControlPanel(GeneralManager general_manager, int initial_volume, int x, int y, int width, int height) {
        //GeneralManagerの登録
        gm = general_manager;
        gm.getUiManager().setVolumeControlPanel(this);

        //パネルの設定
        setOpaque(false);
        addMouseMotionListener(this);
        addMouseListener(this);

        //位置とサイズの設定
        setLayout(null);
        setBounds(x, y, width, height);

        //ボリュームハンドルの初期化と搭載
        Image handle_img = gm.getImageManager().getImage("volume_handle.png");
        vh = new VolumeHandle(handle_img);
        add(vh);

        //ボリュームの初期化
        current_volume = initial_volume;
        range = getWidth() - vh.getWidth() / 4 * 3;//ハンドルの幅*3/4を引いたものがレンジ
        range_min = 0;//パネルの左端は0
        range_max = getWidth() - vh.getWidth();//パネルの右端-ハンドルの半分の値
        range_unit = (float) range / 100;//100分率で計算する際に必要

        moveHandle((int) (range_unit * current_volume));
    }//コンストラクタ

    @Override
    public void mouseDragged(MouseEvent e) {
        moveHandle(e.getX() - vh.getWidth() / 2);//ハンドルの中心を渡す
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        moveHandle(e.getX() - vh.getWidth() / 2);//ハンドルの中心を渡す
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /*ハンドルの位置(x)を設定する。
    setLocationを独自に呼ばず、この関数を利用すること
    したがって、xにはマウスからの直接のxの値を指定できる。*/
    private void moveHandle(int x) {
        //ハンドルがはみ出ないようにレンジの範囲内に収める
        if (range_min >= x) {
            //最小値
            vh.setLocation(0, 0);
        } else if (range_max <= x) {
            //最大値
            vh.setLocation(range_max, 0);
        } else {
            //レンジ内に収まる
            vh.setLocation(x, 0);
        }
    }//moveHandle

    /*現在のボリュームの値を100分率+0で手に入れる
    取得できるxの値は 0  <= x <= 100
     */
    public int getCurtrentVolume() {
        float unit = (float) 100 / range_max;
        //最小でMuteできるようにintに丸める
        return (int) Math.round(unit * vh.getX());
    }//getCurrentVolume

    private class VolumeHandle extends JPanel {

        private GeneralManager gm;
        private Image bg;

        //コンストラクタ
        public VolumeHandle(Image background_image) {
            setOpaque(false);
            //イメージの読み込み
            bg = background_image;

            //位置とサイズの設定
            setBounds(0, 0, bg.getWidth(null), bg.getHeight(null));

            repaint();//描画
        }//コンストラクタ

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //背景の描画
            g.drawImage(bg, 0, 0, bg.getWidth(null), bg.getHeight(null), null);
            g.dispose();
        }//paintComponent

    }//VolumeHandle
}//VolumeControlPanel
