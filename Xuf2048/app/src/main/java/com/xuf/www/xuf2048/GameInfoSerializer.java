package com.xuf.www.xuf2048;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xf on 2015/8/11.
 */
public class GameInfoSerializer {

    private Context mContext;
    private String mInfoFileName;
    private String mStateFileName;
    private List<GameInfo> mGameInfoList = new ArrayList<GameInfo>();
    private int[][] mGamePanel = new int[GameConfig.SQUARE_COUNT][GameConfig.SQUARE_COUNT];
    private int mScore = 0;

    private static final String JSON_SCORE = "json_score";
    private static final String JSON_GAME_PANEL = "json_game_panel";
    private static final String JSON_GAME_POS_NUM = "json_game_pos_num";

    public GameInfoSerializer(Context context){
        mContext = context;
        mInfoFileName = GameConfig.HISTORY_FILE_NAME;
        mStateFileName = GameConfig.GAME_STATE_FILE_NAME;
    }

    public List<GameInfo> getGameInfoList(){
        return mGameInfoList;
    }

    public int getScore(){
        return mScore;
    }

    public int[][] getGamePanel(){
        return mGamePanel;
    }

    public void saveGameState(int score, int[][] gamePanel) throws IOException{
        mScore = score;
        JSONArray jsonStateArray = new JSONArray();
        BufferedWriter writer = null;
        try{
            JSONObject scoreObject = new JSONObject();
            scoreObject.put(JSON_SCORE, mScore);
            jsonStateArray.put(scoreObject);

            JSONObject panelObject = new JSONObject();
            int pos = 0;
            for (int i = 0; i < GameConfig.SQUARE_COUNT; i++){
                for (int j = 0; j < GameConfig.SQUARE_COUNT; j++){
                    panelObject.put(JSON_GAME_POS_NUM + pos, gamePanel[i][j]);
                    pos++;
                }
            }
            jsonStateArray.put(panelObject);

            OutputStream outputStream = mContext.openFileOutput(GameConfig.GAME_STATE_FILE_NAME, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(jsonStateArray.toString());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (writer != null){
                writer.close();
            }
        }
    }

    public void loadGameState() throws IOException, JSONException{
        BufferedReader reader = null;
        JSONArray jsonStateArray = null;
        try{
            InputStream inputStream = mContext.openFileInput(GameConfig.GAME_STATE_FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                jsonString.append(line);
            }
            jsonStateArray = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            JSONObject scoreObject = jsonStateArray.getJSONObject(0);
            JSONObject panelObject = jsonStateArray.getJSONObject(1);
            mScore = scoreObject.getInt(JSON_SCORE);
            int pos = 0;
            for (int i = 0; i < GameConfig.SQUARE_COUNT; i++){
                for (int j = 0; j < GameConfig.SQUARE_COUNT; j++){
                    mGamePanel[i][j] = panelObject.getInt(JSON_GAME_POS_NUM + pos);
                    pos++;
                }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } finally {
            if (reader != null){
                reader.close();
            }
        }

    }

    public void saveGameInfo(GameInfo gameInfo) throws IOException, JSONException{
        BufferedWriter writer = null;
        try {
            JSONArray jsonArray = new JSONArray();
            loadGameInfo();
            for (int i = 0; i < mGameInfoList.size(); i++){
                JSONObject jsonObject = mGameInfoList.get(i).toJson();
                jsonArray.put(jsonObject);
            }
            jsonArray.put(gameInfo.toJson());
            OutputStream outputStream = mContext.openFileOutput(mInfoFileName, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(jsonArray.toString());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (writer != null){
                writer.close();
            }
        }

    }

    public void loadGameInfo() throws IOException, JSONException{
        mGameInfoList.clear();
        BufferedReader reader = null;
        try {
            InputStream inputStream = mContext.openFileInput(mInfoFileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                jsonString.append(line);
            }

            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++){
                JSONObject jsonObject = array.getJSONObject(i);
                mGameInfoList.add(new GameInfo(jsonObject));
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } finally {
            if (reader != null){
                reader.close();
            }
        }
    }

}

