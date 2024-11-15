package FolderPlayer.ui.MenuPanelComponents;

import FolderPlayer.Music.MusicItem;
import FolderPlayer.managers.GeneralManager;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author  AC34
 */
public class StopChooserPanel extends JPanel implements MouseListener {

    private GeneralManager gm;

    //背景画像
    private Image bg_active;
    private Image bg_hover;
    private Image bg_pressed;
    private Image bg_disabled;
    private Image current_background;

    //コンストラクタ
    public StopChooserPanel(GeneralManager general_manager, int x, int y, int size) {
        //GeneralManagerの登録
        gm = general_manager;
        gm.getUiManager().setStopChooserPanel(this);

        //パネルの設定
        setBounds(x, y, size, size);
        addMouseListener(this);
        setOpaque(false);

        //背景画像の読み込み
        bg_active = gm.getImageManager().getImage("menu_stop_active.png");
        bg_hover = gm.getImageManager().getImage("menu_stop_hover.png");
        bg_pressed = gm.getImageManager().getImage("menu_stop_pressed.png");
        bg_disabled = gm.getImageManager().getImage("menu_stop_disabled.png");
        current_background = bg_active;//初期画面

        repaint();//描画
    }//コンストラクタ

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //背景画像の描画
        g.drawImage(current_background, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //ここでやりたい処理はpressedへ移譲する
    }

    @Override
    public void mousePressed(MouseEvent e) {
        current_background = bg_pressed;
        //現在再生中のアイテムを取得
        MusicItem item = null;
        try {
            item = gm.getUiManager().getMusicPanel().getCurrentlyPlaying().getMusicItem();
            if (item == null) {

            }

        } catch (Exception ex) {
            //現在再生中の曲がない
            //起動後フォルダを１度も選んでいないなど
            //System.out.println("現在再生中の曲はありません。");
        }
        //itemはnullの場合がある
        if (item != null) {
            item.stop();
        }
        //一時停止中のものもストップ
        gm.getUiManager().getMusicPanel().stopAllPausingItems();
        repaint();
    }//mousePressed

    @Override
    public void mouseReleased(MouseEvent e) {
        current_background = bg_hover;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        current_background = bg_hover;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        current_background = bg_active;
        repaint();
    }
}