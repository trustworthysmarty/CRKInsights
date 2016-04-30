package com.relsellglobal.crk.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.relsellglobal.crk.app.smartsync.SmartSyncTableTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anilkukreti on 10/12/15.
 */
public class CrkClientServicerSyncService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Sync service started");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new SmartSyncTableTask(null,2,CrkClientServicerSyncService.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new SmartSyncTableTask(null,2,CrkClientServicerSyncService.this).execute();
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
