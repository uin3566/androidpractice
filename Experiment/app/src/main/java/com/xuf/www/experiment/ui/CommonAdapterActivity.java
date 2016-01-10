package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.adapter.CommonAdapter;
import com.xuf.www.experiment.util.CommonViewHolder;
import com.xuf.www.experiment.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/21.
 */
public class CommonAdapterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_adapter);

        final List<Bean> dataList = new ArrayList<>();
        for (int i = 0; i < 1000; i++){
            String str = "我是第" + (i + 1) + "项";
            dataList.add(new Bean(R.mipmap.ic_launcher, str));
        }

        ListView listView = (ListView)findViewById(R.id.lv_common_adapter);
        final CommonAdapter<Bean> adapter = new CommonAdapter<Bean>(this, R.layout.item_common_adapter, dataList) {
            @Override
            public void fillView(final CommonViewHolder holder, final int position) {
                Bean bean = getItem(position);
                holder.setImageResource(R.id.iv_bean, bean.getImgId());
                holder.setText(R.id.tv_bean, bean.getContent());
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showShort(CommonAdapterActivity.this, "点击了第" + (position + 1) + "项");
                    }
                });
            }
        };
        listView.setAdapter(adapter);
    }

    private class Bean{
        private int imgId;
        private String content;

        public Bean(int imgId, String content){
            this.imgId = imgId;
            this.content = content;
        }

        public int getImgId() {
            return imgId;
        }

        public String getContent() {
            return content;
        }
    }
}
