package com.xuf.www.experiment.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuf.www.experiment.GlobalConfig;
import com.xuf.www.experiment.R;
import com.xuf.www.experiment.bean.TrainNoInfo;
import com.xuf.www.experiment.util.HttpUrlConnectionAsync;
import com.xuf.www.experiment.util.ToastUtil;
import com.xuf.www.experiment.widget.ScrollPriceTableView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/28.
 */
public class QueryTrainNoFragment extends Fragment  implements HttpUrlConnectionAsync.QueryDataCallback {

    private HttpUrlConnectionAsync mHttpUrlConnectionWrapper;

    private LinearLayout mTrainInfoLayout;
    private ProgressDialog mProgressDialog;
    private ScrollPriceTableView mPriceTableView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_no_query, container, false);

        _init(view);

        return view;
    }

    private void _init(View view){
        mHttpUrlConnectionWrapper = new HttpUrlConnectionAsync();
        mHttpUrlConnectionWrapper.setCallback(this);

        final EditText trainNoEditText = (EditText)view.findViewById(R.id.et_train_no);
        Button trainNoQueryButton = (Button)view.findViewById(R.id.btn_query);

        trainNoQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trainNo = trainNoEditText.getText().toString();
                if (trainNo.equals("")){
                    ToastUtil.showShort(getActivity(), "请输入查询车次");
                    return;
                }
                String queryUrl = String.format(GlobalConfig.QUERY_TRAIN_NO_URL, GlobalConfig.TRAIN_QUERY_APP_KEY, trainNo);
                try {
                    mHttpUrlConnectionWrapper.queryStringData(queryUrl);
                }catch (Exception e){
                    e.printStackTrace();
                }
                mProgressDialog = ProgressDialog.show(getActivity(), null, "请稍后...");
            }
        });

        mTrainInfoLayout = (LinearLayout)view.findViewById(R.id.ll_train_info);
        mTrainInfoLayout.setVisibility(View.INVISIBLE);
        mPriceTableView = (ScrollPriceTableView)view.findViewById(R.id.price_table_view);
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
        if (!success) {
            return;
        }
        try {
            JSONObject resultJson = new JSONObject(result);
            int errorCode = resultJson.optInt("error_code");
            if (errorCode == 0){
                JSONObject infoJson = resultJson.optJSONObject("result").optJSONObject("list");
                TrainNoInfo trainNoInfo = TrainNoInfo.fromJsonTrainNo(infoJson, true);
                _fillTrainInfo(trainNoInfo);
            }else {
                String errorReason = null;
                switch (errorCode) {
                    case 207902:
                        errorReason = "查询不到火车该班次相关信息";
                        break;
                    case 207903:
                        errorReason = "网络错误，请重试";
                        break;
                }
                ToastUtil.showShort(getActivity(), errorReason);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void _fillTrainInfo(TrainNoInfo info){
        TextView trainType = (TextView)mTrainInfoLayout.findViewById(R.id.tv_train_type);
        TextView startStation = (TextView)mTrainInfoLayout.findViewById(R.id.tv_start_station);
        TextView endStation = (TextView)mTrainInfoLayout.findViewById(R.id.tv_end_station);
        TextView startTime = (TextView)mTrainInfoLayout.findViewById(R.id.tv_start_time);
        TextView endTime = (TextView)mTrainInfoLayout.findViewById(R.id.tv_end_time);
        TextView mileage = (TextView)mTrainInfoLayout.findViewById(R.id.tv_run_mileage);
        TextView runTime = (TextView)mTrainInfoLayout.findViewById(R.id.tv_run_time);

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

        mPriceTableView.setTrainPrice(info.mPriceInfoList);
        mTrainInfoLayout.setVisibility(View.VISIBLE);
    }
}
