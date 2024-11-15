package FolderPlayer.ui.MainPanelComponents;

import FolderPlayer.Music.MusicItem;
import FolderPlayer.Music.Playable;
import FolderPlayer.managers.ColorManager;
import FolderPlayer.managers.GeneralManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 *
 * @author  AC34
 */
/**/
public class MusicPanelItem extends JPanel implements MouseListener {

    //固定
    public static final int LEFT_LABEL_INDENT = 20;
    public static final int RIGHT_LABEL_INDENT = LEFT_LABEL_INDENT / 2;
    public static final int PANEL_HEIGHT = 25;
    public static final int DURATION_PANEL_WIDTH = 65;

    private MusicItem my_music;
    private GeneralManager gm;
    private int ordinal_id;
    private JLabel title_label;
    private JLabel duration_label;
    private boolean is_selected;
    private boolean is_mouse_on;
    private Color current_background;
    private int scroll_width;

    //コンストラクタ
    public MusicPanelItem(GeneralManager general_manager, MusicItem item, int width, int height) {
        my_music = item;//再生操作用クラスの登録
        gm = general_manager;//このパネルはUiManagerへは登録しない

        //パネルの設定
        /*このパネルに関して、
        setBoundsでのxy座標は決めず、このアイテムを格納するコンポーネントの
        レイアウトマネージャに設定を委ねる
        ただし、サイズ設定に関してはマニュアルで設定する。
         */
        setMaximumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setOpaque(true);
        setLayout(null);

        //フラグの初期化
        is_selected = false;
        is_mouse_on = false;

        //各種ラベルの初期化
        scroll_width = ((Integer) UIManager.get("ScrollBar.width")).intValue();
        title_label = new JLabel("");
        int tlx = LEFT_LABEL_INDENT;
        int tlw = width - DURATION_PANEL_WIDTH - RIGHT_LABEL_INDENT - LEFT_LABEL_INDENT - scroll_width;
        title_label.setBounds(tlx, 0, tlw, height);

        duration_label = new JLabel("", SwingConstants.RIGHT);
        int dlx = tlw + LEFT_LABEL_INDENT;
        int dlw = DURATION_PANEL_WIDTH;
        duration_label.setBounds(dlx, 0, dlw, height);

        //ラベルの搭載(この時点では文字は空)
        add(title_label);
        add(duration_label);

        //idの初期化
        ordinal_id = 0;
        setBackground(Color.blue);

        //マウスリスナを搭載
        addMouseListener(this);
    }//MusicPaneltem

    /*このアイテム(インスタンス)を保持している配列の順番に対応する番号を設定
        自然数のみ受け付ける。
        0は初期値*/
    public void setOrdinalId(int id) {
        if (id <= 0) {
            //0はデフォルトの値としてインスタンス化時に設定される値である
            //また、マイナスの値を順番の番号としては受け付けない。
        }
        ordinal_id = id;
    }

    /**
     * 保持しているMusitItemを返す
     * MusicItemがnullの場合も考えられる
     * @return MusicItem
     */
    public MusicItem getMusicItem(){
        return my_music;
    }
    
    /*このアイテム(インスタンス)を保持している配列の順番に対応する番号を取得*/
    public int getOrdinalId() {
        return ordinal_id;
    }

    /*このパネルの設定をして、setVisibleを呼ぶ*/
    public void show() {
        //配列の順番がセットされていない場合例外をスローする
        if (ordinal_id == 0) {
            throw new UnsupportedOperationException("ordinal_id has not been set yet.");
        }
        //文字色の設定
        updateForeground();

        //背景色の設定
        updateBackground();

        //メディア情報の取得
        if(!my_music.isFileInfoLoaded()){
            my_music.loadFileInfo();
        }

        //情報取得後ラベルの更新
        updateTitleLabel(my_music.getTitle());
        updateDurationLabel(my_music.getDurationText());

        //見た目の更新
        updateView();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        //何もしない
        //clickでやれることはpressedで行わせる
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //選択を解除して、自身に選択を入れる
        MusicPanel mp = gm.getUiManager().getMusicPanel();

        mp.cancelAllSelections();
        //キャンセル後にフラグを更新
        is_selected = true;
        //他のアイテムを含め見た目を更新
        mp.updateAllItems();
        
        //再生開始(再生中でない場合のみ)
        if(my_music.getCurrentState()!=Playable.PLAYABLE_STATE_PLAYING){
                this.my_music.play();
        }
    }//mousePressed

