package wellijohn.org.chartlinedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wellijohn.org.linechart.chartline.ChartLine;
import wellijohn.org.linechart.chartline.exception.YCoordinateException;
import wellijohn.org.linechart.chartline.vo.DotVo;

public class MainActivity extends AppCompatActivity {

    //y轴的点
    private double[] mYdots = new double[]{0, 5, 10, 15, 20, 25, 30, 35, 40};
    //x轴的点
    private String[] mXdots = new String[]{"08/18", "08/19", "08/20", "08/21", "08/22", "08/23", "08/24",
            "08/25", "08/26", "08/27", "08/28", "08/29", "09/01", "09/02", "09/23"};
    private ChartLine mChartLine;

    //显示的坐标点
    private List<DotVo> mListDisDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTestData();
        try {
            mChartLine.setYdots(mYdots).setXdots(mXdots).setListDisDots(mListDisDots).reDraw();
        } catch (YCoordinateException e) {
            Log.d("MainActivity", "onCreate: ");
            e.printStackTrace();
        }
    }

    private void initView() {
        mChartLine = (ChartLine) findViewById(R.id.chartLine);
    }

    private void initTestData() {
        mListDisDots = new ArrayList<>();
        DotVo tempDotVo = new DotVo("08/18", 5);
        mListDisDots.add(tempDotVo);
        DotVo tempDotVo1 = new DotVo("08/19", 10);
        mListDisDots.add(tempDotVo1);
        DotVo tempDotVo2 = new DotVo("08/20", 7);
        mListDisDots.add(tempDotVo2);
        DotVo tempDotVo3 = new DotVo("08/21", 15);
        mListDisDots.add(tempDotVo3);
        DotVo tempDotVo4 = new DotVo("08/22", 23);
        mListDisDots.add(tempDotVo4);
        DotVo tempDotVo5 = new DotVo("08/23", 40);
        mListDisDots.add(tempDotVo5);
        DotVo tempDotVo6 = new DotVo("09/02", 0);
        mListDisDots.add(tempDotVo6);

    }
}
