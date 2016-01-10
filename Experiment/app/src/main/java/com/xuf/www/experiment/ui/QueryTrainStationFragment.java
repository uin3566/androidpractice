package com.xuf.www.experiment.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xuf.www.experiment.GlobalConfig;
import com.xuf.www.experiment.R;
import com.xuf.www.experiment.adapter.CommonAdapter;
import com.xuf.www.experiment.bean.TrainNoInfo;
import com.xuf.www.experiment.util.CommonViewHolder;
import com.xuf.www.experiment.util.HttpUrlConnectionAsync;
import com.xuf.www.experiment.util.MyApplication;
import com.xuf.www.experiment.util.ToastUtil;
import com.xuf.www.experiment.widget.ScrollPriceTableView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2015/10/28.
 */
public class QueryTrainStationFragment extends Fragment implements
        HttpUrlConnectionAsync.QueryDataCallback{

    private HttpUrlConnectionAsync mHttpUrlConnectionWrapper;

    private ListView mListView;

    private TrainInfoAdapter mAdapter;

    private ProgressDialog mProgressDialog;

    private List<TrainNoInfo> mTrainNoInfoList;

    private EditText mFromStation;
    private EditText mToStation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_station_query, container, false);

        _init(view);

        return view;
    }

    private void _init(View view){
        mHttpUrlConnectionWrapper = new HttpUrlConnectionAsync();
        mHttpUrlConnectionWrapper.setCallback(this);

        Button query = (Button)view.findViewById(R.id.btn_query);
        mFromStation = (EditText)view.findViewById(R.id.et_from_station);
        mToStation = (EditText)view.findViewById(R.id.et_to_station);

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = mFromStation.getText().toString();
                String to = mToStation.getText().toString();
                if (from.equals("")) {
                    ToastUtil.showShort(getActivity(), "请输入出发地");
                    return;
                }
                if (to.equals("")) {
                    ToastUtil.showShort(getActivity(), "请输入目的地");
                    return;
                }

                try {
                    String fromUtf = URLEncoder.encode(from, "utf-8");
                    String toUtf = URLEncoder.encode(to, "utf-8");
                    String queryUrl = String.format(GlobalConfig.QUERY_TRAIN_STATION_URL, GlobalConfig.TRAIN_QUERY_APP_KEY, fromUtf, toUtf);
                    //mHttpUrlConnectionWrapper.queryStringData(queryUrl);
                    _queryByVolley(queryUrl);
                } catch (Exception e){
                    e.printStackTrace();
                }
                mProgressDialog = ProgressDialog.show(getActivity(), null, "请稍后...");
            }
        });

        mListView = (ListView)view.findViewById(R.id.lv_train_info);
        mAdapter = new TrainInfoAdapter(getActivity(), R.layout.layout_train_info, mTrainNoInfoList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onQueryDataResult(boolean success, String result) {
        mProgressDialog.dismiss();
        if (!success){
            return;
        }
        try {
            JSONObject resultJson = new JSONObject(result);
            int errorCode = resultJson.optInt("error_code");
            if (errorCode == 0){
                JSONArray array = resultJson.optJSONObject("result").optJSONArray("list");
                mTrainNoInfoList = TrainNoInfo.fromJsonList(array);
            }else {
                String errorReason = null;
                switch (errorCode){
                    case 207905:
                        errorReason = "查询不到火车始发站相关信息";
                        break;
                    case 207903:
                        errorReason = "网络错误，请重试";
                        break;
                }
                ToastUtil.showShort(getActivity(), errorReason);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        mAdapter.setDataList(mTrainNoInfoList);
    }

    private void _queryByVolley(String queryUrl){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(queryUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                mProgressDialog.dismiss();
                try {
                    int errorCode = jsonObject.optInt("error_code");
                    if (errorCode == 0){
                        JSONArray array = jsonObject.optJSONObject("result").optJSONArray("list");
                        mTrainNoInfoList = TrainNoInfo.fromJsonList(array);
                    }else {
                        String errorReason = null;
                        switch (errorCode){
                            case 207905:
                                errorReason = "查询不到火车始发站相关信息";
                                break;
                            case 207903:
                                errorReason = "网络错误，请重试";
                                break;
                        }
                        ToastUtil.showShort(getActivity(), errorReason);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

                mAdapter.setDataList(mTrainNoInfoList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private class TrainInfoAdapter extends CommonAdapter<TrainNoInfo>{
        private TrainInfoAdapter(Context context, int layoutId, List<TrainNoInfo> dataList) {
            super(context, layoutId, dataList);
        }

        @Override
        public void fillView(CommonViewHolder holder, int position) {
            fillTrainNoInfo(holder, getItem(position));
        }

        private void fillTrainNoInfo(CommonViewHolder holder, TrainNoInfo info){
            TextView trainType = holder.getView(R.id.tv_train_type);
            TextView startStation = holder.getView(R.id.tv_start_station);
            TextView endStation = holder.getView(R.id.tv_end_station);
            TextView startTime = holder.getView(R.id.tv_start_time);
            TextView endTime = holder.getView(R.id.tv_end_time);
            TextView mileage = holder.getView(R.id.tv_run_mileage);
            TextView runTime = holder.getView(R.id.tv_run_time);
            ScrollPriceTableView priceTableView = holder.getView(R.id.price_table_view);

            trainType.setText(info.mTrainType);
            startStation.setText(info.mStartStation);
            endStation.setText(info.mEndStation);
            startTime.setText(info.mStartTime + "开");
            endTime.setText(info.mEndTime + "到");
            runTime.setText(info.mRunTime);
            if (info.mRunDistance.equals("")){
                mileage.setText("无相关信息");
            } else {
                mileage.setText(info.mRunDistance);
            }

            priceTableView.setTrainPrice(info.mPriceInfoList);
        }
    }
}
