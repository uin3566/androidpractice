package com.xuf.www.experiment.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2015/11/6.
 */
public class DiskLruCacheHelper {

    private static final int VALUE_COUNT = 1;
    private static final int MAX_SIZE = 10 * 1024 * 1024;
    private static final String DISK_CACHE_DIR = "disk_cache_dir";

    private DiskLruCache mDiskLruCache;
    private DiskLruCache.Editor mCurEditor;
    private Context mContext;

    public DiskLruCacheHelper(Context context){
        _initHelper(context, DISK_CACHE_DIR, MAX_SIZE, VALUE_COUNT);
    }

    public DiskLruCacheHelper(Context context, String dirName){
        _initHelper(context, dirName, MAX_SIZE, VALUE_COUNT);
    }

    public DiskLruCacheHelper(Context context, String dirName, long maxSize) {
        _initHelper(context, dirName, maxSize, VALUE_COUNT);
    }

    private void _initHelper(Context context, String dirName, long maxSize, int valueCount){
        mContext = context;
        try {
            mDiskLruCache = DiskLruCache.open(_getDiskCacheDir(dirName), _getAppVersion(), valueCount, maxSize);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /*byte[] 读写*/
    public void put(String key, byte[] bytes){
        String md5 = _keyToMd5(key);
        OutputStream out = _getOutputStream(md5);
        if (out == null){
            return;
        }

        try {
            out.write(bytes);
            flush();
            mCurEditor.commit();
        } catch (IOException e1){
            e1.printStackTrace();
            try {
                mCurEditor.abort();
            } catch (IOException e2){
                e2.printStackTrace();
            }
        } finally {
            try {
                out.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    public byte[] getBytes(String key){
        String md5 = _keyToMd5(key);
        InputStream in = _getInputStream(md5);
        if (in == null){
            return null;
        }

        byte[] ret = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[256];
            while (in.read(buf) != -1){
                out.write(buf);
            }
            ret = out.toByteArray();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        return ret;
    }

    /*Bitmap 读写*/
    public void put(String key, Bitmap bitmap){
        put(key, _Bitmap2Bytes(bitmap));
    }

    public Bitmap getBitmap(String key){
        byte[] bytes = getBytes(key);
        if (bytes != null){
            return _Bytes2Bitmap(bytes);
        }

        return null;
    }

    /*String 读写*/
    public void put(String key, String value){
        String md5 = _keyToMd5(key);
        OutputStream out = _getOutputStream(md5);
        if (out == null){
            return;
        }

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        try {
            writer.write(value);
            writer.flush();
            flush();
            mCurEditor.commit();
        } catch (IOException e1){
            e1.printStackTrace();
            try{
                mCurEditor.abort();
            } catch (IOException e2){
                e2.printStackTrace();
            }
        } finally {
            try {
                writer.close();
            } catch (IOException e3){
                e3.printStackTrace();
            }
        }
    }

    public String getString(String key){
        String md5 = _keyToMd5(key);
        InputStream in = _getInputStream(md5);
        if (in == null) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        return null;
    }

    /*JSONObject 读写*/
    public void put(String key, JSONObject object){
        put(key, object.toString());
    }

    public JSONObject getJSONObject(String key){
        String jsonString = getString(key);
        if (jsonString != null){
            try {
                return new JSONObject(jsonString);
            } catch (JSONException e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /*JSONArray 读写*/
    public void put(String key, JSONArray jsonArray){
        put(key, jsonArray.toString());
    }

    public JSONArray getJSONArray(String key){
        String jsonString = getString(key);
        if (jsonString != null){
            try {
                return new JSONArray(jsonString);
            } catch (JSONException e){
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    /*Object 读写*/
    public void put(String key, Serializable object){
        String md5 = _keyToMd5(key);
        OutputStream out = _getOutputStream(md5);
        if (out == null){
            return;
        }

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(out);
            oos.writeObject(object);
            oos.flush();
            flush();
            mCurEditor.commit();
        } catch (IOException e1){
            e1.printStackTrace();
            try {
                mCurEditor.abort();
            } catch (IOException e2){
                e2.printStackTrace();
            }
        } finally {
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e3){
                    e3.printStackTrace();
                }
            }
        }
    }

    public Object getObject(String key){
        String md5 = _keyToMd5(key);
        InputStream is = _getInputStream(md5);
        if (is == null){
            return null;
        }

        Object object = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(is);
            object = ois.readObject();
        } catch (ClassNotFoundException | IOException e){
            e.printStackTrace();
        } finally {
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return object;
    }

    public long getSize() {
        if (mDiskLruCache != null) {
            return mDiskLruCache.size();
        }
        return -1;
    }

    public void flush(){
        if (mDiskLruCache != null){
            try {
                mDiskLruCache.flush();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void close(){
        if (mDiskLruCache != null){
            try {
                mDiskLruCache.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private InputStream _getInputStream(String md5){
        try{
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(md5);
            if (snapshot != null){
                return snapshot.getInputStream(0);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    private OutputStream _getOutputStream(String md5){
        try {
            mCurEditor = mDiskLruCache.edit(md5);
            if (mCurEditor != null){
                return mCurEditor.newOutputStream(0);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    private byte[] _Bitmap2Bytes(Bitmap bitmap){
        if (bitmap == null){
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }

    private Bitmap _Bytes2Bitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private int _getAppVersion() {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        return 1;
    }

    private File _getDiskCacheDir(String dirName){
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()){
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }

        File cacheDir = new File(cachePath + File.separator + dirName);
        if (!cacheDir.exists()){
            cacheDir.mkdirs();
        }

        return cacheDir;
    }

    private String _keyToMd5(String key){
        String md5;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(key.getBytes());
            md5 = _bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e){
            md5 = String.valueOf(key.hashCode());
        }

        return md5;
    }

    private String _bytesToHexString(byte[] bytes){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++){
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1){
                builder.append('0');
            }
            builder.append(hex);
        }
        return builder.toString();
    }
}
