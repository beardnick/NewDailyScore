package Date;

import org.litepal.crud.ClusterQuery;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by asus on 2017/8/14.
 */

public class Hobby extends DataSupport {
    private String name;
    private int perScore;
    private Calendar beginCalendar;
    private int totalScore;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
    private boolean isFinish;
    private int score;

    public Hobby(String name,int perScore){
        this.name = name;
        this.perScore = perScore;
        this.beginCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        totalScore = 0;
        isFinish = false;
        score = 0;
        HobbyHelper helper = new HobbyHelper(name,perScore);
        helper.save();
    }


    private String getStringClendar(Calendar calendar){
        return format.format(calendar.getTime());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name != null)
        this.name = name;
    }

    public int getPerScore() {
        return perScore;
    }

    public void setPerScore(int perScore) {
        this.perScore = perScore;
    }

    public Calendar getBeginCalendar() {
        return beginCalendar;
    }

    public int getTotalScore() {
        return totalScore;
    }

    private void setTotalScore(boolean finish){
        if(finish)totalScore += perScore;
        else  totalScore -= perScore;
    }

    public boolean getIsFinish(){
        return isFinish;
    }

    public boolean getIsFinish(Calendar calendar){
        String temp = getStringClendar(calendar);
        HobbyHelper helper = getHobbyHelper(name,temp);
        return helper.isFinish();
    }

    public void setIsFinish(Calendar calendar){
        String temp = getStringClendar(calendar);
        HobbyHelper helper = getHobbyHelper(name,temp);
        helper.setFinish(!helper.isFinish());
        setTotalScore(helper.isFinish());
        setScore(helper.getScore());
    }

    public  void setIsFinish(){
        Calendar calendar = Calendar.getInstance();
        isFinish = ! isFinish;
        HobbyHelper helper = getHobbyHelper(name,getStringClendar(calendar));
        helper.setFinish(isFinish);
        setTotalScore(isFinish);
        setScore(helper.getScore());
    }


    public int getScore(){
        return score;
    }

    public int getScore(Calendar calendar){
        String temp = getStringClendar(calendar);
        HobbyHelper helper = getHobbyHelper(name,temp);
        return helper.getScore();
    }

    private void setScore(int score){
        this.score = score;
    }

    HobbyHelper getHobbyHelper(String name,String date){
        List<HobbyHelper> list = where("name == ?",name).where("date == ?",date).find(HobbyHelper.class);
        return list.get(0);
    }
}
