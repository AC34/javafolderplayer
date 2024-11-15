package FolderPlayer.ui.MainPanelComponents;

import FolderPlayer.Music.MusicItem;
import FolderPlayer.Music.Playable;
import FolderPlayer.managers.GeneralManager;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;

/**
 *
 * @author  AC34
 */
public class MusicPanel extends JScrollPane {

    private GeneralManager gm;
    private MusicPanelViewPane mpvp;
    private int scrollbar_width;
    private MusicPanelItem[] current_items;
    private volatile Boolean is_scroll_adjusting;

    //コンストラクタ
    public MusicPanel(GeneralManager general_manager, int x, int y, int width, int height) {
        gm = general_manager;
        gm.getUiManager().setMusicPanel(this);

        //アイテムの高さを初期化
        current_items = null;
        is_scroll_adjusting = false;

        /*スクロールバーの横幅は、OSごと(またLinuxの場合UIソフトウェアごと)に異なる
         このUIManagerクラスはSwingのもので、本プログラムではなくJava側の実装*/
        scrollbar_width = ((Integer) UIManager.get("ScrollBar.width")).intValue();

        //パネルの設定
        setOpaque(false);
        setBorder(null);

        setBounds(x, y, width, height);

        //縦にのみスクローラブルである
        getVerticalScrollBar().setOpaque(true);
        getVerticalScrollBar().setBorder(null);
        getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
        getVerticalScrollBar().setUnitIncrement(18);//スクロールのスピード
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

        //ビューポート用のパネルを作成
        mpvp = new MusicPanelViewPane(x, y, width, height);

        getViewport().setSize(width - scrollbar_width, height);
        getViewport().setOpaque(false);
        setViewportView(mpvp);

    }//コンストラクタ

    public void updateMusicList(MusicItem[] music_item) {

        //MusicPanelItemの生成
        MusicPanelItem[] panel_item = new MusicPanelItem[music_item.length];
        int id = 1;
        for (int i = 0; i < music_item.length; i++) {
            panel_item[i] = new MusicPanelItem(gm, music_item[i], getWidth(), MusicPanelItem.PANEL_HEIGHT);
            panel_item[i].setOrdinalId(id);
            mpvp.add(panel_item[i]);
            panel_item[i].show();
            id++;
        }
        //格納
        current_items = panel_item;

        //mpvpの高さを調整
        adjustHeight();
    }//updteMusicLis

    private class MusicPanelViewPane extends JPanel {

        public MusicPanelViewPane(int x, int y, int width, int height) {
            //パネルの設定
            setBounds(x, y, width, height);
            setPreferredSize(new Dimension(width, height));
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            setOpaque(false);
        }
    }//MusicItemPane

    /*
    コンポーネントをすべて配置し終わった後にコールし、調整する目的
    コンポーネントの個数*コンポーネントの高さ分に調整*/
    public void adjustHeight() {
        if (mpvp.getComponentCount() > 0) {
            int height = mpvp.getComponentCount() * MusicPanelItem.PANEL_HEIGHT;
            mpvp.setPreferredSize(new Dimension(getWidth(), height));
        } else {
            //コンポーネントが設定されていない場合
            //親のサイズに合わせる
            mpvp.setPreferredSize(new Dimension(getWidth(), getHeight()));
        }
        mpvp.revalidate();
        repaint();
    }

    /**
     * 現在一時停止中のフラグが立っているMusicPanelItemを返す すべて再生中でない場合にはnullを返す
     *
     * @return
     */
    public MusicPanelItem getCurrentlyPausing() {
        MusicPanelItem res = null;
        MusicPanelItem[] items = getAllMusicPanelItems();
        //一度も曲を表示していない場合null
        if (items == null) {
            return res;
        }
        for (int i = 0; i < items.length; i++) {
            if (items[i].getMusicItem().getCurrentState() == MusicItem.PLAYABLE_STATE_PAUSING) {
                res = items[i];
                i = items.length;
            }
        }
        return res;
    }

    /**
     * 現在再生中のフラグが立っているMusicPanelItemを返す すべて再生中でない場合にはnullを返す
     *
     * @return
     */
    public MusicPanelItem getCurrentlyPlaying() {
        MusicPanelItem res = null;
        MusicPanelItem[] items = getAllMusicPanelItems();
        //一度も曲を表示していない場合null
        if (items == null) {
            return res;
        }
        for (int i = 0; i < items.length; i++) {
            if (items[i].getMusicItem().getCurrentState() == MusicItem.PLAYABLE_STATE_PLAYING) {
                res = items[i];
                i = items.length;
            }
        }
        return res;
    }

