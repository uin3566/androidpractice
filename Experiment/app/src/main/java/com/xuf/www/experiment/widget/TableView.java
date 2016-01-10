package com.xuf.www.experiment.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.util.DimenUtil;
import com.xuf.www.experiment.util.ToastUtil;

/**
 * Created by Administrator on 2015/10/29.
 */
public class TableView extends ViewGroup {

    private static final int DEFAULT_ROWS = 2;
    private static final int DEFAULT_COLUMNS = 2;

    private int mColumns = DEFAULT_COLUMNS;
    private int mRows = DEFAULT_ROWS;

    private Context mContext;

    //attrs
    private int textSize;
    private int textColor = 0xFF000000;//定义默认color的时候一定要8位，否则，颜色显示不出来
    private int textViewBackgroundResId = R.color.white;
    private int dividingWidth;
    private int dividingColor = 0xFFE1464C;

    public TableView(Context context) {
        this(context, null, 0);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableView);
        textSize = a.getDimensionPixelSize(R.styleable.TableView_tbvTextSize, DimenUtil.sp2px(mContext, 17));
        textColor = a.getColor(R.styleable.TableView_tbvTextColor, textColor);
        textViewBackgroundResId = a.getResourceId(R.styleable.TableView_tbvTextViewBackground, textViewBackgroundResId);
        dividingWidth = a.getDimensionPixelSize(R.styleable.TableView_tbvDividingWidth, DimenUtil.dp2px(mContext, 1));
        dividingColor = a.getColor(R.styleable.TableView_tbvDividingColor, dividingColor);
        a.recycle();

        _addChildViews();
    }

    public void setTableDimens(int columns, int rows){
        if (rows > 0){
            mRows = rows;
        }
        if (columns > 0) {
            mColumns = columns;
        }
        _addChildViews();
    }

    public void setText(int childPosition, String str){
        Button textView = (Button)getChildAt(childPosition);
        textView.setText(str);
    }

    public void setTableWidth(int width){
        setLayoutParams(new LayoutParams(width, LayoutParams.WRAP_CONTENT));
    }

    private void _addChildViews(){
        removeAllViews();
        for (int i = 0; i < mRows; i++){
            for (int j = 0; j < mColumns; j++){
                Button textView = new Button(mContext);
                textView.setClickable(false);
                textView.setBackgroundResource(textViewBackgroundResId);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                textView.setTextColor(textColor);
                textView.setText("dsfjklaj");
                textView.setIncludeFontPadding(false);
                textView.setGravity(Gravity.CENTER);
                addView(textView);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(dividingWidth);
        paint.setColor(dividingColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        // 画列分割线
        for(int i = 0;i <= mColumns; i++){
            canvas.drawLine((getWidth() / mColumns) * i, 0, (getWidth() / mColumns) * i, getHeight(), paint);
        }
        // 画行分割线
        for(int j = 0; j <= mRows; j++){
            canvas.drawLine(0, (getHeight() / mRows) * j, getWidth(), (getHeight() / mRows) * j, paint);
        }

        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int curColumn = 0;
        int curX = dividingWidth;
        int curY = dividingWidth;
        for (int j = 0; j < childCount; j++){
            View child = getChildAt(j);
            child.layout(curX, curY, curX + getWidth() / mColumns - dividingWidth * 2, curY + getHeight() / mRows - dividingWidth * 2);
            if (curColumn < mColumns - 1){
                curColumn++;
                curX += getWidth() / mColumns;
            } else {
                curColumn = 0;
                curY += getHeight() / mRows;
                curX = dividingWidth;
            }
        }
    }
}