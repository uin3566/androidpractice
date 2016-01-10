package com.xuf.www.experiment.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.adapter.CommonAdapter;
import com.xuf.www.experiment.bean.ImageFolder;
import com.xuf.www.experiment.util.CommonViewHolder;
import com.xuf.www.experiment.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/29.
 */
public class ImageFolderPopupWindow extends FrameLayout {

    private List<ImageFolder> mImageFolders;

    private ItemClickListener mListener = null;

    private CommonAdapter<ImageFolder> mAdapter;

    private int mLastSelectedItem = 0;

    public ImageFolderPopupWindow(Context context) {
        this(context, null);
    }

    public ImageFolderPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        _initPopupWindow(context);
    }

    public void setData(List<ImageFolder> imageFolders){
        if (imageFolders != null) {
            mImageFolders = imageFolders;
            mAdapter.setDataList(imageFolders);
        }
    }

    public void setOnItemClickListener(ItemClickListener listener){
        if (listener != null) {
            mListener = listener;
        }
    }

    private void _initPopupWindow(Context context){
        View root = LayoutInflater.from(context).inflate(R.layout.popup_window_image_folder_list, this);
        ListView listView = (ListView)root.findViewById(R.id.lv_image_folder);
        mAdapter = new CommonAdapter<ImageFolder>(context, R.layout.item_image_folder, mImageFolders) {
            @Override
            public void fillView(final CommonViewHolder holder, final int position) {
                ImageFolder imageFolder = mImageFolders.get(position);

                //load image
                String imagePath = imageFolder.getFirstImagePath();
                ImageView imageView = holder.getView(R.id.iv_image_folder);
                ImageLoader.getInstance(3, ImageLoader.Type.TYPE_LIFO).loadImage(imagePath, imageView);

                //image folder name
                holder.setText(R.id.tv_folder_name, imageFolder.getFolderName());

                //image count
                if (position == 0){
                    holder.getView(R.id.tv_image_count).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.tv_image_count).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_image_count, imageFolder.getImageCount() + "张");
                }

                //image folder select tick
                if (position == mLastSelectedItem){
                    holder.getView(R.id.iv_tick).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.iv_tick).setVisibility(View.INVISIBLE);
                }

                RelativeLayout itemContainer = holder.getView(R.id.rl_image_folder_item_container);
                itemContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null){
                            mLastSelectedItem = position;
                            holder.getView(R.id.iv_tick).setVisibility(View.VISIBLE);
                            mListener.onItemClicked(position);
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        };
        listView.setAdapter(mAdapter);
    }

    public interface ItemClickListener{
        void onItemClicked(int position);
    }
}
