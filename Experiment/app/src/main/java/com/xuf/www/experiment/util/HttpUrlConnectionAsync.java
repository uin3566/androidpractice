package com.xuf.www.experiment.util;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/10/28.
 */
public class HttpUrlConnectionAsync {

    private static final int TIME_OUT_MILLIS = 5000;

    private MyHandler mMyHandler = new MyHandler(this);
    private QueryDataCallback mCallback;

    private static class MyHandler extends Handler {
        private final WeakReference<HttpUrlConnectionAsync> mReference;

        public MyHandler(HttpUrlConnectionAsync wrapper) {
            mReference = new WeakReference<>(wrapper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            StringBuilder builder = (StringBuilder) msg.obj;
            String result = builder.toString();
            mReference.get().mCallback.onQueryDataResult(true, result);
        }
    }

    public void setCallback(QueryDataCallback callback) {
        mCallback = callback;
    }

    public void queryStringData(String urlPath) throws Exception {
        URL url = new URL(urlPath);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        _setUrlConnection(true, conn);

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                try {
                    inputStream = conn.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String strLine;
                    StringBuilder strData = new StringBuilder();
                    while ((strLine = bufferedReader.readLine()) != null) {
                        strData.append(strLine);
                    }
                    Message msg = new Message();
                    msg.obj = strData;
                    mMyHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    mCallback.onQueryDataResult(false, null);
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.disconnect();
                }
            }
        }).start();
    }

    private void _setUrlConnection(boolean isGet, HttpURLConnection conn) throws Exception {
        if (isGet) {
            conn.setRequestMethod("GET");
        } else {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
        }
        conn.setConnectTimeout(TIME_OUT_MILLIS);
    }

    public interface QueryDataCallback {
        void onQueryDataResult(boolean success, String result);
    }
}
