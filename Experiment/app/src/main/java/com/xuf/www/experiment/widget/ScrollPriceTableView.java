package com.xuf.www.experiment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.xuf.www.experiment.bean.TrainNoInfo;
import com.xuf.www.experiment.util.DimenUtil;

import java.util.List;

/**
 * Created by lenov0 on 2015/10/30.
 */
public class ScrollPriceTableView extends HorizontalScrollView {

    private Context mContext;
    private LinearLayout mLinearLayout;

    private int tableWidthDp = 100;

    public ScrollPriceTableView(Context context) {
        this(context, null, 0);
    }

    public ScrollPriceTableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollPriceTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mLinearLayout);
    }

    public void setTrainPrice(List<TrainNoInfo.PriceInfo> priceInfoList) {
        mLinearLayout.removeAllViews();
        for (final TrainNoInfo.PriceInfo priceInfo : priceInfoList) {
            final TableView tableView = new TableView(mContext);
            tableView.setTableWidth(DimenUtil.dp2px(mContext, tableWidthDp));
            tableView.setTableDimens(1, 2);
            tableView.setText(0, priceInfo.mPriceType);
            tableView.setText(1, priceInfo.mPrice + "元");
            mLinearLayout.addView(tableView);
        }
    }
}
