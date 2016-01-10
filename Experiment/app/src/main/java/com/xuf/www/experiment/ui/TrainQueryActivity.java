package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.xuf.www.experiment.GlobalConfig;
import com.xuf.www.experiment.R;
import com.xuf.www.experiment.bean.TrainNoInfo;
import com.xuf.www.experiment.util.DimenUtil;
import com.xuf.www.experiment.util.HttpUrlConnectionAsync;
import com.xuf.www.experiment.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/28.
 */
public class TrainQueryActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_query);

        _init();
    }

    private void _init(){
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        final PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tab_strip);
        tabStrip.setViewPager(viewPager);
        tabStrip.setTextColorResource(R.color.tab_text_color_unselected);
        tabStrip.setTextSize(DimenUtil.dp2px(this, 18));
        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int mPrePosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                View preView = tabStrip.getChildView(mPrePosition);
                View curView = tabStrip.getChildView(position);
                if (curView instanceof TextView) {
                    if (preView instanceof TextView) {
                        ((TextView) preView).setTextColor(getResources().getColor(R.color.tab_text_color_unselected));
                    }
                    ((TextView) curView).setTextColor(getResources().getColor(R.color.tab_text_color_selected));
                    mPrePosition = position;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        Fragment[] mFragments = {new QueryTrainNoFragment(), new QueryTrainStationFragment()};
        String[] mPageTitles = {"车次查询","起始查询"};

        private MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPageTitles[position];
        }
    }

}



