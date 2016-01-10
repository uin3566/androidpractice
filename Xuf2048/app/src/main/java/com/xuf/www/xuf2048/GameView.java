package com.xuf.www.xuf2048;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xf on 2015/7/30.
 */
public class GameView extends View implements View.OnTouchListener, GameLogic.DataChangeCallback{

    private Context mContext;

    //Game Logic
    private GameLogic mGameLogic;
    private int[][] mGamePanel;

    //Game Params
    static int SQUARE_COUNT;
    static final int CELL_TYPES = 14;

    private Paint mPaint;
    private Resources mResources;

    //Resources
    private Drawable mPanelDrawable;
    private Bitmap mPanelBitmap;
    private Drawable mBackgroundDrawable;
    private Drawable[] mCellDrawable = new Drawable[CELL_TYPES];
    private Bitmap[] mCellBitmap = new Bitmap[CELL_TYPES];
    private Drawable mRefreshDrawable;

    //Layout sizes
    private float mMainRegionStartX;
    private float mMainRegionStartY;
    private float mMainRegionEndX;
    private float mMainRegionEndY;
    private float mMainRegionLength;
    private float mCellLength;
    private float mCellMargin;
    private float mCellTextSize;
    private float mSx;
    private float mSy;
    private float mEx;
    private float mEy;
    private float mScoreSx;
    private float mScoreEx;
    private float mHighScoreSx;
    private float mHighScoreEx;
    private float mScoreSy;
    private float mScoreEy;
    private float mScorePadding;
    private float mScoreBgHeight;
    private float mScoreBgWidth;
    private float mHighScoreBgWidth;
    private float mRefreshBgSx;
    private float mRefreshBgEx;
    private float mScoreSize;
    private float mScoreTextSize;
    private float mTitleMarginLeft;
    private float mTitleMarginTop;
    private float mTitleTextSize;

    //Touch Params
    private float startX = 0;
    private float curX = 0;
    private float startY = 0;
    private float curY = 0;
    private final int distance = 70;
    private final float whRatio = 1.2f;//宽高比or高宽比

    //Text
    private String mTitleString;
    private String mScoreString;
    private String mHighScoreString;

    public GameView(Context context) {
        super(context);

        setOnTouchListener(this);

        mContext = context;

        _loadConfig();

        mGameLogic = new GameLogic(mContext);
        mGameLogic.setCallback(this);
        mGamePanel = mGameLogic.getGamePanel();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mResources = getResources();

        mRefreshDrawable = mResources.getDrawable(R.mipmap.ic_action_refresh);
        mBackgroundDrawable = mResources.getDrawable(R.mipmap.game_page_background);
        mPanelDrawable = mResources.getDrawable(R.drawable.shape_pannel_background);
        mCellDrawable[0] = mResources.getDrawable(R.drawable.shape_cell);
        mCellDrawable[1] = mResources.getDrawable(R.drawable.shape_cell_2);
        mCellDrawable[2] = mResources.getDrawable(R.drawable.shape_cell_4);
        mCellDrawable[3] = mResources.getDrawable(R.drawable.shape_cell_8);
        mCellDrawable[4] = mResources.getDrawable(R.drawable.shape_cell_16);
        mCellDrawable[5] = mResources.getDrawable(R.drawable.shape_cell_32);
        mCellDrawable[6] = mResources.getDrawable(R.drawable.shape_cell_64);
        mCellDrawable[7] = mResources.getDrawable(R.drawable.shape_cell_128);
        mCellDrawable[8] = mResources.getDrawable(R.drawable.shape_cell_256);
        mCellDrawable[9] = mResources.getDrawable(R.drawable.shape_cell_512);
        mCellDrawable[10] = mResources.getDrawable(R.drawable.shape_cell_1024);
        mCellDrawable[11] = mResources.getDrawable(R.drawable.shape_cell_2048);
        mCellDrawable[12] = mResources.getDrawable(R.drawable.shape_cell_4096);
        mCellDrawable[13] = mResources.getDrawable(R.drawable.shape_cell_8192);

        mTitleString = mResources.getString(R.string.game_title);
        mScoreString = getContext().getString(R.string.score);
        mHighScoreString = getContext().getString(R.string.high_score);
    }

    private void _loadConfig(){
        SQUARE_COUNT = GameConfig.SQUARE_COUNT;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mPanelBitmap, 0, 0, mPaint);
        _drawCells(canvas);
        _drawScore(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        _setLayoutParams(w, h);
        _getBackgroundBitmap(w, h);
        _getCellBitmap();
    }

    @Override
    public void onDataChanged(int[][] gamePanel) {
        mGamePanel = gamePanel;
        invalidate();
    }

    public GameLogic getGameLogic(){
        return mGameLogic;
    }

