package com.xuf.www.experiment.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.ipc.BinderPoolActivity;
import com.xuf.www.experiment.ipc.MessengerActivity;
import com.xuf.www.experiment.ipc.BookManagerActivity;
import com.xuf.www.experiment.ipc.ProviderActivity;
import com.xuf.www.experiment.mvp.MVPLoginActivity;
import com.xuf.www.experiment.util.ActivityManager;
import com.xuf.www.experiment.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().createAc(this);
        _setList();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mList);
        setListAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().destroyAc(this);
    }

    private void _setList(){
        //在这里添加item和测试activity
        mList.add("HeadFooterRecyclerActivity");
        ActivityManager.add("HeadFooterRecyclerActivity", HeadFooterRecyclerActivity.class);

        mList.add("GlideActivity1");
        ActivityManager.add("GlideActivity1", GlideActivity1.class);

        mList.add("UINestStackOverflowActivity");
        ActivityManager.add("UINestStackOverflowActivity", UINestStackOverflowActivity.class);

        mList.add("WebviewActivity");
        ActivityManager.add("WebviewActivity", WebviewActivity.class);

        mList.add("LocalServiceActivity");
        ActivityManager.add("LocalServiceActivity", LocalServiceActivity.class);

        mList.add("GestureDetectorActivity");
        ActivityManager.add("GestureDetectorActivity", GestureDetectorActivity.class);

        mList.add("RecyclerViewActivity");
        ActivityManager.add("RecyclerViewActivity", SimpleRecyclerViewActivity.class);

        mList.add("MVPStructure");
        ActivityManager.add("MVPStructure", MVPLoginActivity.class);

        mList.add("AnimatorActivity");
        ActivityManager.add("AnimatorActivity", AnimatorActivity.class);

        mList.add("ActivityTaskActivity");
        ActivityManager.add("ActivityTaskActivity", ActivityTaskActivity.class);

        mList.add("View");
        ActivityManager.add("View", ViewActivity.class);

        mList.add("CommonAdapter");
        ActivityManager.add("CommonAdapter", CommonAdapterActivity.class);

        mList.add("ImageLoader");
        ActivityManager.add("ImageLoader", ImageLoaderActivity.class);

        mList.add("ImageSelect");
        ActivityManager.add("ImageSelect", ImageSelectActivity.class);

        mList.add("ScrollTabViewPager");
        ActivityManager.add("ScrollTabViewPager", ScrollTabViewPagerActivity.class);

        mList.add("TrainQueryActivity");
        ActivityManager.add("TrainQueryActivity", TrainQueryActivity.class);

        mList.add("MessengerActivity");
        ActivityManager.add("MessengerActivity", MessengerActivity.class);

        mList.add("BookManagerActivity");
        ActivityManager.add("BookManagerActivity", BookManagerActivity.class);

        mList.add("ProviderActivity");
        ActivityManager.add("ProviderActivity", ProviderActivity.class);

        mList.add("BinderPoolActivity");
        ActivityManager.add("BinderPoolActivity", BinderPoolActivity.class);

        mList.add("DragViewActivity");
        ActivityManager.add("DragViewActivity", DragViewActivity.class);

        mList.add("FloatWidgetActivity");
        ActivityManager.add("FloatWidgetActivity", FloatWidgetActivity.class);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String item = mList.get(position);
        Intent intent = new Intent(MainActivity.this, ActivityManager.get(item));
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
