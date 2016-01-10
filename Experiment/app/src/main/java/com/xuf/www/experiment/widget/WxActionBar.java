package com.xuf.www.experiment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.util.DimenUtil;

/**
 * Created by lenov0 on 2015/10/7.
 */
public class WxActionBar extends RelativeLayout {

    private Context mContext;

    private ActionBarCallback mCallback = null;

    private TextView mNavTextView;

    public void setActionBarCallback(ActionBarCallback callback){
        if (callback != null){
            mCallback = callback;
        }
    }

    public WxActionBar(Context context) {
        this(context, null);
    }

    public WxActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context);
    }

    private void _init(Context context){
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.wx_title_bar, this);

        RelativeLayout toBack = (RelativeLayout)view.findViewById(R.id.rl_back_arrow_container);
        toBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null){
                    mCallback.onBackClicked();
                }
            }
        });

        mNavTextView = (TextView)view.findViewById(R.id.tv_up_navigation);
    }

    public void setNavText(String navText){
        mNavTextView.setText(navText);
    }

    public void addRightView(View view){
        if (view == null){
            return;
        }
        ViewGroup parent = (ViewGroup)view.getParent();
        if (parent != null){
            parent.removeView(view);
        }

        LayoutParams lp = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(ALIGN_PARENT_RIGHT);
        lp.addRule(CENTER_VERTICAL);
        lp.setMargins(0, DimenUtil.dp2px(mContext, 8), DimenUtil.dp2px(mContext, 8), DimenUtil.dp2px(mContext, 8));
        view.setLayoutParams(lp);
        if (view instanceof TextView){
            view.setPadding(DimenUtil.dp2px(mContext, 9), DimenUtil.dp2px(mContext, 6), DimenUtil.dp2px(mContext, 9), DimenUtil.dp2px(mContext, 6));
            ((TextView) view).setGravity(Gravity.CENTER);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        }

        addView(view);
    }

    public interface ActionBarCallback {
        void onBackClicked();
    }
}
