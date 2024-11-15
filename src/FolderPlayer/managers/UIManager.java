package FolderPlayer.managers;

import FolderPlayer.ui.MainFrame;
import FolderPlayer.ui.MainFramePanel;
import FolderPlayer.ui.MainPanel;
import FolderPlayer.ui.MainPanelComponents.MusicPanel;
import FolderPlayer.ui.MainPanelComponents.StatusPanel;
import FolderPlayer.ui.MenuPanel;
import FolderPlayer.ui.MenuPanelComponents.DurationIndicatorPanel;
import FolderPlayer.ui.MenuPanelComponents.FolderChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.NextChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.PauseChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.PlayChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.PrevChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.StopChooserPanel;
import FolderPlayer.ui.MenuPanelComponents.VolumeControlPanel;

/**
 *
 * @author  AC34
 */

/*UIに関するクラスのインスタンスを管理するマネージャ*/
public class UIManager {

    /*以下の変数名は、netbeansのコード生成機能を使用して
    自動的にゲッターセッターメソッドを作成する目的で
    全てキャメルケースとする
     */
    private GeneralManager gm;
    //メインフレームとパネル
    private MainFrame mainFrame;
    private MainFramePanel mainFramePanel;

    //メインパネルとそのコンポーネント
    private MainPanel mainPanel;
    private StatusPanel statusPanel;
    private MusicPanel musicPanel;

    //メニューパネルとそのコンポーネント
    private MenuPanel menuPanel;
    private DurationIndicatorPanel durationIndicatorPanel;
    private FolderChooserPanel folderChooserPanel;
    private PrevChooserPanel prevChooserPanel;
    private PlayChooserPanel playChooserPanel;
    private PauseChooserPanel pauseChooserPanel;
    private NextChooserPanel nextChooserPanel;
    private StopChooserPanel stopChooserPanel;
    private VolumeControlPanel volumeControlPanel;


    /*コンストラクタ*/
    public UIManager(GeneralManager general_manager) {
        gm = general_manager;
    }

    /**
     * *********************************************************************
     * **********************************************************************
     * **********************************************************************
     * **********************************************************************
     * **********************************************************************
     */
    /*以下のメソッド自動生成したもの*/
    public void setMainPanel(MainFramePanel main_frame_panel) {
        this.mainFramePanel = main_frame_panel;
    }

    public MainFramePanel getMainPanel() {
        return mainFramePanel;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFramePanel(MainFramePanel mainFramePanel) {
        this.mainFramePanel = mainFramePanel;
    }

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setMenuPanel(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
    }

    public MainFramePanel getMainFramePanel() {
        return mainFramePanel;
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    public void setStatusPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    public void setDurationIndicatorPanel(DurationIndicatorPanel durationIndicatorPanel) {
        this.durationIndicatorPanel = durationIndicatorPanel;
    }

    public DurationIndicatorPanel getDurationIndicatorPanel() {
        return durationIndicatorPanel;
    }

    public FolderChooserPanel getFolderChooserPanel() {
        return folderChooserPanel;
    }

    public void setFolderChooserPanel(FolderChooserPanel folderChooserPanel) {
        this.folderChooserPanel = folderChooserPanel;
    }

    public PrevChooserPanel getPrevChooserPanel() {
        return prevChooserPanel;
    }

    public void setPrevChooserPanel(PrevChooserPanel prevChooserPanel) {
        this.prevChooserPanel = prevChooserPanel;
    }

    public PlayChooserPanel getPlayChooserPanel() {
        return playChooserPanel;
    }

    public void setPlayChooserPanel(PlayChooserPanel playChooserPanel) {
        this.playChooserPanel = playChooserPanel;
    }

    public NextChooserPanel getNextChooserPanel() {
        return nextChooserPanel;
    }

    public void setNextChooserPanel(NextChooserPanel nextChooserPanel) {
        this.nextChooserPanel = nextChooserPanel;
    }

    public void setStopChooserPanel(StopChooserPanel stopChooserPanel) {
        this.stopChooserPanel = stopChooserPanel;
    }

    public StopChooserPanel getStopChooserPanel() {
        return stopChooserPanel;
    }

    public void setPauseChooserPanel(PauseChooserPanel pauseChooserPanel) {
        this.pauseChooserPanel = pauseChooserPanel;
    }

    public PauseChooserPanel getPauseChooserPanel() {
        return pauseChooserPanel;
    }

    public VolumeControlPanel getVolumeControlPanel() {
        return volumeControlPanel;
    }

    public void setVolumeControlPanel(VolumeControlPanel volumeControlPanel) {
        this.volumeControlPanel = volumeControlPanel;
    }

    public MusicPanel getMusicPanel() {
        return musicPanel;
    }

    public void setMusicPanel(MusicPanel musicPanel) {
        this.musicPanel = musicPanel;
    }

}//UIManager
