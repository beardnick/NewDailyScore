package com.example.asus.newdailyscore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import Date.Hobby;

/**
 * Created by asus on 2017/8/16.
 */

public class HistoryScore extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private Calendar beginDate;
    private Calendar todayDate;
    private Hobby hobby;
    private SimpleDateFormat format;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        ActionBar actionBar = getSupportActionBar();
        initDate();
        actionBar.setTitle(hobby.getName());
        initCalendar();
    }

    public void initDate(){
        calendarView = (MaterialCalendarView) findViewById(R.id.history_calendar);
        format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Intent intent =  getIntent();
       List<Hobby> hobbyList = DataSupport.where("name == ?",
               intent.getStringExtra("name")).find(Hobby.class);
        hobby = hobbyList.get(0);
        beginDate = Calendar.getInstance();
            beginDate = hobby.getBeginCalendar();
            todayDate = Calendar.getInstance();
    }

    public void initCalendar(){
        String tempTodaDate = format.format(todayDate.getTime());
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        calendarView.setBackgroundColor(getResources().getColor(R.color.white_a0));
        String tempBeginDate;
        for(;;beginDate.add(Calendar.DATE,1)){
            tempBeginDate = format.format(beginDate.getTime());
            if(hobby.getIsFinish(beginDate))
                calendarView.setDateSelected(beginDate,true);
            if(tempBeginDate.equals(tempTodaDate)) break;
        }
        calendarView.addDecorator(new EventDecorator(calendarView.getSelectedDates()));
    }

    public class EventDecorator implements DayViewDecorator {

        private final HashSet<CalendarDay> dates;

        public EventDecorator(Collection<CalendarDay> dates) {
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            if(hobby.getPerScore() > 0)
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_amber_700));
            else
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_black_ae));
        }
    }

}
