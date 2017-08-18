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
    private Calendar todayCalendar;
    private int todayScore;
    private boolean todayIsFinish;

    public Hobby(String name,int perScore){
        this.name = name;
        this.perScore = perScore;
        this.beginCalendar = Calendar.getInstance();
        this.todayCalendar = Calendar.getInstance();
        todayIsFinish = false;
        totalScore = 0;
        HobbyHelper helper = new HobbyHelper(name,perScore);
        helper.setDefault();
        if(name != null)helper.save();
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
        Calendar calendar = Calendar.getInstance();
        if(getStringClendar(todayCalendar).equals(getStringClendar(calendar)))return getTodayIsFinish();
        else {
            setYesterdayHelper(name,todayCalendar);
            todayCalendar = calendar;
            HobbyHelper helper = getHobbyHelper(name,getStringClendar(calendar));
            helper.setDefault();
            todayScore = helper.getScore();
            todayIsFinish = helper.isFinish();
            helper.save();
            return helper.isFinish();
        }
    }

    private void setYesterdayHelper(String name,Calendar calendar){
        HobbyHelper helper;
        helper = getHobbyHelper(name,getStringClendar(calendar));
        helper.setFinish(todayIsFinish);
        if(helper.isSaved())helper.save();
    }

    boolean getTodayIsFinish(){
        return todayIsFinish;
    }

    public boolean getIsFinish(Calendar calendar){
        if(inTheTimeZone(calendar)){
            if(getStringClendar(todayCalendar).equals(getStringClendar(calendar)))return getTodayIsFinish();
            else {
                HobbyHelper helper = getHobbyHelper(name,getStringClendar(calendar));
                return helper.isFinish();
            }
        }
        else
            return false;
    }
    public void setIsFinish(Calendar calendar){
        if(inTheTimeZone(calendar)){
            HobbyHelper helper = getHobbyHelper(name,getStringClendar(calendar));
            helper.setFinish(!helper.isFinish());
            if(helper.isSaved())helper.save();
            setTotalScore(helper.isFinish());
        }
    }

    public  void setIsFinish(){
        Calendar calendar = Calendar.getInstance();
        if(getStringClendar(todayCalendar).equals(getStringClendar(calendar))){
            todayIsFinish = ! todayIsFinish;
            if(todayIsFinish)todayScore = perScore;
            else todayScore = 0;
            setTotalScore(todayIsFinish);
        }
        else {
            boolean isFinish = !getIsFinish();
            HobbyHelper helper = getHobbyHelper(name,getStringClendar(calendar));
            helper.setFinish(isFinish);
            if(helper.isSaved())helper.save();
            setTotalScore(isFinish);
        }
    }


    public int getScore(){
        Calendar calendar = Calendar.getInstance();
        if(getStringClendar(todayCalendar).equals(getStringClendar(calendar)))return getTodayScore();
        else {
            setYesterdayHelper(name,todayCalendar);
            todayCalendar = calendar;
            HobbyHelper helper = getHobbyHelper(name,getStringClendar(calendar));
            helper.setDefault();
            todayScore = helper.getScore();
            todayIsFinish = helper.isFinish();
            helper.save();
            return helper.getScore();
        }
    }

    private int getTodayScore(){
        return todayScore;
    }

    public int getScore(Calendar calendar){
        if(inTheTimeZone(calendar)){
                String test = getStringClendar(todayCalendar);
            String test1 = getStringClendar(calendar);
            if(getStringClendar(todayCalendar).equals(getStringClendar(calendar )))return getTodayScore();
            else {
                HobbyHelper helper = getHobbyHelper(name,getStringClendar(calendar));
                return helper.getScore();
            }
        }
        else return 0;
    }


    HobbyHelper getHobbyHelper(String name,String date){
        HobbyHelper helper = new HobbyHelper(name,perScore);
        List<HobbyHelper> list = where("hobbyName like ? and Date like ?",
                "%"+name+"%","%"+date+"%").find(HobbyHelper.class);
        for(HobbyHelper helper1:list){
            String test1 = helper1.getHobbyName();
            String test2 = helper1.getDate();
            int test3 = helper1.getScore();
            int test4 = test3;
        }
        if(list.size() == 0){
            helper.setDefault();
            return helper;
        } else
            return list.get(0);
    }

    public boolean inTheTimeZone(Calendar calendar){
        return calendar.getTimeInMillis() > beginCalendar.getTimeInMillis() - 86400000
                && calendar.getTimeInMillis() < System.currentTimeMillis() + 86400000;
    }
}
