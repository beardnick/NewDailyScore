package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.newdailyscore.R;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import Date.Hobby;


public class ScoreFragment extends Fragment {
    private LineChartView mLineChartView;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisValueList = new ArrayList<AxisValue>();
    private Calendar mCalendar ;
    private List<Hobby> list;
    private int [][] a = {{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
            {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}};
    private long beginDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score,container,false);
        mLineChartView = (LineChartView) view.findViewById(R.id.score_chart);
        list = DataSupport.findAll(Hobby.class);
        beginDate = getBeginDate();
        getX();
        getScore();
        initLineChart();
        return view;
    }

    private long getBeginDate(){
        long temp = Long.MAX_VALUE;
        Date tempDate;
        for(Hobby hobby:list){
            tempDate = hobby.getBeginCalendar().getTime();
            if(tempDate.getTime() < temp)temp = tempDate.getTime();
        }
        return temp;
    }

    private void getX() {
        mCalendar = Calendar.getInstance();
        Date date = mCalendar.getTime();
        int year = date.getYear();
        int month = date.getMonth();
        if (((year % 4 == 0) || (year % 400 == 0)) && (year % 100 != 0)) {
            for (int i = 0; i < a[1][month - 1]; i++) {
                mAxisValueList.add(new AxisValue(i).setLabel(Integer.valueOf(i + 1).toString()));
            }
        } else {
            for (int i = 0; i < a[0][month - 1]; i++) {
                mAxisValueList.add(new AxisValue(i).setLabel(Integer.valueOf(i + 1).toString()));
            }
        }
    }

    private void getScore(){
        Date date = mCalendar.getTime();
        int year = date.getYear();
        int month = date.getMonth();
        if (((year % 4 == 0) || (year % 400 == 0)) && (year % 100 != 0)) {
            for (int i = 0; i < a[1][month - 1]; i++) {
                date = new Date(year,month,i+1);
                if(date.getTime() < beginDate - 86400000){
                    mPointValues.add(new PointValue(i,0));
                }
                else {
                    mPointValues.add(new PointValue(i+1,getTodayTotalScore(date)));
                }
            }
        } else {
            for (int i = 0; i < a[0][month - 1]; i++) {
                date = new Date(year,month,i+1);
                if(date.getTime() < beginDate - 86400000){
                    mPointValues.add(new PointValue(i,0));
                }
                else {
                    mPointValues.add(new PointValue(i+1,getTodayTotalScore(date)));
                }
            }
        }

    }

    public int getTodayTotalScore(Date date){
        long temp = System.currentTimeMillis();
        long temp1 = date.getTime();
        if(temp1 > temp + 86400000){
            return 0;
        }
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int total = 0;
            for(Hobby hobby:list){
                total += hobby.getScore(calendar);
            }
            return total;
        }
    }

    private void  initLineChart() {
        Line line = new Line(mPointValues).setColor(getResources().getColor(R.color.blue_700));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);
        line.setHasLabels(true);
        line.setHasLines(true);
        lines.add(line);
        line.setHasPoints(false);
        LineChartData lineChartData = new LineChartData();
        lineChartData.setLines(lines);
        Axis axisX = new Axis();
        axisX.setTextColor(getResources().getColor(R.color.blue_700));
        axisX.setTextSize(10);
        axisX.setMaxLabelChars(31);
        lineChartData.setAxisXBottom(axisX);
        Axis axisY = new Axis();
        axisY.setName("分数");
        axisY.setTextColor(getResources().getColor(R.color.blue_700));
        lineChartData.setAxisYLeft(axisY);
        mLineChartView.setInteractive(false);
        mLineChartView.setInteractive(true);
        mLineChartView.setZoomType(ZoomType.HORIZONTAL);
        mLineChartView.setLineChartData(lineChartData);
        mLineChartView.setVisibility(View.VISIBLE);
//            Viewport v = new Viewport(mLineChartView.getMaximumViewport());
//            v.left = 0;
//            v.right = 7;
//           mLineChartView.setCurrentViewport(v);
    }
}
