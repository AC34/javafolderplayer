package FolderPlayer.ui.MainPanelComponents;

import FolderPlayer.managers.ColorManager;
import FolderPlayer.managers.GeneralManager;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author  AC34
 */
public class StatusPanel extends JPanel{
    private GeneralManager gm;
    private StatusLabel sl;
    
    public StatusPanel(GeneralManager general_manager,int width,int height){
        //gmへの登録
        gm = general_manager;
        gm.getUiManager().setStatusPanel(this);
        
        //パネルの設定
        setBackground(gm.getColorManager().getColor(ColorManager.STATUS_PANEL_BACKGROUND));
        setBounds(0,0,width,height);
        setLayout(null);
        
        //ラベルの設定
        //初期化と初期メッセージ配置
        sl = new StatusLabel(gm.getTextManager().getText(1),width,height);
         //左端からのインデントを設定(デザインとして)
         sl.indentByLeftPosition(6);
        //搭載
        add(sl);
    }
    
    public void updateMessage(String message){
        sl.updateMessage(message);
    }
    /*このラベルはStatusPanelからのみアクセスする*/
    private class StatusLabel extends JLabel{
        public StatusLabel(String initial_message,int width,int height){

            //初期化設定
            setText(initial_message);
            setBounds(0,0,width,height);
            setBackground(Color.blue);
            setForeground(Color.white);
            
        }//StatusLabel
        
        public void updateMessage(String message){
            setText(message);
        }//updateMessage
        public String getCurrentMessage(){
            return getText();
        }//getCurrentMessage
        /*ラベルの左端からの距離を設定する。
        widthは設定した値分短くなり、
        右端の位置は常に変わらない*/
        public void indentByLeftPosition(int left_pos){
            //左からの距離の値が現在のwidthより大きい場合処理をしない
            if(left_pos<getWidth()){
                setBounds(left_pos,getY(),getWidth()-left_pos,getHeight());
                repaint();
            }
        }//indentbyLeftPosition
    }//StatusLabel
    
}//StatusPanel
