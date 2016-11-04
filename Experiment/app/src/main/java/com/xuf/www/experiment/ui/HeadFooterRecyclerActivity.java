package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.adapter.SimpleRecyclerAdapter;
import com.xuf.www.experiment.util.HeaderAndFooterWrapper;

/**
 * Created by Administrator on 2016/10/26.
 */
public class HeadFooterRecyclerActivity extends Activity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_footer_recycler);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter(this);

        final HeaderAndFooterWrapper wrapper = new HeaderAndFooterWrapper(adapter);
        TextView tv1 = new TextView(this);
        tv1.setText("head1");
        wrapper.addHeaderView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("head2");
        wrapper.addHeaderView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText("footer1");
        wrapper.addFootView(tv3);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wrapper);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView t4 = new TextView(HeadFooterRecyclerActivity.this);
                t4.setText("head3");
                wrapper.addHeaderView(t4);
                wrapper.notifyDataSetChanged();
            }
        }, 2000);
    }
}
