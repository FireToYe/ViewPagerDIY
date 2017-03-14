package cn.ycl.com.viewpagerdiy.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yechenglong on 2017/3/13.
 */

public class MyViewPager extends ViewPager{
    private View mleft;
    private View mRight;
    private float mTrans;
    private static final float SCALE_MAX = 0.5f;
    private Map<Integer,View> viewMap = new HashMap<>();
    private float mScale;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        float effectoffset =  isSmall(offset)?0:offset;
        Log.i("Position",String.valueOf(position));
        mleft =getChlidView(position);
        mRight = getChlidView(position+1);
        animateStack(mleft,mRight,effectoffset,offsetPixels);
        super.onPageScrolled(position, offset, offsetPixels);
    }

    private boolean isSmall(float positionOffset)
    {
        return Math.abs(positionOffset) < 0.0001;
    }

    private View getChlidView(int position){
        return viewMap.get(position);
    }

    protected void animateStack(View left, View right, float effectOffset,
                                int positionOffsetPixels)
    {
        if (right != null)
        {
            /**
             * 缩小比例 如果手指从右到左的滑动（切换到后一个）：0.0~1.0，即从一半到最大
             * 如果手指从左到右的滑动（切换到前一个）：1.0~0，即从最大到一半
             */
            mScale = (1 - SCALE_MAX) * effectOffset + SCALE_MAX;
            /**
             * x偏移量： 如果手指从右到左的滑动（切换到后一个）：0-720 如果手指从左到右的滑动（切换到前一个）：720-0
             */
            mTrans = -getWidth() - getPageMargin() + positionOffsetPixels;
            ViewHelper.setScaleX(right, mScale);
            ViewHelper.setScaleY(right, mScale);
            ViewHelper.setTranslationX(right, mTrans);
            ViewHelper.setAlpha(right,mScale);
        }
        if (left != null)
        {
            left.bringToFront();
        }
    }

    public void addChlidView(int position,View view){
        viewMap.put(position,view);
    }
}
