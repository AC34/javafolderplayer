package FolderPlayer.managers;

import java.awt.Color;

/**
 *
 * @author  AC34
 */
public class ColorManager {

    private GeneralManager gm;
    //MainPanel
    public static int[] MAIN_PANEL_BACKGROUND = {77, 77, 77};

    //テキスト用白
    public static int[] TEXT_WHITE = {236, 236, 236};

    //StatusPanel背景色
    public static int[] STATUS_PANEL_BACKGROUND = {51, 51, 51};

    //MusicPanel用
    public static int[] MUSIC_PANEL_BACKGROUND = {77, 77, 77};
    
    //MusicPanelItem用
    public static int[] TEXT_SELECTED = {246, 227, 159};
    public static int[] MUSIC_PANEL_ITEM_SELECTED_FOREGROUND = {246, 227, 159};
    public static int[] MUSIC_PANEL_ITEM_DEFUALT_FOREGROUND = {249, 249, 249};
    public static int[] MUSIC_PANEL_ITEM_ODD_BACKGROUND = {51, 51, 51};//奇数用
    public static int[] MUSIC_PANEL_ITEM_EVEN_BACKGROUND = {77, 77, 77};//偶数用拝啓
    public static int[] MUSIC_PANEL_ITEM_SELECTED_BACKGROUND = {0, 0, 0};
    public static int[] MUSIC_PANEL_ITEM_HOVER_BORDER = {255, 230, 128};

    //コンストラクタ
    public ColorManager(GeneralManager general_manager) {
        gm = general_manager;
    }

    public Color getColor(int[] rgb) {
        return new Color(rgb[0], rgb[1], rgb[2]);
    }//getColor

}//ColorManager
