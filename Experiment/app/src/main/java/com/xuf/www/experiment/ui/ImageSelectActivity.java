package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.adapter.CommonAdapter;
import com.xuf.www.experiment.bean.ImageFolder;
import com.xuf.www.experiment.util.CommonViewHolder;
import com.xuf.www.experiment.util.ImageLoader;
import com.xuf.www.experiment.util.ToastUtil;
import com.xuf.www.experiment.widget.FinishButton;
import com.xuf.www.experiment.widget.WxActionBar;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2015/9/28.
 */
public class ImageSelectActivity extends Activity implements ImageFolderPopupWindow.ItemClickListener, WxActionBar.ActionBarCallback{

    private List<ImageFolder> mImageFolderList = new ArrayList<>();

    private List<String> mFolderPathList = new ArrayList<>();

    private GridView mGridView;

    private GridAdapter mGridAdapter;

    private ProgressDialog mProgressDialog;

    private FinishButton mFinishButton;

    private List<String> mImageList = new ArrayList<>();

    private ImageFolderPopupWindow mPopupWindow;

    private View mBackground;

    private boolean mPopupDataSeted = false;

    private final String[] imageTypes = {".jpg", ".jpeg", ".png", ".ico"};

    private static final int MAX_SELECT_IMAGE_COUNT = 9;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mProgressDialog.dismiss();
            if (mFolderPathList.isEmpty()){
                ToastUtil.showShort(ImageSelectActivity.this, "没有扫描到图片");
                return;
            }

