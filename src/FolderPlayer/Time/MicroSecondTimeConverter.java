package FolderPlayer.Time;

/**
 * マイクロ秒単位の数値管理と、他のデータ形式の管理
 *
 * @author  AC34
 */
public class MicroSecondTimeConverter {

    private long time;
    //コンストラクタ
    public MicroSecondTimeConverter(long time_in_micro) {
        time = time_in_micro;
    }
    
    public long getTime() {
        return time;
    }

    public void setTime(long time_in_micro) {
        time = time_in_micro;
    }

    /*あまりを省いた最大の分数を返す*/
    public int getMinutesWithin() {
        return (int) getInSec() / 60;
    }

    /*getMinutesWithinのあまりの秒数を返す*/
    public int getTheRestSecsOfMinutesWithin() {
        return (int) getInSec() - (getMinutesWithin() * 60);
    }

    /*マイクロセカンドから秒へ直して返す*/
    public int getInSec() {
        return (int) time / 1000000;
    }

}
