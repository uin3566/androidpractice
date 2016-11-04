package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xuf.www.experiment.R;

/**
 * Created by Administrator on 2016/9/8.
 */
public class GlideActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide2);
        final ImageView imageView = (ImageView) findViewById(R.id.iv_glide);
        String url = "https://raw.githubusercontent.com/uin3566/Dota2Helper/master/screenshots/screenshot.jpg";
        Glide.with(this).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                imageView.setImageBitmap(resource);
            }
        });
    }
}
