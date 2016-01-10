package com.xuf.www.experiment.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by Administrator on 2015/9/24.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    private static ImageLoader mInstance = null;

    private LruCache<String, Bitmap> mLruCache;

    private ExecutorService mThreadPool;

    private Handler mUIHandler = null;

    private volatile Semaphore mInitSemaphore = new Semaphore(1);

    private Handler mLooperHandler;

    private volatile Semaphore mPoolSemaphore;

    private LinkedList<Runnable> mTasks;

    private Type mType = Type.TYPE_LIFO;

    public enum Type {
        TYPE_LIFO,
        TYPE_FIFO
    }

    private class ImgBean {
        String mImgPath;
        ImageView mImageView;
        Bitmap mBitmap;
    }

    private class ImgSize {
        int mWidth;
        int mHeight;
    }

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(1, Type.TYPE_LIFO);
                }
            }
        }
        return mInstance;
    }

    public static ImageLoader getInstance(int threadCount, Type type) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    private ImageLoader(int threadCount, Type type) {
        _init(threadCount, type);
    }

    private void _init(int threadCount, Type type) {
        try {
            mInitSemaphore.acquire();
        } catch (InterruptedException e) {
            Log.e(TAG, "acquire mInitSemaphore cause InterruptedException");
        }
        mType = type;
        Thread looperThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mLooperHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        try {
                            mPoolSemaphore.acquire();
                        } catch (InterruptedException e) {
                            Log.e(TAG, "acquire mPoolSemaphore cause InterruptedException");
                        }

                        mThreadPool.execute(_getTask());
                    }
                };
                mInitSemaphore.release();
                Looper.loop();
            }
        };
        looperThread.start();

        long maxMemory = Runtime.getRuntime().maxMemory();
        long cacheSize = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>((int) cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
        mThreadPool = threadCount <= 0 ? Executors.newFixedThreadPool(1) : Executors.newFixedThreadPool(threadCount);
        mPoolSemaphore = threadCount <= 0 ? new Semaphore(1) : new Semaphore(threadCount);
        mTasks = new LinkedList<>();
        mType = type == null ? Type.TYPE_LIFO : type;
    }

    private synchronized void _addTask(Runnable task) {
        try {
            if (mLooperHandler == null) {
                mInitSemaphore.acquire();
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "acquire mInitSemaphore cause InterruptedException");
        }
        mTasks.add(task);
        mLooperHandler.sendEmptyMessage(0);
    }

    private Runnable _getTask() {
        if (mType == Type.TYPE_LIFO) {
            return mTasks.removeLast();
        } else if (mType == Type.TYPE_FIFO) {
            return mTasks.removeFirst();
        }

        return null;
    }

    public void loadImage(final String imgPath, final ImageView imageView) {
        imageView.setTag(imgPath);
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    ImgBean bean = (ImgBean) msg.obj;
                    ImageView imageView = bean.mImageView;
                    Bitmap bitmap = bean.mBitmap;
                    String path = bean.mImgPath;
                    if (imageView.getTag().toString().equals(path)) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            };
        }

        Bitmap bitmap = mLruCache.get(imgPath);
        if (bitmap != null) {
            ImgBean bean = new ImgBean();
            bean.mBitmap = bitmap;
            bean.mImgPath = imgPath;
            bean.mImageView = imageView;
            Message msg = Message.obtain();
            msg.obj = bean;
            mUIHandler.sendMessage(msg);
        } else {
            _addTask(new Runnable() {
                @Override
                public void run() {
                    ImgSize imgSize = _getImgViewSize(imageView);
                    int reqWidth = imgSize.mWidth;
                    int reqHeight = imgSize.mHeight;
                    Bitmap bp = _decodeSampledBitmapFromResource(imgPath, reqWidth, reqHeight);
                    if (bp != null){
                        mLruCache.put(imgPath, bp);
                    }
                    ImgBean bean = new ImgBean();
                    bean.mBitmap = bp;
                    bean.mImgPath = imgPath;
                    bean.mImageView = imageView;
                    Message msg = Message.obtain();
                    msg.obj = bean;
                    mUIHandler.sendMessage(msg);
                    mPoolSemaphore.release();
                }
            });
        }
    }

    private Bitmap _decodeSampledBitmapFromResource(String pathName, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = _calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

        return bitmap;
    }

    private int _calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的宽度
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth && height > reqHeight) {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    private ImgSize _getImgViewSize(ImageView imageView) {
        ImgSize imgSize = new ImgSize();
        final DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        final ViewGroup.LayoutParams params = imageView.getLayoutParams();

        int width = params.width == ViewGroup.LayoutParams.WRAP_CONTENT ? 0 : imageView
                .getWidth(); // Get actual image width
        if (width <= 0)
            width = params.width; // Get layout width parameter
        if (width <= 0)
            width = _getImageViewFieldValue(imageView, "mMaxWidth");
        if (width <= 0)
            width = displayMetrics.widthPixels;

        int height = params.height == ViewGroup.LayoutParams.WRAP_CONTENT ? 0 : imageView
                .getHeight(); // Get actual image height
        if (height <= 0)
            height = params.height; // Get layout height parameter
        if (height <= 0)
            height = _getImageViewFieldValue(imageView, "mMaxHeight");
        if (height <= 0)
            height = displayMetrics.heightPixels;

        imgSize.mWidth = width;
        imgSize.mHeight = height;
        return imgSize;
    }

    private static int _getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;

                Log.e("TAG", value + "");
            }
        } catch (Exception e) {
        }
        return value;
    }
}
