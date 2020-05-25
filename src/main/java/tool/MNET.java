package tool;

import tool.RegexParse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MNET {

    /***
     *
     *
     * https://www.ipip.net/ip.html 甄别是否是真人ip
     *
     *
     *
     * @param cmd
     * @return
     */

    public static String executeLinuxCmd(String cmd) {
        System.out.println("got cmd job : " + cmd);
        Runtime run = Runtime.getRuntime();
        try {
//            Process process = run.exec(cmd);
            Process process = run.exec(new String[] {"/bin/sh", "-c", cmd});
            InputStream in = process.getInputStream();
            BufferedReader bs = new BufferedReader(new InputStreamReader(in));
            List<String> list = new ArrayList<String>();
            String result = null;
            while ((result = bs.readLine()) != null) {
                System.out.println("job result [" + result + "]");
                list.add(result);
            }
            in.close();
            // process.waitFor();
            process.destroy();
            StringBuilder stringBuilder=new StringBuilder();
            list.forEach(item->{
                stringBuilder.append(item);
            });
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getIP(){
        return executeLinuxCmd("curl -L tool.lu/ip");
    }

    public static String killChrome(){
        return executeLinuxCmd("ps aux|grep chrome|awk '{print $2}'|xargs kill -9");
    }

    /***
     * 测试是否可以连接到外网;
     * @return
     */
    public static boolean isConnection(){
       try {
           String result=executeLinuxCmd("curl http://ip-api.com/json/?lang=zh-CN");
           if(result!=null&& RegexParse.ismatching(result,"query\":\"[\\d]*?\\.[\\d]*?\\.[\\d]*?\\.[\\d]{1,3}")){
               return true;
           }
       }catch (Throwable e){
            e.printStackTrace();
       }
        return false;
    }

    private static String lastIp="0";
    /***
     * 测试是否可以连接到外网;
     * @return
     */
    public static boolean isChangeIP(){
        try {
            String result=executeLinuxCmd("curl http://ip-api.com/json/?lang=zh-CN");
            if(result!=null&&RegexParse.ismatching(result,"query\":\"[\\d]*?\\.[\\d]*?\\.[\\d]*?\\.[\\d]{1,3}")){
                String ip=RegexParse.baseParse(result,"query\":\"([\\d]*?\\.[\\d]*?\\.[\\d]*?\\.[\\d]{1,3})\"",1);
                if(!lastIp.equals(ip))
                {
                    lastIp=ip;
                    return true;
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * * pppoe-start 拨号
     *  *
     *  * pppoe-stop  断开拨号
     *  *
     *  * pppoe-status 拨号连接状态
     */
    public static void ipChange() throws InterruptedException {
        //重试次数5分钟;
        //System.out.println("执行清空日志文件的命令");
//        executeLinuxCmd("rm -rf /tmp/*");
        //System.out.println("执行清空日志文件的命令完成....");
        out:
        for(int i=1;i<2;i++){
            try{
                //先执行关闭,并且判断已经关闭后;开启启动
                executeLinuxCmd("pppoe-stop");
                String result="";
                for(int cc=0;cc<3;cc++){
                    result=executeLinuxCmd("pppoe-status");
                    if(RegexParse.ismatching(result,"Link is down")||RegexParse.ismatching(result,"Link[\\s]*?isLink[\\s]*?down")){
                        executeLinuxCmd("pppoe-start");
                        break ;
                    }
                    Thread.sleep(1000);
                }
                for(int j=1;i<10;j++){
                    result=executeLinuxCmd("pppoe-status");
                    if(RegexParse.ismatching(result,"Link is up")||RegexParse.ismatching(result,"Link[\\s]*?is[\\s]*?up")){
                        System.out.println("ip 连接成功>>>>");
                        break out;
                    }
                    Thread.sleep(1000);
                    System.out.println("状态为成功，重试查看"+j);
                }
                System.out.println("ip 拨号没通；开始第"+i+"次尝试...");
            }catch (Throwable e){
                e.printStackTrace();
                System.exit(0);
            }
        }
    }


    public static void main(String[] args) {
        System.out.println(executeLinuxCmd("pppoe-stop"));
    }
}
