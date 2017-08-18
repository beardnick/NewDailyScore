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
    }

    public void setDefault(){
        score = 0;
        isFinish = false;
    }

    public String getHobbyName() {
        return hobbyName;
    }

    public void setHobbyName(String hobbyName) {
        this.hobbyName = hobbyName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    private void setScore(int score) {
     if(isFinish == false && score == 0){
         this.score = 0;
     }
     else {
         this.score = score;
     }
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
        if(finish)setScore(perScore);
        else  setScore(0);
    }
}
