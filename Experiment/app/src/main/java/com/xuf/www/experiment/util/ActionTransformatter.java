package com.xuf.www.experiment.util;

/**
 * Created by dear33 on 2016/11/9.
 */
public class ActionTransformatter {

    public static String transformActionCode(int action) {
        String str;
        switch (action) {
            case 0:
                str = "ACTION_DOWN";
                break;
            case 1:
                str = "ACTION_UP";
                break;
            case 2:
                str = "ACTION_MOVE";
                break;
            case 3:
                str = "ACTION_CANCEL";
                break;
            default:
                str = "ACTION_" + action;
                break;
        }
        return str;
    }
}
