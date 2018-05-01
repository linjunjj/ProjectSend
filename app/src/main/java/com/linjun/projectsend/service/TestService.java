package com.linjun.projectsend.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class TestService extends Service{

    private boolean connecting = false;
    private Callback callback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    public class Binder extends android.os.Binder {
        public TestService getService() {
            return TestService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connecting = true;
        new Thread(new Runnable() {

            @Override
            public void run() {
                int i = 0;
                while (connecting == true) {
                    i++;
                    if (callback != null) {
                        callback.onDataChange(i + "");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public static interface Callback {
        void onDataChange(String data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connecting = false;
    }
}
