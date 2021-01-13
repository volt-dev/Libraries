package com.example.library;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class CheckUpdateTask extends AsyncTask<Void, Void, Void> {

    private ProgressDialog dialog;
    private Context mContext;
    private int mType;
    private boolean mShowProgressDialog;
    private String JSONurl;
    private String apkUrl;
    private int apkCode;
    private Class<? extends Activity> ActivityToOpen;
//    private static final String url = com.example.library.Constants.UPDATE_URL;

    CheckUpdateTask(Context context, int type, boolean showProgressDialog, String JSONurl, final Class<? extends Activity> ActivityToOpen) {

        this.mContext = context;
        this.mType = type;
        this.mShowProgressDialog = showProgressDialog;
        this.JSONurl = JSONurl;
        this.ActivityToOpen = ActivityToOpen;
    }


    protected void onPreExecute() {
        if (mShowProgressDialog) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getString(R.string.android_auto_update_dialog_checking));
            dialog.show();
        }
    }


    @Override
    protected void onPostExecute(Void result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

//        if (!TextUtils.isEmpty()) {
        parseJson();
//        }
    }

    private void parseJson() {


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
                int versionCode = AppUtils.getVersionCode(mContext);
                Log.i("APPD", "versioncode: " + versionCode);

                // Se comparan y se toma una decisión con respecto al resultado.
                if (apkCode > versionCode) {
                    if (mType == Constants.TYPE_NOTIFICATION) {
                        new NotificationHelper(mContext).showNotification(updateMessage, apkUrl);
                    } else if (mType == Constants.TYPE_DIALOG) {
                        showDialog(mContext, updateMessage, apkUrl, ActivityToOpen);
                    }
                } else if (mShowProgressDialog) {
                    Toast.makeText(mContext, mContext.getString(R.string.android_auto_update_toast_no_new_update), Toast.LENGTH_SHORT).show();
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
    private void showDialog(Context context, String content, String apkUrl, final Class<? extends Activity> ActivityToOpen) {
//        UpdateDialog.show(context, content, apkUrl, ActivityToOpen);
    }


    @Override
    protected Void doInBackground(Void... args) {
        return null;
//        return com.example.library.HttpUtils.get(JSONurl);
    }
}
