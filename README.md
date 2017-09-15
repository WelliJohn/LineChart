# LineChart
<b>在项目的gradle文件中，增加compile 'wellijohn.org.simplelinechart:linechart:0.0.1'</b><br>
一.线性图表的实现<br>
![image](https://github.com/WelliJohn/LineChart/blob/master/imgs/shitu.gif)

<b>使用example:</b><br>

<b>1.初始化数据：</b><br>
//y轴的点<br>
private double[] mYdots = new double[]{0, 5, 10, 15, 20, 25, 30, 35, 40};<br>
//x轴的点<br>
private String[] mXdots = new String[]{"08/18", "08/19", "08/20", "08/21", "08/22", "08/23",<br>　　　　　　　　 "08/24","08/25", "08/26", "08/27", "08/28", "08/29", "09/01", "09/02", "09/23"};<br>

//在布局中写的ChartLine<br>
<wellijohn.org.linechart.chartline.ChartLine<br>
        　　android:id="@+id/chartLine"<br>
        　　android:layout_width="wrap_content"<br>
        　　android:layout_height="wrap_content"<br>
       　　 /><br>
private ChartLine mChartLine;<br>


//显示的坐标点,通过这些坐标点进行连线<br>
private List<DotVo> mListDisDots;<br>

//构造显示的点数据<br>
private void initTestData() {<br>
　　mListDisDots = new ArrayList<>();<br>
　　DotVo tempDotVo = new DotVo("08/18", 5);<br>
　　mListDisDots.add(tempDotVo);<br>
　　DotVo tempDotVo1 = new DotVo("08/19", 10);<br>
　　mListDisDots.add(tempDotVo1);<br>
　　DotVo tempDotVo2 = new DotVo("08/20", 7);<br>
　　mListDisDots.add(tempDotVo2);<br>
　　DotVo tempDotVo3 = new DotVo("08/21", 15);<br>
　　mListDisDots.add(tempDotVo3);<br>
　　DotVo tempDotVo4 = new DotVo("08/22", 23);<br>
　　mListDisDots.add(tempDotVo4);<br>
　　DotVo tempDotVo5 = new DotVo("08/23", 40);<br>
　　mListDisDots.add(tempDotVo5);<br>
　　DotVo tempDotVo6 = new DotVo("09/02", 0);<br>
　　mListDisDots.add(tempDotVo6);<br>

}

<b>2.设置数据，并显示</b><br>
try {<br>
    　　mChartLine.setYdots(mYdots).setXdots(mXdots).setListDisDots(mListDisDots).reDraw();<br>
} catch (YCoordinateException e) {<br>
    　　e.printStackTrace();<br>
}<br>


二.百分比圆形图表的实现<br>
实现效果图：<br>
![image](https://github.com/WelliJohn/LineChart/blob/master/imgs/percent_chart.png)<br>

<b>使用example:</b><br>

<b>1.初始化数据：</b><br>
//在布局中写的CirclePercentChart<br>
<wellijohn.org.simplelinechart.circlepercentchart.CirclePercentChart<br>
          　　android:id="@+id/circle_percent_chart"<br>
        　　  android:layout_width="300dp"<br>
         　　 android:layout_height="300dp"<br>
        　　  android:layout_centerInParent="true" /><br>
private CirclePercentChart mCirclePercentChart;<br>

构造数据：
  private void initTestData() {
        　　mListDisArcs = new ArrayList<>();
      　　  ArcVo tempDotVo = new ArcVo(Color.RED, .42f);
       　　 mListDisArcs.add(tempDotVo);
      　　  ArcVo tempDotVo1 = new ArcVo(Color.YELLOW, .38f);
      　　  mListDisArcs.add(tempDotVo1);
       　　 ArcVo tempDotVo2 = new ArcVo(Color.GREEN, .09f);
       　　 mListDisArcs.add(tempDotVo2);
      　　  ArcVo tempDotVo3 = new ArcVo(Color.BLACK, .05f);
     　　   mListDisArcs.add(tempDotVo3);
   　　     ArcVo tempDotVo4 = new ArcVo(Color.BLUE, .06f);
       　　 mListDisArcs.add(tempDotVo4);
    }

<b>2.设置数据，并显示</b><br>
 try {<br>
        　　    mCirclePercentChart.setDisArcList(mListDisArcs).reDraw();<br>
        } catch (PercentOverFlowException e) {<br>
      　　      e.printStackTrace();<br>
        }<br>