    private void _setLayoutParams(int width, int height){
        int minLength = (width < height) ? width : height;
        int mainRegionMargin = minLength / 20;

        mMainRegionStartX = mainRegionMargin;
        mMainRegionEndX = width - mainRegionMargin;

        mMainRegionLength = mMainRegionEndX - mMainRegionStartX;
        float gridLength = mMainRegionLength / SQUARE_COUNT;
        mCellLength = gridLength * 6 / 7;
        mCellMargin = gridLength / 7;
        mCellTextSize = mCellLength * 0.3f;

        mMainRegionEndY = height - gridLength;
        mMainRegionStartY = mMainRegionEndY - mMainRegionLength;
        //棋盘需要多出mCellMargin
        mMainRegionStartX -= mCellMargin / 2;
        mMainRegionEndX += mCellMargin / 2;
        mMainRegionStartY -= mCellMargin / 2;
        mMainRegionEndY += mCellMargin / 2;
        mMainRegionLength += mCellMargin;

        mTitleTextSize = mCellLength * 0.7f;
        mTitleMarginLeft = mCellLength * 1.2f;
        mTitleMarginTop = mMainRegionStartY - mCellLength / 2;
    }

    private void _getBackgroundBitmap(int width, int height){
        mPanelBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mPanelBitmap);
        _drawBackground(canvas, width, height);
        _drawTitle(canvas);
        _drawRefreshIcon(canvas);
        _drawMainRegion(canvas);
        _drawMainRegionGrid(canvas);
    }

    private void _drawBackground(Canvas canvas, int width, int height){
        _drawDrawable(canvas, mBackgroundDrawable, 0, 0, width, height);
    }

    private void _drawTitle(Canvas canvas){
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mResources.getColor(R.color.text_title));
        mPaint.setTextSize(mTitleTextSize);
        canvas.drawText(mTitleString, mTitleMarginLeft, mTitleMarginTop, mPaint);
    }

    private void _drawMainRegion(Canvas canvas){
        _drawDrawable(canvas, mPanelDrawable, (int)mMainRegionStartX, (int)mMainRegionStartY, (int)mMainRegionEndX, (int)mMainRegionEndY);
    }

    private void _drawMainRegionGrid(Canvas canvas){
        for (int yy = 0; yy < SQUARE_COUNT; yy++) {
            for (int xx = 0; xx < SQUARE_COUNT; xx++) {
                _convertCoordinate(xx, yy);
                _drawDrawable(canvas, mCellDrawable[0], (int)mSx, (int)mSy, (int)mEx, (int)mEy);
            }
        }
    }

    private void _getCellBitmap(){
        mPaint.setTextSize(mCellTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setColor(getResources().getColor(R.color.text_black));
        int textAdjustY = (int)((mPaint.descent() + mPaint.ascent()) / 2);
        for (int i = 0; i < CELL_TYPES; i++){
            Bitmap cellBitmap = Bitmap.createBitmap((int)mCellLength, (int)mCellLength, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(cellBitmap);
            _drawDrawable(canvas, mCellDrawable[i], 0, 0, (int)mCellLength, (int)mCellLength);
            if (i > 0){
                int number = (int)Math.pow(2, i);
                canvas.drawText(String.valueOf(number), mCellLength / 2, mCellLength / 2 - textAdjustY, mPaint);
            }
            mCellBitmap[i] = cellBitmap;
        }
    }

    private void _drawDrawable(Canvas canvas, Drawable draw, int startingX, int startingY, int endingX, int endingY){
        draw.setBounds(startingX, startingY, endingX, endingY);
        draw.draw(canvas);
    }

    private void _drawCells(Canvas canvas){
        for (int i = 0; i < SQUARE_COUNT; i++){
            for (int j = 0; j < SQUARE_COUNT; j++){
                _drawItem(canvas, mGamePanel[i][j], i, j);
            }
        }
    }

    private void _drawItem(Canvas canvas, int itemNumber, int xx, int yy){
        Bitmap bitmap;
        if (itemNumber == 0){
            bitmap = mCellBitmap[0];
        } else {
            int pos = (int)(Math.log((double)itemNumber) / Math.log((double)2));
            bitmap = mCellBitmap[pos];
        }
        Drawable drawable = new BitmapDrawable(bitmap);
        _convertCoordinate(xx, yy);
        _drawDrawable(canvas, drawable, (int)mSx, (int)mSy, (int)mEx, (int)mEy);
    }

    private void _convertCoordinate(int xx, int yy){
        mSx = mMainRegionStartX + mCellMargin + yy * (mCellLength + mCellMargin);
        mEx = mSx + mCellLength;
        mSy = mMainRegionStartY + mCellMargin + xx * (mCellLength + mCellMargin);
        mEy = mSy + mCellLength;
    }

    private void _drawScore(Canvas canvas){
        mPaint.setTextAlign(Paint.Align.CENTER);

        mScoreSize = mCellLength * 0.25f;
        mScoreTextSize = mScoreSize * 0.5f;
        mPaint.setTextSize(mScoreTextSize);
        float scoreTextWidth = mPaint.measureText(mScoreString);
        float highScoreTextWidth = mPaint.measureText(mHighScoreString);

        mPaint.setTextSize(mScoreSize);
        float scoreNumberWidth = mPaint.measureText(String.valueOf(mGameLogic.getScore()));
        float highScoreNumberWidth = mPaint.measureText(String.valueOf(mGameLogic.getHighScore()));

        float scoreWidth = Math.max(scoreTextWidth, scoreNumberWidth);
        float highScoreWidth = Math.max(highScoreTextWidth, highScoreNumberWidth);
        mScorePadding = mScoreSize * 0.65f;
        mScoreBgWidth = scoreWidth + mScorePadding;
        mHighScoreBgWidth = highScoreWidth + mScorePadding;
        mScoreBgHeight = mCellLength * 0.5f;

        mScoreSx = mTitleMarginLeft + mCellLength + mScorePadding;
        mScoreEx = mScoreSx + mScoreBgWidth;
        mScoreSy = mMainRegionStartY - mCellLength;
        mScoreEy = mScoreSy + mScoreBgHeight;
        mHighScoreSx = mScoreEx + mScorePadding * 2;
        mHighScoreEx = mHighScoreSx + mHighScoreBgWidth;

        //draw score
        _drawDrawable(canvas, mPanelDrawable, (int)mScoreSx, (int)mScoreSy, (int)mScoreEx, (int)mScoreEy);
        mPaint.setTextSize(mScoreTextSize);
        mPaint.setColor(mResources.getColor(R.color.text_white));
        canvas.drawText(mScoreString, mScoreSx + mScoreBgWidth / 2, mScoreSy + mScoreTextSize + mScorePadding / 3, mPaint);
        mPaint.setTextSize(mScoreSize);
        mPaint.setColor(mResources.getColor(R.color.text_black));
        canvas.drawText(String.valueOf(mGameLogic.getScore()), mScoreSx + mScoreBgWidth / 2, mScoreSy + mScoreBgHeight - mScorePadding / 3, mPaint);

        //draw high score
        _drawDrawable(canvas, mPanelDrawable, (int)mHighScoreSx, (int)mScoreSy, (int)mHighScoreEx, (int)mScoreEy);
        mPaint.setTextSize(mScoreTextSize);
        mPaint.setColor(mResources.getColor(R.color.text_white));
        canvas.drawText(mHighScoreString, mHighScoreSx + mHighScoreBgWidth / 2, mScoreSy + mScoreTextSize + mScorePadding / 3, mPaint);
        mPaint.setTextSize(mScoreSize);
        mPaint.setColor(mResources.getColor(R.color.text_black));
        canvas.drawText(String.valueOf(mGameLogic.getHighScore()), mHighScoreSx + mHighScoreBgWidth / 2, mScoreSy + mScoreBgHeight - mScorePadding / 3, mPaint);
    }

    private void _drawRefreshIcon(Canvas canvas){
        mRefreshBgSx = mCellLength * 4.3f;
        mRefreshBgEx = mRefreshBgSx + mCellLength * 0.5f;
        float refreshBgSy = mMainRegionStartY - mCellLength;
        float refreshBgEy = refreshBgSy + mCellLength * 0.5f;
        float padding = mCellLength * 0.25f * 0.65f / 3f;
        _drawDrawable(canvas, mPanelDrawable, (int)mRefreshBgSx, (int)refreshBgSy, (int)mRefreshBgEx, (int)refreshBgEy);
        _drawDrawable(canvas, mRefreshDrawable, (int)(mRefreshBgSx + padding), (int)(refreshBgSy + padding),
                (int)(mRefreshBgEx - padding), (int)(refreshBgEy - padding));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
                float dx = curX - startX;
                float dy = curY - startY;
                float absDx = Math.abs(dx);
                float absDy = Math.abs(dy);
                //触摸了刷新按钮
                if (Math.abs(dx) < distance && Math.abs(dy) < distance &&
                        curX >= mRefreshBgSx && curX <= mRefreshBgEx && curY >= mScoreSy && curY <= mScoreEy){
                    if (mGameLogic.getHighScore() <= mGameLogic.getScore()){
                        mGameLogic.saveParams();
                    }
                    mGameLogic.init(mContext);
                }
                if (dx > distance && (absDy < distance || absDx/absDy > whRatio)){//right
                    mGameLogic.move(GameLogic.MoveDirection.MOVE_RIGHT);
                } else if (dx < -distance && (absDy < distance || absDx/absDy > whRatio)){//left
                    mGameLogic.move(GameLogic.MoveDirection.MOVE_LEFT);
                } else if (dy > distance && (absDx < distance || absDy/absDx > whRatio)){//down
                    mGameLogic.move(GameLogic.MoveDirection.MOVE_DOWN);
                } else if (dy < -distance && (absDx < distance || absDy/absDx > whRatio)){//up
                    mGameLogic.move(GameLogic.MoveDirection.MOVE_UP);
                }
                mGamePanel = mGameLogic.getGamePanel();
                invalidate();
                break;
        }

        return true;
    }
}
