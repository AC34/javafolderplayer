package FolderPlayer.ui.MenuPanelComponents;

import FolderPlayer.Music.MusicItem;
import FolderPlayer.Music.Playable;
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
public class PlayChooserPanel extends JPanel implements MouseListener {

    private GeneralManager gm;

    //背景画像
    private Image bg_active;
    private Image bg_hover;
    private Image bg_pressed;
    private Image bg_disabled;
    private Image current_background;

    //コンストラクタ
    public PlayChooserPanel(GeneralManager general_manager, int x, int y, int size) {
        //GeneralManagerの登録
        gm = general_manager;
        gm.getUiManager().setPlayChooserPanel(this);

        //パネルの設定
        setBounds(x, y, size, size);
        addMouseListener(this);
        setOpaque(false);

        //背景画像の読み込み
        bg_active = gm.getImageManager().getImage("menu_play_active.png");
        bg_hover = gm.getImageManager().getImage("menu_play_hover.png");
        bg_pressed = gm.getImageManager().getImage("menu_play_pressed.png");
        bg_disabled = gm.getImageManager().getImage("menu_play_disabled.png");
        current_background = bg_active;//初期画面

        repaint();//描画
    }//コンストラクタ

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
        //Pressedへ移譲
    }

    @Override
    public void mousePressed(MouseEvent e) {
        current_background = bg_pressed;
        repaint();

        //play処理
        MusicItem item = null;
        try {
            //現在選択中のものを取得
            item = gm.getUiManager().getMusicPanel().getCurrentlySelected().getMusicItem();
        } catch (Exception ex) {
            //現在再生中の曲がない
            //起動後フォルダを１度も選んでいないなど
        }
        if (item != null) {
            //現在再生中でないものを取得
            if (item.getCurrentState() != Playable.PLAYABLE_STATE_PLAYING) {
                item.play();//新規も再開も受け付ける
            }
        }//itemのnull判定

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
