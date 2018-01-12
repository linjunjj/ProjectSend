package com.linjun.projectsend.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by linjun on 2018/1/12.
 */

public class HeartbeatTimer {
   private Timer timer;
   private TimerTask timerTask;
   private  OnScheduleLinstener linstener;
   public HeartbeatTimer(){
       timer=new Timer();
   }
 public  void startTimer(long delay,long period){
       timerTask=new TimerTask() {
           @Override
           public void run() {
               if (linstener!=null){
                   linstener.onSchedule();
               }
           }
       };
       timer.schedule(timerTask,delay,period);

 }

public  void exit(){
     if (timer!=null){
         timer.cancel();
     }
     if (timerTask!=null){
         timerTask.cancel();
     }
}
public  interface  OnScheduleLinstener{
     void onSchedule();
}

public  void setOnScheduleListener(OnScheduleLinstener listener){
     this.linstener=listener;
}

}
