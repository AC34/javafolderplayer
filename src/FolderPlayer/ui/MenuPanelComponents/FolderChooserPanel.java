package FolderPlayer.ui.MenuPanelComponents;

import FolderPlayer.Dialog.FolderChooserDialog;
import FolderPlayer.Music.MusicItem;
import FolderPlayer.managers.FileManager;
import FolderPlayer.managers.GeneralManager;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.JPanel;

/**
 *
 * @author  AC34
 */
public class FolderChooserPanel extends JPanel implements MouseListener {

    private GeneralManager gm;

    //背景画像
    private Image bg_active;
    private Image bg_hover;
    private Image bg_pressed;
    private Image bg_disabled;
    private Image current_background;

    //コンストラクタ
    public FolderChooserPanel(GeneralManager general_manager, int x, int y, int size) {
        //GeneralManagerの登録
        gm = general_manager;
        gm.getUiManager().setFolderChooserPanel(this);

        //パネルの設定
        setBounds(x, y, size, size);
        addMouseListener(this);
        setOpaque(false);

        //背景画像の読み込み
        bg_active = gm.getImageManager().getImage("menu_folder_active.png");
        bg_hover = gm.getImageManager().getImage("menu_folder_hover.png");
        bg_pressed = gm.getImageManager().getImage("menu_folder_pressed.png");
        bg_disabled = gm.getImageManager().getImage("menu_folder_disabled.png");
        current_background = bg_active;//初期画面

        repaint();//描画
    }//コンストラクタ

    /*音楽フォルダの選択*/
    private void chooseFolder() {
        //ダイアログ　の準備
        FolderChooserDialog dialog = new FolderChooserDialog(gm);
        dialog.openDialog();

        if (dialog.isSelected()) {
            //フォルダアイテムの設定
            showMusicItems(dialog);
        }
    }//chooseFolder

    /*音楽アイテムの表示を更新
    1.拡張子を指定してフォルダーをスキャン
    2.ファイルのリストを取得
    3.MusicPanel上へアイテムを表示*/
    private void showMusicItems(FolderChooserDialog dialog) {

        //選択された場合のみ処理
        String path = dialog.getSelectedPath();
        //フォルダ名のみを抽出
        String[] path_arr = path.split(Pattern.quote(File.separator));//区切り文字はescapeする必要がある
        String folder_name = path_arr[path_arr.length - 1];
        path_arr = null;

        //フォルダが存在する前提
        //フォルダをスキャンさせる
        String[] attrs = FileManager.joinAttributesArray(
                FileManager.ATTRIBUTES_MP3,
                FileManager.ATTRIBUTES_WAVE);
        //デバッグ用
        /*for(int i = 0; i < attrs.length;i++){
                    System.out.println("attrs"+i+"["+attrs[i]+"]");
        }*/

        dialog.scanFolder(dialog.getSelectedPath(), attrs);
        //有効な曲目がある場合のみ見た目を含めて変更を反映する
        if (dialog.getList().length > 0) {
            //フォルダ名を設定
            gm.getUiManager().getStatusPanel().updateMessage(folder_name);

            //以下の処理は、スキャン結果が0個以上の場合にのみ実行する
            //リストのMusicItemの作成
            String[] names = dialog.getList();
            ArrayList<MusicItem> candidates = new ArrayList<MusicItem>();
            //musicを作成
            for (int i = 0; i < names.length; i++) {
                MusicItem m = new MusicItem(gm);
                m.setPath(names[i]);
                if (m.loadFileInfo()) {
                    //ローディングに成功した場合のみリストへ追加
                    candidates.add(m);
                }
            }
            //固定長配列へ変換
            MusicItem[] musics = candidates.toArray(new MusicItem[candidates.size()]);

            //すでにMusicPanelにアイテムがある場合、すべて削除
            gm.getUiManager().getMusicPanel().removeAllItems();
            //MusicItemの配列を渡し、表示
            gm.getUiManager().getMusicPanel().updateMusicList(musics);

            System.gc();
        } else {
            //フォルダは選択されたが、
            //指定した拡張子の音楽ファイルが存在しない場合の処理
            //何もしない
        }
        //ダイアログの保持データをクリア
        dialog.flush();
    }//showMusicItems

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
        repaint();
        //フォルダの選択処理
        chooseFolder();
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
}//Class