    @Override
    public void mouseReleased(MouseEvent e) {
        updateView();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        is_mouse_on = true;
        updateView();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        is_mouse_on = false;
        updateView();
    }//mouseExited

    /*見た目の内容に変更があるとき、必要に応じて更新
    自身のみを更新する場合も、
    管理側から一意に呼び出す場合もあるため、publicである
     */
    public void updateView() {
        updateBackground();
        updateForeground();
        updateLabeLColor();
        repaint();
        System.gc();
    }//updateView

    /*操作による選択フラグを解除
    選択されていた場合に、見た目の更新も行う*/
    public void cancelSellection() {
        if(!is_mouse_on){
             is_selected = false;
        }
    }//cancelSellection
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //ボーダーの描画
        updatePseudoBorder(g);
    }
    
    /*Itemの状態に応じて背景を切り替える*/
    private void updateBackground() {
        ColorManager cm = gm.getColorManager();
        //選択されている状態
        if (is_selected) {
            //選択された状態
            current_background = cm.getColor(ColorManager.MUSIC_PANEL_ITEM_SELECTED_BACKGROUND);

        } else {
            //通常の状態
            //奇数と偶数で色を切り替える
            if (ordinal_id % 2 == 0) {
                //偶数
                current_background = cm.getColor(ColorManager.MUSIC_PANEL_ITEM_ODD_BACKGROUND);
            } else {
                //奇数
                current_background = cm.getColor(ColorManager.MUSIC_PANEL_ITEM_EVEN_BACKGROUND);
            }
        }
        setBackground(current_background);
    }//updateBackground

    /*Itemの状態に応じて文字色を切り替える*/
    private void updateForeground() {
        ColorManager cm = gm.getColorManager();
        if (is_selected) {
            //選択されている場合
            setForeground(cm.getColor(ColorManager.MUSIC_PANEL_ITEM_SELECTED_FOREGROUND));
        } else {
            //デフォルト
            setForeground(cm.getColor(ColorManager.MUSIC_PANEL_ITEM_DEFUALT_FOREGROUND));
        }
    }//updateForeground

    /**/
    private void updateTitleLabel(String title) {
        title_label.setText(title);
    }

    /**/
    private void updateDurationLabel(String duration_text) {
        duration_label.setText(duration_text);
    }

    /**/
    private void updateLabeLColor() {
        ColorManager cm = gm.getColorManager();
        if (is_selected) {
            //曲が選択されている場合
            title_label.setForeground(cm.getColor(ColorManager.TEXT_SELECTED));
            duration_label.setForeground(cm.getColor(ColorManager.TEXT_SELECTED));
        } else {
            //デフォルトの状態
            title_label.setForeground(cm.getColor(ColorManager.TEXT_WHITE));
            duration_label.setForeground(cm.getColor(ColorManager.TEXT_WHITE));
        }
    }

    /*実際にはボーダーではなく、Graphicsでの矩形描画であるためpseudo。
    
     */
    private void updatePseudoBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        ColorManager cm = gm.getColorManager();
        int stroke_thickness = 1;
        g2.setColor(current_background);//現在の背景色と同じ
        if (is_mouse_on) {
            //マウスホバー時
            g2.setColor(cm.getColor(ColorManager.MUSIC_PANEL_ITEM_HOVER_BORDER));
        }
        g2.setStroke(new BasicStroke(stroke_thickness));
        g2.drawRoundRect(0, 0,
                getWidth()-scroll_width,
                getHeight() - (stroke_thickness * 2),
                stroke_thickness * 2,
                stroke_thickness * 2);
    }//upsedatePseudoBorder
    
    /**
     * ユーザーの操作により選択状態にある場合にtrue
     * @return Boolean
     */
    public Boolean isSelected(){
        return is_selected;
    }
    
    /**
     * ユーザーの操作により選択状態にする場合に呼ぶ
     * また、準じて表示も更新される
     */
    public void Select(){
        is_selected = true;
        this.updateView();
        //selectの状態に応じてスクロールの調整を行わせる
        gm.getUiManager().getMusicPanel().forceScrollAdjustmentToSelected();
    }
        /**
     * ユーザーの操作により選択状態を解除する場合に呼ぶ
     * また、準じて表示も更新される
     */
    public void UnSelect(){
        is_selected = false;
        this.updateView();
    }
}//MusicPaneltem
