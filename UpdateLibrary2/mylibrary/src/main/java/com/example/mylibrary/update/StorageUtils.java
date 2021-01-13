package com.example.mylibrary.update;

import android.content.Context;
import android.util.Log;

import java.io.File;

public final class StorageUtils {

    /**
     * Obtener el directorio de caché de la aplicación
     */
    public static File getCacheDirectory(Context context) {
        File appCacheDir = context.getCacheDir();
        if (appCacheDir == null) {
            Log.w("StorageUtils", "Can't define system cache directory! The app should be re-installed.");
        }
        return appCacheDir;
    }

}
