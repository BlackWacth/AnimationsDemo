package com.hua.animationsdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ScreenSlideActivity extends AppCompatActivity {

    public static final int PAGE_COUNT = 5;

    private ViewPager mViewPager;
    private SlidePagerAdapter mAdapter;

    private int[] imgs = {
            R.drawable.img01,
            R.drawable.img02,
            R.drawable.img03,
            R.drawable.img04,
            R.drawable.img05
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mViewPager = (ViewPager) findViewById(R.id.slide_viewpager);
        initViewPager();
    }

    private void initViewPager() {
        mViewPager.setPageTransformer(true, new ZoomOutpageTransformer());
        mAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
    }


    class SlidePagerAdapter extends FragmentStatePagerAdapter {

        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SlideFragment.create(imgs[position]);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }

    class ZoomOutpageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();
            int pageHeight = page.getHeight();

            if(position < -1) {
                page.setAlpha(0f);
            }else if(position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 -Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;

                if(position < 0) {
                    page.setTranslationX(horzMargin - vertMargin / 2);
                }else {
                    page.setTranslationX(-horzMargin + vertMargin / 2);
                }
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);

                page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            }else {
                page.setAlpha(0);
            }
        }
    }

}
