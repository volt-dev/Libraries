package com.example.library;

import android.content.Context;
import android.util.Log;

public class UpdateChecker {

    public static void checkForDialog(Context context, String JSONurl) {
        if (context != null) {
            new com.example.library.CheckUpdateTask(context, com.example.library.Constants.TYPE_DIALOG, true, JSONurl).execute();
        } else {
            Log.e(com.example.library.Constants.TAG, "The arg context is null");
        }
    }

    public static void checkForNotification(Context context, String JSONurl) {
        if (context != null) {
            new com.example.library.CheckUpdateTask(context, com.example.library.Constants.TYPE_NOTIFICATION, false, JSONurl).execute();
        } else {
            Log.e(com.example.library.Constants.TAG, "The arg context is null");
        }

    }
}
