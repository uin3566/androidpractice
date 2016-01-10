package com.xuf.www.xuf2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by xf on 2015/8/8.
 */
public class GameLogic {

    private static final String GAME_PARAM_PREFERENCE = "com.xuf.www.xuf2048";
    private static final String HIGH_SCORE = GAME_PARAM_PREFERENCE + ".high_score";

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private GameInfoSerializer mInfoSerializer;
    private int mSquareCount;
    private int[][] mGamePanel;
    private int mScore = 0;
    private int mHighScore = 0;
    private boolean mGameOver = false;
    private List<Integer> mPositionList = new ArrayList<>();
    private DataChangeCallback mCallback;
    public enum MoveDirection{
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT
    }

    public interface DataChangeCallback{
        void onDataChanged(int[][] gamePanel);
    }

    public void setCallback(DataChangeCallback callback){
        mCallback = callback;
    }

    public GameLogic(Context context){
        init(context);
    }

    public void init(Context context){
        mContext = context;
        mGameOver = false;
        mScore = 0;
        mSharedPreferences = mContext.getSharedPreferences(GAME_PARAM_PREFERENCE, Context.MODE_PRIVATE);
        mInfoSerializer = new GameInfoSerializer(mContext);
        mSquareCount = GameConfig.SQUARE_COUNT;
        mPositionList.clear();;
        mGamePanel = new int[mSquareCount][mSquareCount];
        for (int i = 0; i < mSquareCount; i++){
            for (int j = 0; j < mSquareCount; j++){
                mGamePanel[i][j] = 0;
            }
        }
        _randomGenerate();
        _randomGenerate();
    }

    private void _randomGenerate(){
        _updatePositionList();
        int value = Math.random() < 0.75 ? 2 : 4;
        Random random = new Random();
        int pos = random.nextInt(mPositionList.size());
        int index = mPositionList.get(pos);
        int xx = index / 4;
        int yy = index % 4;
        mGamePanel[xx][yy] = value;
    }

    private void _updatePositionList(){
        mPositionList.clear();
        for (int i = 0; i < mSquareCount; i++){
            for (int j = 0; j < mSquareCount; j++){
                if (mGamePanel[i][j] == 0){
                    mPositionList.add(i * mSquareCount + j);
                }
            }
        }
    }

    public void saveParams(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(HIGH_SCORE, mHighScore);
        editor.commit();
    }

    public void loadParams(){
        mHighScore = mSharedPreferences.getInt(HIGH_SCORE, 0);
    }

    public void saveStates() throws IOException{
        mInfoSerializer.saveGameState(mScore, mGamePanel);
    }

    public void loadStates() throws IOException, JSONException{
        mInfoSerializer.loadGameState();
        mScore = mInfoSerializer.getScore();
        mGamePanel = mInfoSerializer.getGamePanel();
        mCallback.onDataChanged(mGamePanel);
    }

    private void _refreshHighScore(int score){
        if (score > mHighScore){
            mHighScore = score;
        }
    }

    private boolean _IsGameOver(){
        if (mPositionList.size() == 1){
            for (int i = 0; i < mSquareCount; i++){
                for (int j = 0; j < mSquareCount - 1; j++){
                    if (mGamePanel[i][j] == mGamePanel[i][j + 1]) {
                        return false;
                    }
                }
            }

            for (int j = 0; j < mSquareCount; j++){
                for (int i = 0; i < mSquareCount - 1; i++){
                    if (mGamePanel[i][j] == mGamePanel[i + 1][j]){
                        return false;
                    }
                }
            }
            return true;
        }

        return false;
    }

    public int getHighScore(){
        return mHighScore;
    }

    public int getScore(){
        return mScore;
    }

    public int[][] getGamePanel(){
        return mGamePanel;
    }

