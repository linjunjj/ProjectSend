package com.linjun.projectsend.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;

import com.linjun.projectsend.ui.main.MainActivity;


public class TestService extends Service {
    private int i=0;
    private boolean isStop;
    public TestService() {
        System.out.println("实例化downService");
    }

    public void onCreate() {

        System.out.println("downService->onCreate");
        new Thread(new DownThread()).start();//启动线程
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        isStop=true;
        System.out.println("downService->onDestroy");
    }
    //每个一秒发一个通知
    class DownThread implements Runnable{

        public void run() {
            while(!isStop){
                i++;
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = i;
//                    MainActivity.handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isStop() {
        return isStop;
    }
    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    public IBinder onBind(Intent intent) {
        System.out.println("onBind");

        return echoBinder;
    }
    private CountBinder echoBinder = new CountBinder();

    public class CountBinder extends Binder {
        public TestService getService(){
            return TestService.this;
        }
    }
}
