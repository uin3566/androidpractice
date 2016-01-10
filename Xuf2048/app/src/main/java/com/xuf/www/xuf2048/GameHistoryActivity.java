package com.xuf.www.xuf2048;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xf on 2015/8/9.
 */
public class GameHistoryActivity extends Activity {

    private ListView mHistoryListView;
    private List<GameInfo> mGameInfoList;
    private HistoryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        _init();
    }

    private void _init(){
        mHistoryListView = (ListView)findViewById(R.id.lv_history);
        mAdapter = new HistoryListAdapter();
        mHistoryListView.setAdapter(mAdapter);
        try{
            GameInfoSerializer infoSerializer= new GameInfoSerializer(this);
            infoSerializer.loadGameInfo();
            mGameInfoList = infoSerializer.getGameInfoList();
            _sort(mGameInfoList);
            mAdapter.notifyDataSetChanged();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _sort(List<GameInfo> infoList){
        Comparator comp = new SortComparator();
        Collections.sort(infoList, comp);
    }

    private class SortComparator implements Comparator{
        @Override
        public int compare(Object lhs, Object rhs) {
            GameInfo info1 = (GameInfo)lhs;
            GameInfo info2 = (GameInfo)rhs;

            return (int)(info2.getTimeStampSeconds() - info1.getTimeStampSeconds());
        }
    }

    private class ViewHolder{
        TextView mDateTextView;
        TextView mScoreTextView;

        public ViewHolder(View view){
            mDateTextView = (TextView)view.findViewById(R.id.tv_date);
            mScoreTextView = (TextView)view.findViewById(R.id.tv_score);
        }
    }

    private class HistoryListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            if (mGameInfoList == null){
                return 0;
            }
            return mGameInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mGameInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null){
                convertView = LayoutInflater.from(GameHistoryActivity.this).inflate(R.layout.list_item_history, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder = (ViewHolder)convertView.getTag();

            GameInfo gameInfo = (GameInfo)getItem(position);
            holder.mScoreTextView.setText(String.valueOf(gameInfo.getScore()));
            holder.mDateTextView.setText(String.valueOf(gameInfo.getDate()));

            return convertView;
        }
    }
}
