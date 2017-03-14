package cn.ycl.com.viewpagerdiy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cn.ycl.com.viewpagerdiy.viewpager.MyViewPager;

public class MainActivity extends AppCompatActivity {
    private MyPageAdapter adapter;
    private MyViewPager viewPager;
    private int[] arr = {R.drawable.drawable1,R.drawable.drawable2,R.drawable.drawable3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (MyViewPager) findViewById(R.id.mvp);
        adapter = new MyPageAdapter(arr);
        viewPager.setAdapter(adapter);
       //  viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
    }

    private class MyPageAdapter extends PagerAdapter{
        private int[] arr;
        public MyPageAdapter(int[] arr){
            this.arr =arr;
        }
        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(MainActivity.this);
            Picasso.with(MainActivity.this).load(arr[position]).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(imageView);
            viewPager.addChlidView(position,imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer
    {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @SuppressLint("NewApi")
        public void transformPage(View view, float position)
        {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            Log.e("TAG", view + " , " + position + "");

            if (position < -1)
            { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0)
                {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else
                {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                        / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else
            { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
