package com.example.mylibrary.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import com.example.mylibrary.R;

import java.lang.ref.SoftReference;

public class UpdateDialog {

    public static SoftReference<Activity> sfr_activity;
    public static SoftReference<View> sfr_view;
    private static int prg_id, txtv_id, deshb_id;

    static void show(final Context context, String content, final String downloadUrl, View view, Activity activity, int prg_circular, int textView, int deshabilitado) {
        sfr_activity = new SoftReference<Activity>(activity);
        sfr_view = new SoftReference<View>(view);
        prg_id = prg_circular;
        txtv_id = textView;
        deshb_id = deshabilitado;
        if (isContextValid(context)) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.android_auto_update_dialog_title)
                    .setMessage(content)
                    .setPositiveButton(R.string.android_auto_update_dialog_btn_download, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            goToDownload(context, downloadUrl);
                            lockScreen(true);
                        }
                    })
                    .setNegativeButton(R.string.android_auto_update_dialog_btn_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    public static void lockScreen(boolean lock) {
        sfr_activity.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lock) {
                    sfr_activity.get().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    sfr_view.get().findViewById(prg_id).setVisibility(View.VISIBLE);
                    sfr_view.get().findViewById(txtv_id).setVisibility(View.VISIBLE);
                    sfr_view.get().findViewById(deshb_id).setVisibility(View.VISIBLE);
                } else {
                    //volver
                    sfr_activity.get().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    sfr_view.get().findViewById(prg_id).setVisibility(View.GONE);
                    sfr_view.get().findViewById(txtv_id).setVisibility(View.GONE);
                    sfr_view.get().findViewById(deshb_id).setVisibility(View.GONE);
                }
            }
        });
    }

    private static boolean isContextValid(Context context) {
        return context instanceof Activity && !((Activity) context).isFinishing();
    }

    private static void goToDownload(Context context, String downloadUrl) {
        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra("url", downloadUrl);
        context.startService(intent);
    }
}
