package com.kidzie.jeff.restclientmanager;

import android.util.Log;

import com.kidzie.jeff.restclientmanager.config.Global;

/**
 * Created by ${Jeffry} on 03-Jun-15.
 */

public class Logging {

    public static enum LOGTYPE {
        debug,
        error,
        info,
        verbose,
        warn
    }

    public static void setLog(Logging.LOGTYPE logType, String tag, String additionalMessage, Exception ex) {
        String message = "";

        if (Global.DEBUG_MODE == true) {
            if (ex == null) {
                message = "";
            } else {
                message = ex.getMessage() + " AT " + ex.getClass().getName();
            }

            if (logType == null) {
                logType = Logging.LOGTYPE.info;
            }
            if (tag.trim().equals("")) {
                tag = Global.TAG;
            }

            if (ex == null) {
                ex = new Exception("");
            }

            try {
                switch (logType) {
                    case debug:
                        Log.d(tag, additionalMessage + "\r\n" + message);
                        break;
                    case error:
                        Log.e(tag, additionalMessage + "\r\n" + message);
                        break;
                    case info:
                        Log.i(tag, additionalMessage + "\r\n" + message);
                        break;
                    case verbose:
                        Log.v(tag, additionalMessage + "\r\n" + message);
                        break;
                    case warn:
                        Log.w(tag, additionalMessage + "\r\n" + message);
                        break;
                }
            } catch (Exception exs) {
                Log.e(Global.TAG, exs.getClass().getName() + ": " + exs.getMessage());
            }
        }
    }
}
