package tool;

import com.teamdev.jxbrowser.chromium.Browser;

import java.util.Calendar;
import java.util.Date;

public class Monitor implements  Runnable{

        long stopTime=10;
        Browser browser=null;
     public Monitor(){

     }
     public Monitor(long time){
          stopTime=time;
     }

    public Monitor(long time, Browser browser){
        this.browser=browser;
        stopTime=time;
    }



   public  static   String getTime(){
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改   对时间进行加减操作等
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        System.out.println(year + "/" + month + "/" + date + " " +hour + ":" +minute + ":" + second);
        return year + "/" + month + "/" + date + " " +hour + ":" +minute + ":" + second;
    }


    public static String getSomeDay(Date date, int day){
                Calendar calendar = Calendar.getInstance();
                 calendar.setTime(date);
                calendar.add(Calendar.DATE, day);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayo = calendar.get(Calendar.DATE);

                return year + "-" + (month+1) + "-" + dayo;
        }

    public  static   int getTimeDay(){
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改   对时间进行加减操作等
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        return date;
    }

     /**
      * When an object implementing interface <code>Runnable</code> is used
      * to create a thread, starting the thread causes the object's
      * <code>run</code> method to be called in that separately executing
      * thread.
      * <p>
      * The general contract of the method <code>run</code> is that it may
      * take any action whatsoever.
      *
      * @see Thread#run()
      */
     @Override
     public void run() {
          while (true){
               try {
                    stopTime--;
                    if(stopTime<1)
                    {
                        System.out.println("走到这里说明,正常的退出没成功..kill掉...");
                        MNET.executeLinuxCmd("ps aux|grep java|awk '{print $2}'|xargs kill -9");
                        System.exit(0);
                        MNET.executeLinuxCmd("ps aux|grep java|awk '{print $2}'|xargs kill -9");
                    }
                    Thread.sleep(1000);
               } catch (Throwable e) {
                   System.out.println("走到这里说明,正常的退出没成功,并且异常了");
                   MNET.executeLinuxCmd("ps aux|grep chrome|awk '{print $2}'|xargs kill -9");
                   MNET.executeLinuxCmd("ps aux|grep java|awk '{print $2}'|xargs kill -9");
                   System.exit(1);
               }
          }
     }

    /****
     * 获取当前小时数
     */
    public static int getTimeH(){
        Calendar calendar = Calendar.getInstance();
        int curHour24 = calendar.get(calendar.HOUR_OF_DAY);
        return curHour24;
    }

    public static Long getmicTime() {
        Long cutime = System.currentTimeMillis() * 1000; // 微秒
        Long nanoTime = System.nanoTime(); // 纳秒
        return cutime + (nanoTime - nanoTime / 1000000 * 1000000) / 1000;
    }

     public static void main(String[] args) {
         System.out.println(getLastDay());
     }

     public static String getLastDay(){
        String str= "startdate="+getSomeDay(new Date(),-2)+"&enddate="+getSomeDay(new Date(),0);
        return str;
     }
}