    //1，取值；2，合并；2，恢复
    public void move(MoveDirection direction){
        List<Integer> rowList = new ArrayList<>();
        int[] tmpArray;
        boolean moved = false;
        switch (direction){
            case MOVE_LEFT:
                for (int i = 0; i < mSquareCount; i++){
                    tmpArray = mGamePanel[i].clone();

                    for (int j = 0; j < mSquareCount; j++){
                        if (tmpArray[j] != 0){
                            rowList.add(tmpArray[j]);
                        }
                    }

                    _mergeRow(rowList);
                    _restoreRow(rowList, tmpArray);
                    for (int k = 0; k < mSquareCount; k++){
                        if (tmpArray[k] != mGamePanel[i][k]){
                            moved = true;
                        }
                    }
                    mGamePanel[i] = tmpArray;
                    rowList.clear();
                }
                break;
            case MOVE_RIGHT:
                for (int i = 0; i < mSquareCount; i++){
                    tmpArray = mGamePanel[i].clone();
                    _revertArray(tmpArray);

                    for (int j = 0; j < mSquareCount; j++){
                        if (tmpArray[j] != 0){
                            rowList.add(tmpArray[j]);
                        }
                    }

                    _mergeRow(rowList);
                    _restoreRow(rowList, tmpArray);
                    _revertArray(tmpArray);
                    for (int k = 0; k < mSquareCount; k++){
                        if (tmpArray[k] != mGamePanel[i][k]){
                            moved = true;
                        }
                    }
                    mGamePanel[i] = tmpArray;
                    rowList.clear();
                }
                break;
            case MOVE_UP:
                tmpArray = new int[mSquareCount];
                for (int j = 0; j < mSquareCount; j++){
                    for (int i = 0; i < mSquareCount; i++){
                        tmpArray[i] = mGamePanel[i][j];
                    }
                    for (int k = 0; k < mSquareCount; k++){
                        if (tmpArray[k] != 0){
                            rowList.add(tmpArray[k]);
                        }
                    }

                    _mergeRow(rowList);
                    _restoreRow(rowList, tmpArray);
                    for (int l = 0; l < mSquareCount; l++){
                        if (mGamePanel[l][j] != tmpArray[l]){
                            moved = true;
                        }
                        mGamePanel[l][j] = tmpArray[l];
                    }
                    rowList.clear();
                }
                break;
            case MOVE_DOWN:
                tmpArray = new int[mSquareCount];
                for (int j = 0; j < mSquareCount; j++){
                    for (int i = 0; i < mSquareCount; i++){
                        tmpArray[i] = mGamePanel[i][j];
                    }
                    _revertArray(tmpArray);
                    for (int k = 0; k < mSquareCount; k++){
                        if (tmpArray[k] != 0){
                            rowList.add(tmpArray[k]);
                        }
                    }

                    _mergeRow(rowList);
                    _restoreRow(rowList, tmpArray);
                    _revertArray(tmpArray);
                    for (int l = 0; l < mSquareCount; l++){
                        if (mGamePanel[l][j] != tmpArray[l]){
                            moved = true;
                        }
                        mGamePanel[l][j] = tmpArray[l];
                    }
                    rowList.clear();
                }
                break;
        }
        if (moved){
            _randomGenerate();
        }
        if (_IsGameOver()){
            saveParams();
            if (!mGameOver){
                mGameOver = true;
                try{
                    mInfoSerializer.saveGameInfo(new GameInfo(mScore));
                } catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(mContext, mContext.getString(R.string.game_over), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void _mergeRow(List<Integer> rowList){
        if (rowList.size() < 2){
            return;
        }

        for (int i = 0; i < rowList.size() - 1; i++){
            if (i + 1 > rowList.size() - 1){
                return;
            }
            int item1 = rowList.get(i);
            int item2 = rowList.get(i + 1);
            if (item2 == 0){
                return;
            }

            if (item1 == item2){
                int itemSum = item1 + item2;
                mScore += itemSum;
                _refreshHighScore(mScore);
                rowList.set(i, itemSum);
                for (int j = i + 1; j < rowList.size(); j++){
                    if (j != rowList.size() - 1){
                        rowList.set(j, rowList.get(j + 1));
                    }else {
                        rowList.set(j, 0);
                    }
                }
            }
        }
    }

    private void _restoreRow(List<Integer> rowList, int[] rowArray){
        for (int i = 0; i < rowList.size(); i++){
            rowArray[i] = rowList.get(i);
        }
        for (int i = rowList.size(); i < mSquareCount; i++){
            rowArray[i] = 0;
        }
    }

    private void _revertArray(int[] srcArray){
        int start = 0;
        int end = srcArray.length - 1;
        int tmp;

        while (start < end){
            tmp = srcArray[start];
            srcArray[start] = srcArray[end];
            srcArray[end] = tmp;
            start++;
            end--;
        }
    }

}
