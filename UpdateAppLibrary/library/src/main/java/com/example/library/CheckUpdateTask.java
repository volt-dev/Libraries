package com.example.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

class CheckUpdateTask extends AsyncTask<Void, Void, String> {

    private ProgressDialog dialog;
    private Context mContext;
    private int mType;
    private boolean mShowProgressDialog;
    private static final String url = com.example.library.Constants.UPDATE_URL;

    CheckUpdateTask(Context context, int type, boolean showProgressDialog) {

        this.mContext = context;
        this.mType = type;
        this.mShowProgressDialog = showProgressDialog;

    }


    protected void onPreExecute() {
        if (mShowProgressDialog) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getString(R.string.android_auto_update_dialog_checking));
            dialog.show();
        }
    }


    @Override
    protected void onPostExecute(String result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (!TextUtils.isEmpty(result)) {
            parseJson(result);
        }
    }

    private void parseJson(String result) {
        try {
            // Se obtiene el String en el proceso doInBackground y se genera un nuevo JSON.
            JSONObject obj = new JSONObject(result);
            // Extren los Strings del JSON.
            String updateMessage = obj.getString(com.example.library.Constants.APK_UPDATE_CONTENT);
            Log.i("APPD", "updateMessage: "+obj.getString(com.example.library.Constants.APK_UPDATE_CONTENT));

            String apkUrl = obj.getString(com.example.library.Constants.APK_DOWNLOAD_URL);
            Log.i("APPD", "apkUrl: " +obj.getString(com.example.library.Constants.APK_UPDATE_CONTENT));

            // Se extrae el código de versión del JSON.
            int apkCode = obj.getInt(com.example.library.Constants.APK_VERSION_CODE);
            Log.i("APPD", "apkCode: "+apkCode);
            // Se extrae el código de versión de la app actual.
            int versionCode = com.example.library.AppUtils.getVersionCode(mContext);
            Log.i("APPD", "versioncode: "+versionCode);

            // Se comparan y se toma una decisión con respecto al resultado.
            if (apkCode > versionCode) {
                if (mType == com.example.library.Constants.TYPE_NOTIFICATION) {
                    new com.example.library.NotificationHelper(mContext).showNotification(updateMessage, apkUrl);
                } else if (mType == com.example.library.Constants.TYPE_DIALOG) {
                    showDialog(mContext, updateMessage, apkUrl);
                }
            } else if (mShowProgressDialog) {
                Toast.makeText(mContext, mContext.getString(R.string.android_auto_update_toast_no_new_update), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(com.example.library.Constants.TAG, "parse json error");
        }
    }


    /**
     * Show dialog
     */
    private void showDialog(Context context, String content, String apkUrl) {
        UpdateDialog.show(context, content, apkUrl);
    }


    @Override
    protected String doInBackground(Void... args) {
        return com.example.library.HttpUtils.get(url);
    }
}
