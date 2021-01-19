package com.example.library;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateChecker {

    private static String apkUrl;
    private static int versionCode;

    public static void checkForDialog(Context context, Activity activity, View view, int prg_circular, int textView, int deshabilitado) {
        if (context != null) {
//            new com.example.library.CheckUpdateTask(context, com.example.library.Constants.TYPE_DIALOG, true, JSONurl, ActivityToOpen).execute();
            checkFirebase(context, activity, view, prg_circular, textView, deshabilitado);
        } else {
            Log.e(com.example.library.Constants.TAG, "The arg context is null");
        }
    }

    public static void checkFirebase(Context context, Activity activity, View view, int prg_circular, int textView, int deshabilitado) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    switch (snapshot1.getKey()) {
                        case "url":
                            apkUrl = (String) snapshot1.getValue();
                            Log.i("TAGS", "url: " + apkUrl);
                            break;
                        case "versionCode":
                            String aux = "" + snapshot1.getValue();
                            if (!TextUtils.isEmpty(aux)) {
                                versionCode = Integer.parseInt(aux);
                            } else {
                                versionCode = 0;
                            }
                            Log.i("TAGS", "versionCode: " + versionCode);
                            break;
                    }
                }

                String updateMessage = "";

                // Se extrae el código de versión de la app actual.
                int versionCodeOld = com.example.library.AppUtils.getVersionCode(context);
                Log.i("TAGS", "versioncodeOld: " + versionCodeOld);
                Log.i("TAGS", "versionCode > versionCodeOld: " + (versionCode > versionCodeOld));
                // Se comparan y se toma una decisión con respecto al resultado.
                if (versionCode > versionCodeOld && !TextUtils.isEmpty(apkUrl)) {
                    showDialog(context, updateMessage, apkUrl, view, activity, prg_circular, textView, deshabilitado);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Show dialog
     */
    private static void showDialog(Context context, String content, String apkUrl, View view, Activity activity, int prg_circular, int textView, int deshabilitado) {
        com.example.library.UpdateDialog.show(context, content, apkUrl, view, activity, prg_circular, textView, deshabilitado);
    }
}
