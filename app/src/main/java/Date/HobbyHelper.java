package Date;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by asus on 2017/8/16.
 */

public class HobbyHelper extends DataSupport {
    private String hobbyName;
    private String date;
    private int score;
    private boolean isFinish;
    private int perScore;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);


    public HobbyHelper(String hobbyName,int perScore){
        this.hobbyName = hobbyName;
        this.perScore = perScore;
        Calendar calendar = Calendar.getInstance();
        date = format.format(calendar.getTime());
        score = 0;
        isFinish = false;
    }

    public String getHobbyName() {
        return hobbyName;
    }

    public void setHobbyName(String hobbyName) {
        this.hobbyName = hobbyName;
        save();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        save();
    }

    public int getScore() {
        return score;
    }

    private void setScore(int score) {
        this.score = score;
        save();
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
        if(finish)setScore(perScore);
        else  setScore(0);
        save();
    }

    HobbyHelper getHobbyHelper(String name,String date){
        List<HobbyHelper>  list = where("name == ?",name).where("date == ?",date).find(HobbyHelper.class);
        return list.get(0);
    }
}
