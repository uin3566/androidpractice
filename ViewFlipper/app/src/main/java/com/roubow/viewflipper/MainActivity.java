package com.roubow.viewflipper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

//将横向响应的控件放入listView，会导致事件响应冲突，需作特殊处理
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HorizontalScrollListView mFlipperListView = (HorizontalScrollListView)findViewById(R.id.lv_view_flipper);
        ViewFlipperAdapter mFlipperAdapter = new ViewFlipperAdapter(this);
        mFlipperListView.setAdapter(mFlipperAdapter);
    }
}
