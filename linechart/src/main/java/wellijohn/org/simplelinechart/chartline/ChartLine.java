package wellijohn.org.simplelinechart.chartline;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wellijohn.org.simplelinechart.R;
import wellijohn.org.simplelinechart.chartline.exception.YCoordinateException;
import wellijohn.org.simplelinechart.chartline.vo.DotVo;
import wellijohn.org.simplelinechart.utils.UiUtils;

/**
 * @author: JiangWeiwei
 * @time: 2017/9/6-11:55
 * @email:
 * @desc: 图标的数据
 */
public class ChartLine extends View {
    private static final String TAG = "ChartLine";
    //y轴的点
    private double[] mYdots;
    //x轴的点
    private String[] mXdots;

    //屏幕的宽高
    private double mScreenWidth;
    private double mScreenHeight;

    //y轴上的间隔距离
    private double mYinterval;
    //x轴上的间隔距离
    private double mXinterval;

    private int mXvisibleNum = 7;
    private int mYvisibleNum = 6;
    //x轴默认显示最大为7个
    private static final int mDefXMaxNum = 7;

    //画x轴横线的画笔
    private Paint mXlinePaint;

    //y轴的文字的画笔
    private Paint mYNumPaint;

    private Canvas mYNumCanvas;

    private float mScrollPosX;
    private Bitmap mBitmap;

    //总共在x轴滑动的距离
    private Float mTotalScrollX = 0f;

    //显示的坐标点
    private List<DotVo> mListDisDots;

    //连线的画笔
    private Paint mLinePaint;

    //y轴的点对应的x的位置
    private Map<String, Float> mYDotMaps;
    //判断数据是否有初始化
    private boolean mIsInitDataSuc = false;

    private Path mLinePath;

    private float mLineLength;

    private float[] mCurrentPosition = new float[2];
    private PathMeasure mPathMeasure;

    //绘制的距离
    private float mDistance = -1;

    private float mFirstXPos = 0;
    private float mFirstYPos = 0;

    private final Path mLineDrawPath = new Path();

    //判断是否绘制结束
    private boolean isDrawOver;


