# LineChart
![image](https://github.com/WelliJohn/LineChart/blob/master/imgs/shitu.gif)

example:
1.初始化数据：
//y轴的点
private double[] mYdots = new double[]{0, 5, 10, 15, 20, 25, 30, 35, 40};
//x轴的点
private String[] mXdots = new String[]{"08/18", "08/19", "08/20", "08/21", "08/22", "08/23", "08/24",
            "08/25", "08/26", "08/27", "08/28", "08/29", "09/01", "09/02", "09/23"};

//在布局中写的ChartLine
<wellijohn.org.linechart.chartline.ChartLine<br> 
        android:id="@+id/chartLine"<br> 
        android:layout_width="wrap_content"<br> 
        android:layout_height="wrap_content"<br> 
        /><br> 
private ChartLine mChartLine;


//显示的坐标点,通过这些坐标点进行连线
private List<DotVo> mListDisDots;

//构造显示的点数据
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

2.设置数据，并显示
try {
    mChartLine.setYdots(mYdots).setXdots(mXdots).setListDisDots(mListDisDots).reDraw();
} catch (YCoordinateException e) {
    e.printStackTrace();
}

3.现在只是写个设计思路，目前由于ui还没出来，可以慢慢定制，等ui好了，会封装成jar上传至jcenter.





