package com.xuf.www.xuf2048;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by xf on 2015/7/30.
 */
public class GameActivity extends Activity {

    private GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mGameView = new GameView(this);
        setContentView(mGameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGameView.getGameLogic().loadParams();
        try{
            mGameView.getGameLogic().loadStates();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mGameView.getGameLogic().saveParams();
        try{
            mGameView.getGameLogic().saveStates();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
