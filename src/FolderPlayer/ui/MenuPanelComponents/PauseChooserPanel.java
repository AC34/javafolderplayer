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
public class PauseChooserPanel extends JPanel implements MouseListener {

    private GeneralManager gm;

    //背景画像
    private Image bg_active;
    private Image bg_hover;
    private Image bg_pressed;
    private Image bg_disabled;
    private Image current_background;

    public PauseChooserPanel(GeneralManager general_manager, int x, int y, int size) {
        //GeneralManagerの登録
        gm = general_manager;
        gm.getUiManager().setPauseChooserPanel(this);

        //パネルの設定
        setBounds(x, y, size, size);
        addMouseListener(this);
        setOpaque(false);

        //背景画像の読み込み
        bg_active = gm.getImageManager().getImage("menu_pause_active.png");
        bg_hover = gm.getImageManager().getImage("menu_pause_hover.png");
        bg_pressed = gm.getImageManager().getImage("menu_pause_pressed.png");
        bg_disabled = gm.getImageManager().getImage("menu_pause_disabled.png");
        current_background = bg_active;//初期画面

        repaint();//描画

        //デフォルトではPlayChooserPanelを表示する為非表示
        deactivatePanel();
    }

    //パネル自体の操作を無効化する
    public void deactivatePanel() {
        setVisible(false);
    }

    //パネル事態の操作を有効化する
    public void activatePanel() {
        //activateされたタイミングでは、マウスはhoverしている
        current_background = bg_hover;
        setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //背景画像の描画
        g.drawImage(current_background, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        current_background = bg_pressed;
        //pause処理
        MusicItem item = null;
        try {
            item = gm.getUiManager().getMusicPanel().getCurrentlyPlaying().getMusicItem();
        } catch (Exception ex) {
            //現在再生中の曲がない
            //起動後フォルダを１度も選んでいないなど
        }
        if (item != null) {
            item.pause();

            //Playへの切り替え
            deactivatePanel();
            gm.getUiManager().getPlayChooserPanel().activatePanel();
        }
        repaint();
    }

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
