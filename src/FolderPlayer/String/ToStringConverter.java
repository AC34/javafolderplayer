package FolderPlayer.String;

/**
 *
 * @author  AC34
 */
public class ToStringConverter {
    /**
     * 指定された桁数に満たない数字の先頭に0を追加して
     * @param num
     * @param digits
     * @return
     */
    public static String fillHeadWithZero(int num, int digits){
        String back = Integer.toString(num);
        int fillers = digits - back.length();
        if(fillers <= 0){
            //埋める必要はない
            return back;
        }
        String prefix = "";
        for(int i = 0; i < fillers;i++)prefix += "0";
        return prefix+back;
    }//fillHeadWithZero
}//ToStringConverter