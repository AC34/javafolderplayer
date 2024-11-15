package FolderPlayer.ui.MenuPanelComponents;

import FolderPlayer.Time.MicroSecondTimeConverter;
import FolderPlayer.managers.ColorManager;
import FolderPlayer.managers.GeneralManager;
import FolderPlayer.String.ToStringConverter;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author  AC34
 */
public class DurationIndicatorPanel extends JPanel {

    private GeneralManager gm;
    private IndicationLabel il;

    //コンストラクタ
    public DurationIndicatorPanel(GeneralManager general_manager, int x, int y, int width, int height) {
        //GeneralManagerの設定
        gm = general_manager;
        gm.getUiManager().setDurationIndicatorPanel(this);

        //パネルの設定
        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);

        //ラベルの設定
        /*
                ラベル表示の微調整について(縦横の中央寄せ表示について)
                フォント用のptサイズの抽出とpxでの設定について、
                一見簡単に思えるが情報と時間が必要なため、行わないこととした。
                固定値のyを与えることでWindows環境での縦の見た目を調整した。
                */
        il = new IndicationLabel(0, 4, width, height, 14);

        add(il);
    }//コンストラクタ

    /**
     * durationで得た時間を設定し、更新する
     *
     * @param time
     */
    public void setCurrentTime(long time) {
        il.setCurrentTime(time);
        il.updateLabel();
    }

    /**
     * durationで得た時間を設定し、更新する
     *
     * @param time
     */
    public void setMaximumTime(long time) {
        il.setMaximumTime(time);
        il.updateLabel();
    }

    private class IndicationLabel extends JLabel {

        //時刻操作用変数
        private String indicationString = "";
        /*表示内容の変更に対応する際に、Durationの値が必要となる場合があるため
                内部的にDurationで保持する。*/
        private long current_time;
        private long maximum_time;
        private String current_str;
        private String maximum_str;

        //文字列操作用定数
        private final String DEFAULT_TIME_STR = "00:00";
        private final String TIME_DELIMITER = "/";
        private final String MIN_SEC_DELIMITER = ":";
        public final int INDICATION_DIGITS = 2;

        //コンストラクタ
        public IndicationLabel(int x, int y, int width, int height, int font_size) {
            //ラベルの設定
            setOpaque(false);

            //サイズ設定
            setBounds(x,y,width,height);
            setMinimumSize(new Dimension(width, height));
            setPreferredSize(new Dimension(width, height));
            setMaximumSize(new Dimension(width, height));

            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.BOTTOM);

            //フォントの設定
            Font f = this.getFont();
            this.setFont(new Font(f.getName(), f.getStyle(), font_size));

            ColorManager cm = gm.getColorManager();
            setForeground(cm.getColor(ColorManager.TEXT_WHITE));

            //時間の初期化
            current_time = 0;
            maximum_time = 0;

            //後に設定する
            //ラベル文字の初期化と配置
            resetIndication();
        }//コンストラクタ

        /*現在時刻と最大時間を0に合わせたデフォルトの表示をする*/
        public void resetIndication() {
            indicationString = DEFAULT_TIME_STR + TIME_DELIMITER + DEFAULT_TIME_STR;
            setText(indicationString);
        }//resetIndication

        public void setCurrentTime(long current) {
            if (current_time != current) {
                current_time = current;
                current_str = durationToString(current);
            }
        }//setCurrentTime

        public void setMaximumTime(long maximum) {
            //違う場合のみ更新をする
            if (maximum_time != maximum) {
                maximum_time = maximum;
                maximum_str = durationToString(maximum_time);
            }
        }//setMaximumTime

        /*Durationクラスから表示に適したStringへ変換し返す
                     00:00を標準とし、1音楽ファイル当たり最大99分とする。
                    後にこのフォーマットを変える可能性はあるが、デザインから変更する必要がある。
         */
        private String durationToString(long time) {
            MicroSecondTimeConverter mstc = new MicroSecondTimeConverter(time);
            int min = mstc.getMinutesWithin();
            //99分以上を99分へ(変わるのは表示だけ)
            if (min > 99) {
                min = 99;
            }
            String res = ToStringConverter.fillHeadWithZero(min, INDICATION_DIGITS);
            res += MIN_SEC_DELIMITER;
            //99秒以上はmstcからは来ない
            res += ToStringConverter.fillHeadWithZero(mstc.getTheRestSecsOfMinutesWithin(), INDICATION_DIGITS);
            return res;
        }//durationToString

        /*実際にsetTextを呼び、setCurrentTimeとsetMaximumTimeで更新された値を表示する。*/
        public void updateLabel() {
            indicationString = current_str + TIME_DELIMITER + maximum_str;
            setText(indicationString);
        }//updateLabel
    }//IndicationLabel
}//DurationIndicatorPanel
