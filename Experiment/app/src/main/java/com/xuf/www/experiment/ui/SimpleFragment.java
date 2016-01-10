package com.xuf.www.experiment.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuf.www.experiment.R;

/**
 * Created by Administrator on 2015/10/9.
 */
public class SimpleFragment extends Fragment {

    private String mContent = "default";

    public static SimpleFragment getInstance(String content){
        SimpleFragment fragment = new SimpleFragment();
        fragment.setContent(content);

        return fragment;
    }

    public void setContent(String content){
        mContent = content;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple, container, false);

        TextView textView = (TextView)view.findViewById(R.id.tv_content);
        textView.setText(mContent);

        return view;
    }
}
