package FolderPlayer.ui.MenuPanelComponents;

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
public class NextChooserPanel extends JPanel implements MouseListener {

    private GeneralManager gm;

    //背景画像
    private Image bg_active;
    private Image bg_hover;
    private Image bg_pressed;
    private Image bg_disabled;
    private Image current_background;

    //コンストラクタ
    public NextChooserPanel(GeneralManager general_manager, int x, int y, int size) {
        //GeneralManagerの登録
        gm = general_manager;
        gm.getUiManager().setNextChooserPanel(this);

        //パネルの設定
        setBounds(x, y, size, size);
        addMouseListener(this);
        setOpaque(false);

        //背景画像の読み込み
        bg_active = gm.getImageManager().getImage("menu_next_active.png");
        bg_hover = gm.getImageManager().getImage("menu_next_hover.png");
        bg_pressed = gm.getImageManager().getImage("menu_next_pressed.png");
        bg_disabled = gm.getImageManager().getImage("menu_next_disabled.png");
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
        gm.getUiManager().getMusicPanel().playNextToSelected();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        current_background = bg_pressed;
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