    private OverScroller mScroller;
    private final GestureDetector mGestureDetector =
            new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                        float distanceX, float distanceY) {
                    // Note 0 as the x-distance to prevent horizontal scrolling
//                    scrollBy((int) distanceX, 0);
                    if (mYNumCanvas != null) {
                        mScrollPosX = -distanceX;
                        Log.d(TAG, "onScroll: " + mTotalScrollX + "滑动的距离：" + mScrollPosX);
                        //表示手向左移动
                        synchronized (this) {
                            if (mScrollPosX < 0) {
                                //代表已经在初始化位置还要向左移动
                                if (mTotalScrollX > 0) {
                                    float lastScorllX = mTotalScrollX;
                                    if (lastScorllX + mScrollPosX < 0) {
                                        mTotalScrollX = 0f;
                                        mScrollPosX = mTotalScrollX - lastScorllX;
                                        invalidate();
                                    }

                                } else {
                                    mTotalScrollX += mScrollPosX;
                                    invalidate();
                                }
                            } else if (mScrollPosX > 0) {
                                //代表已经在初始化位置还要向右移动
                                if (mTotalScrollX < 0) {
                                    float lastScorllX = mTotalScrollX;
                                    if (lastScorllX + mScrollPosX > 0) {
                                        mTotalScrollX = 0f;
                                        mScrollPosX = mTotalScrollX - lastScorllX;
                                        invalidate();
                                    } else {
                                        mTotalScrollX += mScrollPosX;
                                        invalidate();
                                    }
                                }
                            }

                        }
                    }
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2,
                                       float velocityX, float velocityY) {
//                    final int maxScrollX = 0;
//
//                    // wholeViewHeight is height of everything that is drawn
//                    int wholeViewHeight =  calculateWholeHeight();
//                    int visibleHeight = getHeight();
//                    final int maxScrollY = wholeViewHeight - visibleHeight;
//
//                    mScroller.forceFinished(true);
//
//                    mScroller.fling(0, // No startX as there is no horizontal scrolling
//                            getScrollY(),
//                            0, // No velocityX as there is no horizontal scrolling
//                            - (int) velocityY,
//                            0,
//                            maxScrollX,
//                            0,
//                            maxScrollY);

//                    invalidate();

                    return true;
                }

                @Override
                public boolean onDown(MotionEvent e) {
                    if (!mScroller.isFinished()) {
                        mScroller.forceFinished(true);
                    }
                    return true;
                }
            });


    public ChartLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


    private void initPaint() {
        mXlinePaint = new Paint();
        mXlinePaint.setStrokeWidth(UiUtils.dip2px(getContext(), 0.1f));
        mXlinePaint.setStyle(Paint.Style.STROKE);
        mXlinePaint.setColor(ContextCompat.getColor(getContext(), R.color.app_red));
        mXlinePaint.setAntiAlias(true);

        mYNumPaint = new Paint();
        mYNumPaint.setStrokeWidth(UiUtils.dip2px(getContext(), 0.03f));
        mYNumPaint.setStyle(Paint.Style.STROKE);
        mYNumPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
        mYNumPaint.setAntiAlias(true);
        mYNumPaint.setTextSize(UiUtils.dip2px(getContext(), 10));

        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(UiUtils.dip2px(getContext(), 0.5f));
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.blue));
        mLinePaint.setAntiAlias(true);


    }


    //设置y轴的坐标的显示
    public ChartLine setYdots(double[] paramYdots) {
        this.mYdots = paramYdots;
        return this;
    }

    //设置x点的坐标的显示
    public ChartLine setXdots(String[] paramXdots) {
        this.mXdots = paramXdots;
        return this;
    }

    //设置显示的坐标点会自动连线起来
    public ChartLine setListDisDots(List<DotVo> paramListDisDots) {
        this.mListDisDots = paramListDisDots;
        return this;
    }

    private void initData() throws YCoordinateException {
        if (!isCanDraw()) return;
        for (DotVo temp : mListDisDots) {
            if (!Arrays.asList(mXdots).contains(temp.getX())) {
                Log.e(TAG, "please check if you y dot exists in mYdots,you must set Y dot in Y coordinates");
                throw new YCoordinateException("please check if you y dot exists in mYdots,you must set Y dot in Y coordinates");
            }
        }

        mScroller = new OverScroller(getContext(), new FastOutLinearInInterpolator());
        mScreenHeight = UiUtils.getScreenHeight(getContext());
        mScreenWidth = UiUtils.getScreenWidth(getContext());
        mXvisibleNum = mXdots.length > mDefXMaxNum ? mDefXMaxNum : mXdots.length;
        mYvisibleNum = mYdots.length - 1;
        mXinterval = (mScreenWidth - getLeft() - getYMaxTextWidth()) / mXvisibleNum;
        mYinterval = 80;
        mYDotMaps = new HashMap<>();
        for (int x = 0, size = mXdots.length; x <= size; x++) {
            if (x >= 1) {
                String tempText = mXdots[x - 1];
                mYDotMaps.put(tempText, (float) (mXinterval * x));
            }
        }
        mIsInitDataSuc = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isCanDraw() || !mIsInitDataSuc) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int widthParentMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthParentMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightParentMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightParentMeasureSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultWidthSize = 0;
        int resultHeightSize = 0;
        int resultWidthMode = MeasureSpec.EXACTLY;//用来对childView进行计算的
        int resultHeightMode = MeasureSpec.EXACTLY;
        int paddingWidth = getPaddingLeft() + getPaddingRight();
        int paddingHeight = getPaddingTop() + getPaddingBottom();
        ViewGroup.LayoutParams thisLp = getLayoutParams();
        switch (widthParentMeasureMode) {
            //父类不加限制给子类
            case MeasureSpec.UNSPECIFIED:
                //这个代表在布局写死了宽度
                if (thisLp.width > 0) {
                    resultWidthSize = thisLp.width;
                    resultWidthMode = MeasureSpec.EXACTLY;
                } else {
                    resultWidthSize = (int) (getYMaxTextWidth() + mXinterval * mXdots.length);
                    resultWidthMode = MeasureSpec.UNSPECIFIED;
                }
                break;
            case MeasureSpec.AT_MOST:
                //这个代表在布局写死了宽度
                if (thisLp.width > 0) {
                    resultWidthSize = thisLp.width;
                    resultWidthMode = MeasureSpec.EXACTLY;
                } else if (thisLp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    resultWidthSize = Math.max(0, widthParentMeasureSize - paddingWidth);
                    resultWidthMode = MeasureSpec.AT_MOST;
                } else if (thisLp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    resultWidthSize = (int) (getYMaxTextWidth() + mXinterval * mXdots.length);
                    resultWidthMode = MeasureSpec.AT_MOST;
                }
                break;
            case MeasureSpec.EXACTLY:
                //这个代表在布局写死了宽度
                if (thisLp.width > 0) {
                    resultWidthSize = Math.min(widthParentMeasureSize, thisLp.width);
                    resultWidthMode = MeasureSpec.EXACTLY;
                } else if (thisLp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    resultWidthSize = widthParentMeasureSize;
                    resultWidthMode = MeasureSpec.EXACTLY;
                } else if (thisLp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    resultWidthSize = (int) (getYMaxTextWidth() + mXinterval * mXdots.length);
                    resultWidthMode = MeasureSpec.AT_MOST;
                }
                break;
        }


        switch (heightParentMeasureMode) {
            //父view不加限制
            case MeasureSpec.UNSPECIFIED:
                //这个代表在布局写死了宽度
                if (thisLp.height > 0) {
                    resultHeightSize = thisLp.height;
                    resultHeightMode = MeasureSpec.EXACTLY;
                } else {
                    resultHeightSize = (int) (getYMaxTextHeight() + mYvisibleNum * mYinterval + getXMaxTextHeight());
                    resultHeightMode = MeasureSpec.UNSPECIFIED;
                }
                break;
            case MeasureSpec.AT_MOST:
                if (thisLp.height > 0) {
                    resultHeightSize = heightParentMeasureSize;
                    resultHeightMode = MeasureSpec.EXACTLY;
                } else if (thisLp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    resultHeightSize = Math.max(0, heightParentMeasureSize - paddingHeight);
                    resultHeightMode = MeasureSpec.AT_MOST;
                } else if (thisLp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    resultHeightSize = (int) (getYMaxTextHeight() + mYvisibleNum * mYinterval + getXMaxTextHeight());
                    resultHeightMode = MeasureSpec.UNSPECIFIED;
                }
                break;
            case MeasureSpec.EXACTLY:
                //这个代表在布局写死了宽度
                if (thisLp.height > 0) {
                    resultHeightSize = Math.min(heightParentMeasureSize, getMeasuredWidth());
                    resultHeightMode = MeasureSpec.EXACTLY;
                } else if (thisLp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    resultHeightSize = heightParentMeasureSize;
                    resultHeightMode = MeasureSpec.EXACTLY;
                } else if (thisLp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    resultHeightSize = (int) (getYMaxTextHeight() + mYvisibleNum * mYinterval + getXMaxTextHeight());
                    resultHeightMode = MeasureSpec.AT_MOST;
                }
                break;
        }

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(resultWidthSize, resultWidthMode),
                MeasureSpec.makeMeasureSpec(resultHeightSize, resultHeightMode));

    }

    /**
     * @return 每个数字代表的间隔，默认是mYdots是最大的高度
     */
    private float getIntervalPerInch() {
        return (float) (mYvisibleNum * mYinterval / mYdots[mYdots.length - 1]);
    }


    /**
     * @return y轴文字的最大宽度
     */
    private float getYMaxTextWidth() {
        float maxWidth = 0;
        for (double y : mYdots) {
            if (mYNumPaint.measureText(String.valueOf(y)) > maxWidth) {
                maxWidth = mYNumPaint.measureText(String.valueOf(y));
            }
        }
        return maxWidth;
    }

    /**
     * @return y轴文字的最大高度
     */
    private float getYMaxTextHeight() {
        Rect rect = new Rect();
        mYNumPaint.getTextBounds(String.valueOf(mYdots[0]), 0, 1, rect);
        return rect.height();
    }


    /**
     * @return x轴文字的最大高度
     */
    private float getXMaxTextHeight() {
        Rect rect = new Rect();
        mYNumPaint.getTextBounds(mXdots[0], 0, 1, rect);
        return rect.height();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (!isCanDraw() || !mIsInitDataSuc) return;
        float tempTableLeftPadding = getYMaxTextWidth();
        if (mBitmap == null || mYNumCanvas == null) {
            mBitmap = Bitmap.createBitmap((int) (getMeasuredWidth() - getYMaxTextWidth()), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            mYNumCanvas = new Canvas(mBitmap);
        }

        mYNumCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mYNumCanvas.translate(mScrollPosX, 0);


        //绘制横线
        for (int y = 0, size = mYdots.length; y < size; y++) {
            String tempText = String.valueOf(mYdots[mYdots.length - 1 - y]);
            mYNumCanvas.drawLine(0, (float) (mYinterval * y), (float) (mXdots.length * mXinterval), (float) (mYinterval * y), mXlinePaint);
            canvas.drawText(tempText, getYMaxTextWidth() - mYNumPaint.measureText(tempText), getYMaxTextHeight() + (float) (mYinterval * y), mYNumPaint);
//            Bitmap tempBitamp = Bitmap.createBitmap((int) (getYMaxTextWidth()), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//            Canvas tempCanvas = new Canvas(tempBitamp);
//            tempCanvas.drawText(tempText, getYMaxTextWidth() - mYNumPaint.measureText(tempText), getYMaxTextHeight() + (float) (mYinterval * y), mYNumPaint);
//            canvas.drawBitmap(tempBitamp, 0, 0, null);
        }


        //绘制竖线
        for (int x = 0, size = mXdots.length; x <= size; x++) {
            mYNumCanvas.drawLine((float) (mXinterval * x), 0, (float) (mXinterval * x), (float) (mYinterval * mYvisibleNum), mXlinePaint);
            if (x >= 1) {
                String tempText = mXdots[x - 1];
                mYNumCanvas.drawText(tempText, (float) (mXinterval * x) - mYNumPaint.measureText(tempText) / 2, (float) (mYvisibleNum * mYinterval + getYMaxTextHeight()), mYNumPaint);
//                mYDotMaps.put(tempText, (float) (mXinterval * x));
            }
        }

        mYNumCanvas.drawPath(mLineDrawPath, mLinePaint);
//        mYNumCanvas.drawLine(mFirstXPos, mFirstYPos, mCurrentPosition[0], mCurrentPosition[1], mLinePaint);

        canvas.drawBitmap(mBitmap, tempTableLeftPadding, getYMaxTextHeight() / 2, null);
    }


    public void initPath() {
        mLinePath = new Path();
        if (mListDisDots != null) {
            for (int i = 0; i < mListDisDots.size() - 1; i++) {
                DotVo tempDotVo = mListDisDots.get(i);
                DotVo nextDotVo = mListDisDots.get(i + 1);
                float startX = mYDotMaps.get(tempDotVo.getX()) == null ? 0 : mYDotMaps.get(tempDotVo.getX());
                float startY = (float) ((mYdots[mYdots.length - 1] - tempDotVo.getY()) * getIntervalPerInch());
                float stopX = mYDotMaps.get(nextDotVo.getX()) == null ? 0 : mYDotMaps.get(nextDotVo.getX());
                float stopY = (float) ((mYdots[mYdots.length - 1] - nextDotVo.getY()) * getIntervalPerInch());
                Log.d(TAG, "第: " + i + "个坐标点" + "startX:" + startX + "  startY:" + startY + "  stopX:" + stopX + "  stopY:" + stopY);
//                mYNumCanvas.drawLine(startX, startY, stopX, stopY, mLinePaint);
                if (i == 0) {
                    mFirstXPos = startX;
                    mFirstYPos = startY;
                    mLinePath.moveTo(startX, startY);
                    mLineDrawPath.moveTo(startX, startY);
                }

                mLinePath.lineTo(stopX, stopY);
            }
        }
        mPathMeasure = new PathMeasure(mLinePath, false);
        mLineLength = mPathMeasure.getLength();
    }

    /**
     * 给外部进行调用的重新绘制的方法。
     */
    public void reDraw() throws YCoordinateException {
        initData();
        initPath();
//        requestLayout();
        startPathAnim(3000);
    }

    // 开启路径动画
    private void startPathAnim(long duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mLineLength);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                mLineDrawPath.lineTo(mCurrentPosition[0], mCurrentPosition[1]);
                invalidate();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isDrawOver = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isDrawOver)
            return mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * @return 是否需要绘制图表
     */
    private boolean isCanDraw() {
        return !(mYdots == null || mYdots.length == 0 || mXdots == null || mXdots.length == 0 || mListDisDots == null);
    }
}
