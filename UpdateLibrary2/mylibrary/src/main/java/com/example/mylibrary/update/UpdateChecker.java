package com.example.mylibrary.update;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mylibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateChecker {

    private static String apkUrl;
    private static int apkCode;

    public static void checkForDialog(Context context, Activity activity, View view, int prg_circular, int textView, int deshabilitado) {
        if (context != null) {
            checkFirebase(context, activity, view, prg_circular, textView, deshabilitado);
        } else {
            Log.e("ASPAS", "The arg context is null");
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
                            break;
                        case "versionCode":
                            String aux = "" + snapshot1.getValue();
                            apkCode = Integer.parseInt(aux);
                            break;
                    }
                }
                Log.i("TAGS", "url: " + apkUrl);
                Log.i("TAGS", "versionCode: " + apkCode);

                String updateMessage = "";
                Log.i("APPD", "apkCode: " + apkCode);

                // Se extrae el código de versión de la app actual.
                int versionCode = com.example.mylibrary.update.AppUtils.getVersionCode(context);
                Log.i("APPD", "versioncode: " + versionCode);

                // Se comparan y se toma una decisión con respecto al resultado.
                if (apkCode > versionCode) {
                    showDialog(context, updateMessage, apkUrl, view, activity, prg_circular, textView, deshabilitado);
                } else {
                    Toast.makeText(context, context.getString(R.string.android_auto_update_toast_no_new_update), Toast.LENGTH_SHORT).show();
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
        com.example.mylibrary.update.UpdateDialog.show(context, content, apkUrl, view, activity, prg_circular, textView, deshabilitado);
    }
}
