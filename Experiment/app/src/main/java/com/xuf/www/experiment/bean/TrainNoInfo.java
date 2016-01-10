package com.xuf.www.experiment.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/28.
 */
public class TrainNoInfo {

    public String mTrainNo;
    public String mTrainType;
    public String mStartStation;
    public String mStartStationType;
    public String mEndStation;
    public String mEndStationType;
    public String mStartTime;
    public String mEndTime;
    public String mRunTime;
    public String mRunDistance;
    public List<PriceInfo> mPriceInfoList;

    public TrainNoInfo(){
        mPriceInfoList = new ArrayList<>();
    }

    public static class PriceInfo{
        public String mPriceType;
        public String mPrice;
    }

    public static TrainNoInfo fromJsonTrainNo(JSONObject jsonObject, boolean queryTrainNo){
        TrainNoInfo trainNoInfo = new TrainNoInfo();
        trainNoInfo.mTrainNo = jsonObject.optString("train_no");
        trainNoInfo.mTrainType = jsonObject.optString("train_type");
        trainNoInfo.mStartStation = jsonObject.optString("start_station");
        trainNoInfo.mStartStationType = jsonObject.optString("start_station_type");
        trainNoInfo.mEndStation = jsonObject.optString("end_station");
        trainNoInfo.mEndStationType = jsonObject.optString("end_station_type");
        trainNoInfo.mStartTime = jsonObject.optString("start_time");
        trainNoInfo.mEndTime = jsonObject.optString("end_time");
        trainNoInfo.mRunTime = jsonObject.optString("run_time");
        trainNoInfo.mRunDistance = jsonObject.optString("run_distance");
        JSONArray jsonArray;
        if (queryTrainNo) {
            jsonArray = jsonObject.optJSONObject("price_list").optJSONArray("item");
        } else {
            jsonArray = jsonObject.optJSONArray("price_list");
        }
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject object = jsonArray.optJSONObject(i);
            PriceInfo priceInfo = new PriceInfo();
            priceInfo.mPriceType = object.optString("price_type");
            priceInfo.mPrice = object.optString("price");
            trainNoInfo.mPriceInfoList.add(priceInfo);
        }

        return trainNoInfo;
    }

    public static List<TrainNoInfo> fromJsonList(JSONArray jsonArray) throws Exception{
        List<TrainNoInfo> trainNoInfoList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            TrainNoInfo trainNoInfo = fromJsonTrainNo(jsonObject, false);
            trainNoInfoList.add(trainNoInfo);
        }

        return trainNoInfoList;
    }
}
