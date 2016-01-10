package com.xuf.www.experiment.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/4.
 */
public class ChatMessage implements Serializable{

    private boolean mIsComeMsg;
    private String mMessageContent;

    public boolean isComeMsg() {
        return mIsComeMsg;
    }

    public void setComeMsg(boolean isComeMsg) {
        mIsComeMsg = isComeMsg;
    }

    public String getMessageContent() {
        return mMessageContent;
    }

    public void setMessageContent(String messageContent) {
        mMessageContent = messageContent;
    }
}
