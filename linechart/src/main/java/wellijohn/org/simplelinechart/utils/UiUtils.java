package wellijohn.org.simplelinechart.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author: JiangWeiwei
 * @time: 2017/9/6-14:31
 * @email:
 * @desc:
 */
public class UiUtils {
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * dip转换px
     */
    public static int dip2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * @param paramPaint 画笔
     * @return 画笔的宽度
     */
    public static float getPaintStrokeWidth(Paint paramPaint) {
        return paramPaint.getStrokeWidth();
    }

    /**
     * @param paramText  显示的文本
     * @param paramPaint 画笔
     * @return 文本的宽度
     */
    public static float getTextWidth(String paramText, Paint paramPaint) {
        return paramPaint.measureText(paramText);
    }

    /**
     * @param paramText  显示的文本
     * @param paramPaint 画笔
     * @return 文本的高度
     */
    public static float getTextHeight(String paramText, Paint paramPaint) {
        Rect rect = new Rect();
        paramPaint.getTextBounds(paramText, 0, 1, rect);
        return rect.height();
    }
}