    /**
     * 現在一時停止中のものをすべて停止中へ対応させる 再生中のものは停止させない
     */
    public void stopAllPausingItems() {
        MusicPanelItem[] items = getAllMusicPanelItems();
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                //一時停止中のものとマッチ
                if (items[i].getMusicItem().getCurrentState() == Playable.PLAYABLE_STATE_PAUSING) {
                    items[i].getMusicItem().stop();
                }
            }//for
        }
    }//stopAllPausingItems

    /**
     * 現在再生中のものをすべて停止中へ対応させる 一時停止中のものは停止させない
     */
    public void stopAllPlayingItems() {
        MusicPanelItem[] items = getAllMusicPanelItems();
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                //一時停止中のものとマッチ
                if (items[i].getMusicItem().getCurrentState() == Playable.PLAYABLE_STATE_PLAYING) {
                    items[i].getMusicItem().stop();
                }
            }//for
        }//if

    }//stopAllPausingItems

    /**
     * 現在取得できるすべてのMusicPanelIItemの配列を返す 存在しない場合はnullを返す
     *
     * @return
     */
    public MusicPanelItem[] getAllMusicPanelItems() {
        if (current_items != null) {
            if (current_items.length > 0) {
                return current_items;
            } else {
                //配列の長さが0の場合
                return null;
            }
        } else {
            //nullの場合
            return null;
        }
    }

    /**
     * 現在選択されているMusicPanelItemを返す 全てselect状態でない場合にはnullを返す
     *
     * @return
     */
    public MusicPanelItem getCurrentlySelected() {
        MusicPanelItem[] items = getAllMusicPanelItems();
        if (items == null) {
            return null;
        }
        MusicPanelItem res = null;
        for (int i = 0; i < items.length; i++) {
            if (items[i].isSelected()) {
                res = items[i];
                i = items.length;//ループの中断
            }
        }
        return res;
    }//getCurrentlySelected

    /*現在表示されているMusicPanelItemの選択フラグを全て解除*/
    public void cancelAllSelections() {
        if (getAllMusicPanelItems() != null) {
            for (int i = 0; i < getAllMusicPanelItems().length; i++) {
                getAllMusicPanelItems()[i].cancelSellection();
            }//for
        }//if
    }//cancelAllSelection

    public void updateAllItems() {
        if (getAllMusicPanelItems() != null) {
            for (int i = 0; i < getAllMusicPanelItems().length; i++) {
                getAllMusicPanelItems()[i].updateView();
            }//for
        }//if
    }

    /**
     * MusicPanel上にある全てのMusicPanelItemを削除する
     */
    public void removeAllItems() {
        this.stopAllPlayingItems();
        this.stopAllPausingItems();
        current_items = null;
        mpvp.removeAll();
        mpvp.revalidate();
        mpvp.repaint();
        System.gc();
    }//removeallItems

    /**
     * 現在表示中の曲のうち、Selected状態にある曲の次の曲を再生する 曲を表示していない/Select状態の曲が無い場合は何もしない
     * Selectedされているものがリストの最後のアイテムの場合、リストの最初のものを再生する
     *
     * @return
     */
    public void playNextToSelected() {
        MusicPanelItem[] panels = getAllMusicPanelItems();
        //getAllMusicPaneltemsがnullでない場合は必ず再生できる
        if (panels != null) {
            //for文で順繰りに検査し再生
            for (int i = 0; i < panels.length; i++) {
                if (panels[i].isSelected()) {
                    //再生箇所別の処理
                    //インデックスの前処理
                    int index = i + 1;
                    if (i == panels.length - 1) {
                        //インデックスを0へ戻しループ処理
                        index = 0;
                    }
                    //再生し、Select表示を移動
                    panels[index].getMusicItem().play();
                    panels[i].UnSelect();//現在のセレクトを解除
                    panels[index].Select();
                    //ループ終了処理
                    i = panels.length;
                }
            }//for
        }//if panels!=null
    }

    /**
     * 現在表示中の曲のうち、Selected状態にある曲の前の曲を再生する 曲を表示していない/Select状態の曲が無い場合は何もしない
     * Selectedされているものがリストの最初のアイテムの場合、リストの最後のものを再生する
     *
     * @return
     */
    public void playPreviousToSelected() {
        MusicPanelItem[] panels = getAllMusicPanelItems();
        //getAllMusicPaneltemsがnullでない場合は必ず再生できる
        if (panels != null) {
            //for文で順繰りに検査し再生
            for (int i = 0; i < panels.length; i++) {
                if (panels[i].isSelected()) {
                    //再生箇所別の処理
                    //インデックスの前処理
                    int index = i - 1;
                    if (i == 0) {
                        //インデックスを最後尾へ戻し逆ループ処理
                        index = panels.length - 1;
                    }
                    //再生し、Select表示を移動
                    panels[index].getMusicItem().play();
                    panels[i].UnSelect();//現在のセレクトを解除
                    panels[index].Select();
                    //ループ終了処理
                    i = panels.length;
                }
            }//for
        }//if panels!=null

    }//playPreviousToSelected

    /**
     * MusicPanelIItemの表示位置が、Viewportviewからはみ出ている場合
     * そのMusicPanelItemの全体が見える位置まで調整する
     *
     * @return
     */
    public void forceScrollAdjustmentToSelected() {
        if (!is_scroll_adjusting) {
            //アジャスターを生成して処理を任せる
            AutoScrollAdjuster adjuster = new AutoScrollAdjuster(gm);
            adjuster.setDaemon(true);
            is_scroll_adjusting = true;
            adjuster.start();
        }
    }//forceScrollAdjustmentToSelecter

    /**
     * スクロールアジャスター側からの終了処理を受け付ける 以降 forceScrollAdjustmentToSelectedのコールが有効に機能する
     */
    public void notifyScrollAdjustEnded() {
        is_scroll_adjusting = false;
    }

    private class AutoScrollAdjuster extends Thread {

        private MusicPanel panel;
        private GeneralManager gm;
        private static final int ADJUST_DIRECTION_NONE = 0;
        private static final int ADJUST_DIRECTION_UP = 1;
        private static final int ADJUST_DIRECTION_DOWN = 2;

        /**
         * コンストラクタ
         *
         * @param MusicPanel
         * @return
         */
        public AutoScrollAdjuster(GeneralManager general_manager) {
            panel = general_manager.getUiManager().getMusicPanel();
            gm = general_manager;
        }

        @Override
        public void run() {
            int direction = isAdjustNeeded();
            switch (direction) {
                case ADJUST_DIRECTION_UP:
                    adjustUp();
                    break;
                case ADJUST_DIRECTION_DOWN:
                    adjustDown();
                    break;
            }//case
            //調整後に更新
            direction = isAdjustNeeded();
            if (direction == ADJUST_DIRECTION_NONE) {
                //これがなければメインスレッドで一度きりしか forceScrollAdjustmentToSelectedが走らない
                panel.notifyScrollAdjustEnded();
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                }
                //更に調整
                Thread t = new Thread(this);
                t.setDaemon(true);
                t.start();
            }
        }//run

        /**
         * そもそも調整する必要があるかを調べ、intで返す intは定数ADJUST_DIRECTIONのいずれか
         *
         * @param MusicPanel
         * @return
         */
        private int isAdjustNeeded() {
            JViewport v = panel.getViewport();
            MusicPanelItem selected = panel.getCurrentlySelected();

            //現在のスクロールの位置=バー.value()+バーの高さ
            //Vertical Scroll Height
            int vsh = panel.getVerticalScrollBar().getValue();

            /*チェック項目は２つ、
                            1.上に戻る必要があるとき
                                (selectedの位置(の値)<vsh)
                                ※天井の辺でチェック
                            2.下に行く必要がある時
                                (selectedの位置(と高さ)>vsh+バーの高さ
                                ※底辺でチェック
             */
            //1上に戻る必要があるとき
            if (selected.getY() < vsh) {
                return ADJUST_DIRECTION_UP;
            }
            // 2.下に行く必要がある時(下に少しでもはみ出るならば)
            int sbv = selected.getY() + selected.getHeight();//SelectedBottomValue
            vsh += v.getHeight();
            if (sbv > vsh) {
                return ADJUST_DIRECTION_DOWN;
            }
            //何もなければNONE
            return ADJUST_DIRECTION_NONE;
        }//isAdjustNeeded

        private void adjustDown() {
            JScrollBar s = panel.getVerticalScrollBar();
            s.setValue(s.getValue() + 1);
        }

        private void adjustUp() {
            JScrollBar s = panel.getVerticalScrollBar();
            s.setValue(s.getValue() - 1);
        }

    }//AutoScrollAdjuster

}//MusicPanel