            _setPopupWindowData();
            for (String folderPath : mFolderPathList){
                List<String> imageList = _getImagesFromFolder(folderPath);
                mImageList.addAll(imageList);
            }
            mGridAdapter = new GridAdapter(ImageSelectActivity.this, R.layout.item_image_selectable, mImageList);
            mGridView.setAdapter(mGridAdapter);
        }
    };

    private List<String> _getImagesFromFolder(String folderPath){
        return _getFilesFromFolder(folderPath, true, imageTypes);
    }

    private List<String> _getFilesFromFolder(String folderPath, boolean getAbsolutePath, final String[] endsFilters){
        final List<String> fileList;
        File folder = new File(folderPath);
        fileList = Arrays.asList(folder.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                for (int i = 0; i < endsFilters.length; i++){
                    if (filename.endsWith(endsFilters[i])){
                        return true;
                    }
                }
                return false;
            }
        }));
        if (getAbsolutePath){
            for (int i = 0; i < fileList.size(); i++){
                fileList.set(i, folderPath + "/" + fileList.get(i));
            }
        }

        return fileList;
    }

    @Override
    public void onItemClicked(int position) {
        if (position == 0){
            mGridAdapter.setDataList(mImageList);
            _showPopupWindow(false);
        } else {
            ImageFolder folder = mImageFolderList.get(position);
            String folderPath = folder.getFolderPath();
            List<String> imageList = _getImagesFromFolder(folderPath);
            mGridAdapter.setDataList(imageList);
            _showPopupWindow(false);
        }
        mGridAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        mGridView = (GridView)findViewById(R.id.gv_images);
        _loadImage();

        mPopupWindow = (ImageFolderPopupWindow)findViewById(R.id.popup_window);
        mPopupWindow.setOnItemClickListener(this);

        mBackground = findViewById(R.id.v_background);
        mBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShown()){
                    _showPopupWindow(false);
                }
            }
        });

        final RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.rl_image_class_container);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPopupDataSeted){
                    return;
                }
                if (mPopupWindow.isShown()){
                    _showPopupWindow(false);
                } else {
                    _showPopupWindow(true);
                }
            }
        });

        WxActionBar wxActionBar = (WxActionBar)findViewById(R.id.wx_action_bar);
        wxActionBar.setActionBarCallback(this);
        wxActionBar.setNavText("图片");
        mFinishButton = new FinishButton(this, MAX_SELECT_IMAGE_COUNT);
        wxActionBar.addRightView(mFinishButton);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    private void _showPopupWindow(boolean show){
        Animation translateAnimation, alphaAnimation;
        if (show){
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            mPopupWindow.setVisibility(View.VISIBLE);

            alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            mBackground.setVisibility(View.VISIBLE);
        } else {
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            mPopupWindow.setVisibility(View.INVISIBLE);

            alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            mBackground.setVisibility(View.INVISIBLE);
        }
        translateAnimation.setDuration(200);
        translateAnimation.startNow();
        mPopupWindow.setAnimation(translateAnimation);

        alphaAnimation.setDuration(200);
        alphaAnimation.startNow();
        mBackground.setAnimation(alphaAnimation);
    }

    private void _setPopupWindowData(){
        //添加"所有图片"文件夹
        ImageFolder allImageFolder = new ImageFolder();
        allImageFolder.setFirstImagePath(mImageFolderList.get(0).getFirstImagePath());
        allImageFolder.setFolderName("所有图片");
        allImageFolder.setFolderPath(null);
        allImageFolder.setImageCount(0);
        mImageFolderList.add(0, allImageFolder);

        mPopupWindow.setData(mImageFolderList);
        mPopupDataSeted = true;
    }

    private void _loadImage(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            ToastUtil.showShort(this, "暂无外部存储");
            return;
        }

        mProgressDialog = ProgressDialog.show(this, null, "请稍后...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver resolver = ImageSelectActivity.this.getContentResolver();

                Cursor cursor = resolver.query(imgUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                while (cursor.moveToNext()){
                    String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File imageDir = new File(imagePath).getParentFile();
                    String dirPath = imageDir.getAbsolutePath();

                    if (mFolderPathList.contains(dirPath)){
                        continue;
                    } else {
                        ImageFolder folder = new ImageFolder();
                        folder.setFirstImagePath(imagePath);
                        folder.setFolderPath(dirPath);
                        String dirName = imageDir.getName();
                        folder.setFolderName(dirName);
                        int imageCount = imageDir.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                for (String imageType : imageTypes) {
                                    if (filename.endsWith(imageType)) {
                                        return true;
                                    }
                                }
                                return false;
                            }
                        }).length;
                        folder.setImageCount(imageCount);
                        mImageFolderList.add(folder);
                        mFolderPathList.add(dirPath);
                    }
                }
                cursor.close();
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private class GridAdapter extends CommonAdapter<String>{
        private List<String> mSelectedImages = new ArrayList<>();

        private GridAdapter(Context context, int layoutId, List<String> dataList) {
            super(context, layoutId, dataList);
        }

        public void setDataList(List<String> dataList){
            super.setDataList(dataList);
        }

        @Override
        public void fillView(CommonViewHolder holder, int position) {
            final String imgPath = getItem(position);
            final ImageView imageView = holder.getView(R.id.iv_image);
            holder.setImageResource(R.id.iv_image, R.mipmap.img_default);
            ImageLoader.getInstance().loadImage(imgPath, imageView);

            final ImageView selectView = holder.getView(R.id.iv_select);
            if (mSelectedImages.contains(imgPath)){
                selectView.setImageResource(R.mipmap.image_selected);
                imageView.setColorFilter(Color.parseColor("#77000000"));
            } else {
                selectView.setImageResource(R.mipmap.image_unselected);
                imageView.setColorFilter(null);
            }

            RelativeLayout selectViewContainer = holder.getView(R.id.rl_select_container);
            selectViewContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectedImages.contains(imgPath)){
                        selectView.setImageResource(R.mipmap.image_unselected);
                        imageView.setColorFilter(null);
                        mSelectedImages.remove(imgPath);
                    } else {
                        if (mSelectedImages.size() == MAX_SELECT_IMAGE_COUNT){
                            String msg = String.format("你最多只能选择%d张图片", MAX_SELECT_IMAGE_COUNT);
                            ToastUtil.showShort(ImageSelectActivity.this, msg);
                            return;
                        }
                        selectView.setImageResource(R.mipmap.image_selected);
                        imageView.setColorFilter(Color.parseColor("#77000000"));
                        mSelectedImages.add(imgPath);
                    }
                    mFinishButton.updateButton(mSelectedImages.size());
                }
            });
        }
    }
}
