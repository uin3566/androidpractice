package com.xuf.www.experiment.ipc;

/**
 * Created by lenov0 on 2015/11/12.
 */
public class User {

    public int userId;
    public String userName;
    public boolean isMale;

    @Override
    public String toString() {
        return String.format(
                "User:{userId:%s, userName:%s, isMale:%s}",
                userId, userName, isMale);
    }
}
