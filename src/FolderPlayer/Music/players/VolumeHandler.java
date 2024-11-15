package FolderPlayer.Music.players;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

/**
 *　このクラスはインスタンスを持たない
 * 　adjustVolume以外のメソッドを受け付けず、adjustvolumeのみ実行する
 * @author  AC34
 */

class VolumeHandler {
    
/*SourceDataLineを引数にとるvolume*/
    public static void adjustVolume(SourceDataLine line, float percent) {
        FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        adjustVolume(control, percent);//共通メソッドへ移譲
    }
    
/*Clipを引数にとるvolume*/
    public static void adjustVolume(Clip clip, float percent) {
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        adjustVolume(control, percent);//共通メソッドへ移譲
    }

    private static void adjustVolume(FloatControl control, float percent) {

        //マイナスを直す
        float range = control.getMaximum()+Math.abs(control.getMinimum());
        float calc = range*(percent/100);
        calc=calc+control.getMinimum();
        control.setValue(calc);
    }//adjustVolume

}
