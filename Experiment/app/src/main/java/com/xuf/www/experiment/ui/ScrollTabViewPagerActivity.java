package com.xuf.www.experiment.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.xuf.www.experiment.R;
import com.xuf.www.experiment.util.DimenUtil;
import com.xuf.www.experiment.util.ToastUtil;

/**
 * Created by Administrator on 2015/10/9.
 */
public class ScrollTabViewPagerActivity extends FragmentActivity{

    private static final String[] CONTENT = new String[]{"头条", "娱乐", "热点", "体育", "广州",  "财经", "科技"
            , "段子", "图片", "直播", "汽车", "时尚", "轻松一刻", "游戏"};

    private ViewPager mViewPager;
    private TextView mPopupTextView;
    private ImageView mExpandImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_tab_view_pager);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(adapter);

        final PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tab_strip);
        tabStrip.setViewPager(mViewPager);
        tabStrip.setTextColorResource(R.color.tab_text_color_unselected);
        tabStrip.setTextSize(DimenUtil.dp2px(this, 15));
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
        tabStrip.setOnScrollToBottomListener(new TabScrollToBottom());

        mPopupTextView = (TextView)findViewById(R.id.tv_popup);
        mExpandImageView = (ImageView)findViewById(R.id.iv_expand);

        final FrameLayout expandView = (FrameLayout)findViewById(R.id.fl_expand_container);
        expandView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //popup
                if (mPopupTextView.isShown()){
                    _setViewVisibleAnim(mPopupTextView, false);
                    _updateExpandView(false);
                } else {
                    _setViewVisibleAnim(mPopupTextView, true);
                    _updateExpandView(true);
                }
            }
        });
    }

    private void _updateExpandView(boolean show){
        if (show){
            mExpandImageView.setImageResource(R.mipmap.ic_expand_up);
        } else {
            mExpandImageView.setImageResource(R.mipmap.ic_expand_down);
        }
    }

    private void _setViewVisibleAnim(View view, boolean isIn){
        Animation animation;
        if (isIn){
            view.setVisibility(View.VISIBLE);
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
                    , Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0);
        } else {
            view.setVisibility(View.INVISIBLE);
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
                    , Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f);
        }
        animation.setDuration(300);
        view.setAnimation(animation);
        animation.startNow();
    }

    private class TabScrollToBottom implements PagerSlidingTabStrip.ScrollToBottomListener{
        @Override
        public void onScrollToBottom() {
            if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1) {
                ToastUtil.showShort(ScrollTabViewPagerActivity.this, "bottom");
            }
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter{
        private MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return SimpleFragment.getInstance(CONTENT[i % CONTENT.length]);
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }
    }
}
