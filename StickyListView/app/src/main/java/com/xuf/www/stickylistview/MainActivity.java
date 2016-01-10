package com.xuf.www.stickylistview;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MainActivity extends Activity implements AbsListView.OnScrollListener{
    private List<String> list = null;
    private List<String> groupKey = new ArrayList<>();
    private List<String> aList = new ArrayList<>();
    private List<String> bList = new ArrayList<>();
    private List<String> cList = new ArrayList<>();
    private ListView listview;
    private View mListHeader;
    private List<Integer> mHeaderPositions = new ArrayList<>();
    private LinearLayout mStickLayout;

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStickLayout = (LinearLayout)findViewById(R.id.ll_stick);
        listview = (ListView) findViewById(R.id.listView_list);
        initData();

        mListHeader = LayoutInflater.from(this).inflate(R.layout.list_header, null);
        listview.addHeaderView(LayoutInflater.from(this).inflate(R.layout.empty_header, null));
        listview.addHeaderView(mListHeader);

        MyAdapter adapter = new MyAdapter();
        listview.setAdapter(adapter);

        listview.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState){
            case SCROLL_STATE_IDLE:
                break;
            case SCROLL_STATE_FLING:
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d(TAG, "firstVisibleItem--->" + firstVisibleItem);

        if (firstVisibleItem >= (mHeaderPositions.get(0) - 1) && firstVisibleItem < (mHeaderPositions.get(1) - 1)){
            mStickLayout.setVisibility(View.VISIBLE);
            TextView textView = (TextView)mStickLayout.findViewById(R.id.tv_header_after);
            textView.setVisibility(View.VISIBLE);
            textView.setText("A组");
        }
        else if (firstVisibleItem >= (mHeaderPositions.get(1) - 1) && firstVisibleItem < (mHeaderPositions.get(2) - 1)){
            TextView textView = (TextView)mStickLayout.findViewById(R.id.tv_header_after);
            textView.setVisibility(View.VISIBLE);
            textView.setText("B组");
        }
        else if (firstVisibleItem >= (mHeaderPositions.get(2) - 1)){
            TextView textView = (TextView)mStickLayout.findViewById(R.id.tv_header_after);
            textView.setVisibility(View.VISIBLE);
            textView.setText("C组");
        } else {
            mStickLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void initData() {
        list = new ArrayList<>();

        groupKey.add("A组");
        groupKey.add("B组");
        groupKey.add("C组");

        for (int i = 0; i < 5; i++) {
            aList.add("A组" + i);
        }
        list.add("A组");
        list.addAll(aList);

        for (int i = 0; i < 8; i++) {
            bList.add("B组" + i);
        }
        list.add("B组");
        list.addAll(bList);

        for (int i = 0; i < 18; i++) {
            cList.add("C组" + i);
        }
        list.add("C组");
        list.addAll(cList);

        mHeaderPositions.add(2);
        mHeaderPositions.add(8);
        mHeaderPositions.add(17);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean isEnabled(int position) {
            if (groupKey.contains(getItem(position))) {
                return false;
            }
            return super.isEnabled(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (groupKey.contains(getItem(position))) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item_header, null);
            } else {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item, null);
            }
            TextView text = (TextView) view.findViewById(R.id.addexam_list_item_text);
            text.setText((CharSequence) getItem(position));
            return view;
        }

    }
}
