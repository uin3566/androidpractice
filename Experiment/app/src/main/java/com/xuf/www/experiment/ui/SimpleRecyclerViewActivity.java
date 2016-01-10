package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.adapter.SimpleRecyclerAdapter;

/**
 * Created by Xuf on 2016/1/9.
 */
public class SimpleRecyclerViewActivity extends Activity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycler_view);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SimpleRecyclerAdapter(this));
    }

}
