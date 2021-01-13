package com.example.library;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class UpdateChecker {

    public static void checkForDialog(Context context, String JSONurl, final Class<? extends Activity> ActivityToOpen) {
        if (context != null) {
            new com.example.library.CheckUpdateTask(context, com.example.library.Constants.TYPE_DIALOG, true, JSONurl, ActivityToOpen).execute();
        } else {
            Log.e(com.example.library.Constants.TAG, "The arg context is null");
        }
    }
}
