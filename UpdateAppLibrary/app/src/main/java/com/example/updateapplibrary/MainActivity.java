package com.example.updateapplibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.library.AppUtils;
import com.example.library.UpdateChecker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        UpdateChecker.checkForDialog(MainActivity.this, this,
//                findViewById(R.id.main_l).getRootView(), 0, 0, 0);
//        String mensaje = "Información de la versión actual: versionName = " +
//                AppUtils.getVersionName(this) +
//                "\nversionCode = " +
//                AppUtils.getVersionCode(this);
//        Log.i("TAGS", mensaje);
    }
}