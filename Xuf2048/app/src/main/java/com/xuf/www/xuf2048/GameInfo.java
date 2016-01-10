package com.xuf.www.xuf2048;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xf on 2015/8/11.
 */
public class GameInfo {

    private static final String JSON_SCORE = "score";
    private static final String JSON_DATE = "date";
    private static final String JSON_TIME_STAMP = "stamp";


    private String mDate;
    private int mScore;
    private long mTimeStamp;

    private static final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm:ss");

    public String getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = mSimpleDateFormat.format(new Date());
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public long getTimeStampSeconds() {
        return mTimeStamp / 1000;
    }

    public JSONObject toJson() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_DATE, mDate);
        jsonObject.put(JSON_SCORE, mScore);
        jsonObject.put(JSON_TIME_STAMP, mTimeStamp);
        return jsonObject;
    }

    public GameInfo(int score){
        mTimeStamp = System.currentTimeMillis();
        mDate = mSimpleDateFormat.format(new Date(mTimeStamp));
        mScore = score;
    }

    public GameInfo(JSONObject jsonObject) throws JSONException{
        mDate = jsonObject.getString(JSON_DATE);
        mScore = jsonObject.getInt(JSON_SCORE);
        mTimeStamp = jsonObject.getLong(JSON_TIME_STAMP);
    }
}
