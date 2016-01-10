package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.GridView;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.adapter.ImageLoaderAdapter;
import com.xuf.www.experiment.util.ToastUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2015/9/24.
 */
public class ImageLoaderActivity extends Activity {

    private GridView mGridView;

    private List<String> mImageDirPaths = new ArrayList<>();

    private int mMostPictureCount;

    private File mMostImageDir;

    private List<String> mImages = new ArrayList<>();

    private ImageLoaderAdapter mAdapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (String dirPath : mImageDirPaths){
                File dir = new File(dirPath);
                List<String> list = Arrays.asList(dir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg")){
                            return true;
                        }
                        return false;
                    }
                }));
                for (int i = 0; i < list.size(); i++){
                    list.set(i, dirPath + "/" + list.get(i));
                }
                mImages.addAll(list);
            }
            mAdapter = new ImageLoaderAdapter(getApplicationContext(), mImages);
            mGridView.setAdapter(mAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loader);
        mGridView = (GridView)findViewById(R.id.gv_images);
        _loadImage();
    }

    //扫描sd卡上图片最多的文件夹，并将其中的图片加载出来
    private void _loadImage(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            ToastUtil.showShort(this, "暂无外部存储");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver resolver = ImageLoaderActivity.this.getContentResolver();
                //query
                Cursor cursor = resolver.query(imageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                while (cursor.moveToNext()){
                    String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File imageDir = new File(imagePath).getParentFile();
                    String dirPath = imageDir.getAbsolutePath();

                    if (mImageDirPaths.contains(dirPath)){
                        continue;
                    } else {
                        mImageDirPaths.add(dirPath);
                    }

                    int pictureCount = imageDir.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")){
                                return true;
                            }
                            return false;
                        }
                    }).length;

                    if (pictureCount > mMostPictureCount){
                        mMostPictureCount = pictureCount;
                        mMostImageDir = imageDir;
                    }
                }
                cursor.close();
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }
}
