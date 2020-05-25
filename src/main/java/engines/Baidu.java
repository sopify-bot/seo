package engines;

import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyModifiers;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyModifiersBuilder;
import com.teamdev.jxbrowser.chromium.BrowserMouseEvent.BrowserMouseEventBuilder;
import com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseButtonType;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import config.UA;
import tool.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseButtonType.PRIMARY;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseEventType.*;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseScrollType.WHEEL_BLOCK_SCROLL;


public class Baidu {
   static  boolean show=true;
   static EnglishResolution.WidthHeiht widthHeiht = EnglishResolution.getEnglishResolution(EnglishResolution.PC);
    static long startMonitor=System.currentTimeMillis();
    public static void main(String[] args) throws Exception {
        new Thread(new Monitor(1000*60*5)).start();
        if("1".equals(PropertyConfig.getPropertyV("ipChange").trim()))
        {
            MNET.ipChange();
            System.out.println("换IP....");
        }else {
            System.out.println("忘记没有换IP吗？");
        }

        String doman=null,  searchwords=null,title=null;
        try {
            System.out.println("***************测试大量点击，没有展示...***********************");
            List<String> stringList=new ArrayList();
            for(int i=0;i<500;i++){
                String str= PropertyConfig.getPropertyV("test"+i);
                if(str!=null&&str.trim().length()>2){
                    stringList.add(str);
                    System.out.println(str);
                }
            }
            if(stringList.size()>0){
                String item= L.shenChoose(stringList);
                doman= RegexParse.baseParse(item,"doman=([\\S]*?);",1);
                searchwords= RegexParse.baseParse(item,"searchWords=([\\S]*?);",1);
                String url=null;
                System.out.println("当前准备运行的是:"+doman+">>"+searchwords+">>"+url);
                if(item!=null&&doman!=null){
                        init();
                        Browser browser = getBroser();
                        BrowserPreferences.setUserAgent(UA.getUa(UA.PC));
                        browser.setSize(widthHeiht.getWidth(), widthHeiht.getHeight());
                        new Thread(new Monitor(168)).start();
                        logic(browser, doman, searchwords, widthHeiht,url,title);
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    static String  getSource(){
        List<String> arrays= Arrays.asList("baidu","50000049_hao_pg","site888_3_pg","56060048_4_pg","97724291_hao_pg","02049043_23_pg","77092190_pg","50000041_hao_pg","02003390_23_hao_pg","02049043_6_pg","request_28_pg","monline_3_dg","95156753_hao_pg","91121590_hao_pg","94962502_hao_pg","66069139_dg","1314","request_2_pg","maxco4","wjlhkp_pg","78040160_15_pg","site5566","58025142_5_oem_dg","78040160_15_pg","27073201_2_hao_pg","58025142_5_oem_dg");
        return  arrays.get(L.randomNumber(0,arrays.size()-1));
    }

    static void logic(Browser browser, String doman, String searchwords, EnglishResolution.WidthHeiht widthHeiht, String url, String title) throws Exception {
        browser.setPopupHandler(new PopupHandler() {
            public PopupContainer handlePopup(PopupParams params) {
                return new PopupContainer() {
                    public void insertBrowser(final Browser browser, final Rectangle initialBounds) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                    BrowserView browserView = new BrowserView(browser);
                                    browserView.setPreferredSize(initialBounds.getSize());
                                    final JFrame frame = new JFrame();
                                    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                                    frame.add(browserView, BorderLayout.CENTER);
                                    frame.pack();
                                    frame.setSize(1024, 768);
                                    frame.setLocation(initialBounds.getLocation());
                                    frame.setVisible(true);
                                System.out.println("进入监听"+browser.getURL());
                                while (true){
                                    if(browser.getURL().contains(doman)&&!browser.getURL().contains("baidu")&&!browser.getURL().contains("video"))
                                    {
                                        System.out.println("============================================================");
                                        System.out.println("$$$$$$$$$$$$$$$$$$$$点击成功:"+browser.getURL()+"总耗时:"+((System.currentTimeMillis()-startMonitor)/1000)+"秒");
                                        System.out.println("============================================================");
                                        try {
                                            Thread.sleep(L.randomNumber(1000,5000));
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        browser.dispose();
                                        System.exit(0);
                                        break;
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                };
            }
        });

        browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                if(browser.getURL().contains("antispider")){
                    System.out.println("封锁了...");
                    System.exit(0);
                }
                System.out.println("加载完毕.....");
                for(int i=1;i<10;i++){
                    DOMElement domElement=browser.getDocument().findElement(By.xpath("//*[@id=\""+i+"\"]"));
                    if(domElement==null)
                        continue;
                    if(domElement.getInnerHTML().contains(doman)&&domElement.getInnerText().contains(title)){
                            System.out.println(browser.getURL());
                            System.out.println("找到匹配开始点击....");
                            excute(browser, domElement, widthHeiht);
                            System.out.println(domElement.getInnerText());
                        break ;
                    }
                }
            }
        });
        browser.loadURL(url);
    }




    private static void forwardMousePressEvent(Browser browser,
                                               MouseButtonType buttonType,
                                               int x,
                                               int y,
                                               int globalX,
                                               int globalY) {
        BrowserMouseEventBuilder builder = new BrowserMouseEventBuilder();
        builder.setEventType(MOUSE_PRESSED)
                .setButtonType(buttonType)
                .setX(x)
                .setY(y)
                .setGlobalX(globalX)
                .setGlobalY(globalY)
                .setClickCount(1)
                .setModifiers(new KeyModifiersBuilder().mouseButton().build());
        browser.forwardMouseEvent(builder.build());
    }

    private static void forwardMouseReleaseEvent(Browser browser,
                                                 MouseButtonType buttonType,
                                                 int x,
                                                 int y,
                                                 int globalX,
                                                 int globalY) {
        BrowserMouseEventBuilder builder = new BrowserMouseEventBuilder();
        builder.setEventType(MOUSE_RELEASED)
                .setButtonType(buttonType)
                .setX(x)
                .setY(y)
                .setGlobalX(globalX)
                .setGlobalY(globalY)
                .setClickCount(1)
                .setModifiers(KeyModifiers.NO_MODIFIERS);
        browser.forwardMouseEvent(builder.build());
    }

    private static void forwardMouseClickEvent(Browser browser,
                                               MouseButtonType buttonType,
                                               int x,
                                               int y,
                                               int globalX,
                                               int globalY) {
        forwardMousePressEvent(browser, buttonType, x, y, globalX, globalY);
        forwardMouseReleaseEvent(browser, buttonType, x, y, globalX, globalY);
    }

    private static void forwardMouseScrollEvent(Browser browser,
                                                int unitsToScroll,
                                                int x,
                                                int y) {
        BrowserMouseEventBuilder builder = new BrowserMouseEventBuilder();
        builder.setEventType(MOUSE_WHEEL)
                .setX(x)
                .setY(y)
                .setGlobalX(0)
                .setGlobalY(0)
                .setScrollBarPixelsPerLine(25)
                .setScrollType(WHEEL_BLOCK_SCROLL)
                .setWindowX(widthHeiht.width)
                .setWindowY(widthHeiht.height)
                .setUnitsToScroll(unitsToScroll);
        browser.forwardMouseEvent(builder.build());
    }

    public static void excute(Browser browser, DOMElement domElement, EnglishResolution.WidthHeiht widthHeiht) {
            try {
                randomMove(browser,widthHeiht.width,widthHeiht.height);
                scroll(browser, domElement, widthHeiht);
                Rectangle  rectangle = domElement.getBoundingClientRect();
                System.out.println(">>>>>" + rectangle.x + "|" + rectangle.y + "|" + rectangle.width + "|" + rectangle.height + "|" + widthHeiht.width + "|" + widthHeiht.height);
                domElement=domElement.findElement(By.xpath("h3/a"));
                move(browser, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                //browser.executeJavaScript("window.scrollTo(" + rectangle.x + "," + (rectangle.y-(100+(L.randomNumber(1,68)))) + ");");
                randomMove(browser,widthHeiht.width,widthHeiht.height);
                targetMove(browser,domElement);
               // domElement.click();
                System.out.println("___________________________________________________________");
                domElement.scrollToBottom();
                rectangle = domElement.getBoundingClientRect();
                forwardMouseClickEvent(browser, PRIMARY, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    static  int Iloc= L.randomNumber(0,2);
    public static void  scroll(Browser browser, DOMElement domElement, EnglishResolution.WidthHeiht widthHeiht){
        Rectangle rectangleL = domElement.getBoundingClientRect();
        int locL=rectangleL.y;
        int scrollH=0,loc=-1;
        for(int i=0;i<30;i++){
            try {
                scrollH+= L.randomNumber(80,300);
                browser.executeJavaScript("window.scrollTo(" + L.randomNumber(10,rectangleL.x) + "," +scrollH + ");");
                Thread.sleep(L.randomNumber(600,6000));
                if(scrollH>locL){
                    Iloc--;
                }
                if(Iloc<0)
                    break;
                System.out.println("scoll:"+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(int i=0;i<30;i++){
            try {
                scrollH-= L.randomNumber(80,300);
                if(scrollH<0)
                    scrollH= L.randomNumber(0,10);
                browser.executeJavaScript("window.scrollTo(" + L.randomNumber(10,rectangleL.x) + "," +scrollH + ");");
                Thread.sleep(L.randomNumber(600,3000));
                System.out.println("scrollH:"+scrollH+"height："+locL);
                if((scrollH+150)<locL||scrollH<20){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*********
     * 准备工作
     */
    public  static void   init(){
        try {
            Field e = ba.class.getDeclaredField("e");
            e.setAccessible(true);
            Field f = ba.class.getDeclaredField("f");
            f.setAccessible(true);
            Field modifersField = Field.class.getDeclaredField("modifiers");
            modifersField.setAccessible(true);
            modifersField.setInt(e, e.getModifiers() & ~Modifier.FINAL);
            modifersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            e.set(null, new BigInteger("1"));
            f.set(null, new BigInteger("1"));
            modifersField.setAccessible(false);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    public  static  void randomMove(Browser browser,int width,int height) {
        long startT=System.currentTimeMillis();
        System.out.println("随机晃动鼠标开始");
        for(int i = 0; i< L.randomNumber(1,2); i++){
            int sx=0;
            int sy=0;
            int ex=width;
            int ey=height;
            int xxx=0,yyy=0;
            int Monitor= L.randomNumber(100,200),Monitori= L.randomNumber(0,1);
            int z=50,zi=0;
            int s= L.randomNumber(0,3);
            if(s==0){
                xxx=0;
                yyy=0;
            }else if(s==1){
                xxx=width;
                yyy=height;
            }else if(s==2){
                xxx=0;
                yyy=height;
            }else if(s==3){
                xxx=width;
                yyy=0;
            }
            for(int qq = 0; qq< L.randomNumber(1,2); qq++){
                if(s==0){
                    xxx+= L.randomNumber(z-(zi+1),z-(zi++)+ L.randomNumber(0,5));
                    yyy+= L.randomNumber(z-(zi+1),z-(zi++));
                }else if(s==1){
                    xxx-= L.randomNumber(z-(zi+1),z-(zi++)+ L.randomNumber(0,5));
                    yyy-= L.randomNumber(z-(zi+1),z-(zi++));
                }else if(s==2){
                    xxx+= L.randomNumber(z-(zi+1),z-(zi++));
                    yyy-= L.randomNumber(z-(zi+1),z-(zi++)+ L.randomNumber(0,5));
                }else if(s==3){
                    xxx-= L.randomNumber(z-(zi+1),z-(zi++)+ L.randomNumber(0,5));
                    yyy+= L.randomNumber(z-(zi+1),z-(zi++));
                }
                yyy+= L.randomNumber(z-(zi+2),z-(zi++));;
                if(xxx<0||xxx>width||yyy<0||yyy>height)
                    break;
                move(browser,xxx,yyy,width,height);
                try {
                    Monitor=(Monitor<20)?20:(Monitor- L.randomNumber(Monitori+3,Monitori+25));
                    Monitor=Monitor<1?1:Monitor;
                    Thread.sleep(Monitor);
                } catch (Exception e) {
                }
            }
            for(int j = 0; j< L.randomNumber(0,2); j++){
                xxx= L.randomNumber(0,width);
                yyy= L.randomNumber(0,height);
                int ccdd= L.randomNumber(0,3);
                for(int ii=0;ii<30;ii++){
                    if(ccdd==0)
                    {
                        xxx+=ii+ L.randomNumber(0,ii);
                        yyy+=ii+ L.randomNumber(0,ii+ L.randomNumber(0,5));
                    }
                    if(ccdd==1)
                    {
                        xxx-=ii+ L.randomNumber(0,ii+ L.randomNumber(0,5));
                        yyy-=ii+ L.randomNumber(0,ii);
                    }
                    if(ccdd==2)
                    {
                        xxx-=ii+ L.randomNumber(0,ii);
                        yyy+=ii+ L.randomNumber(0,ii+ L.randomNumber(0,5));
                    }
                    if(ccdd==3)
                    {
                        xxx+=ii+ L.randomNumber(0,ii+ L.randomNumber(0,5));
                        yyy-=ii+ L.randomNumber(0,ii);
                    }
                    move(browser,xxx,yyy,width,height);
                    try {
                        Monitor=(Monitor<20)?20:(Monitor- L.randomNumber(Monitori+3,Monitori+25));
                        Monitor=Monitor<1?1:Monitor;
                        Thread.sleep(Monitor+ L.randomNumber(0,10));
                    } catch (Exception e) {
                    }
                }
            }
        }
        System.out.println("随机晃动鼠标完毕 耗时:"+(System.currentTimeMillis()-startT));
    }

    public  static void move(Browser browser,int x,int y,int gx,int gy){
        BrowserMouseEvent.BrowserMouseEventBuilder builder = new BrowserMouseEvent.BrowserMouseEventBuilder();
        builder.setEventType(MOUSE_MOVED)
                .setX(x)
                .setY(y)
                .setGlobalX(gx)
                .setGlobalY(gy);
        browser.forwardMouseEvent(builder.build());
    }

    public static  void  targetMove(Browser browser,DOMElement domElement){
        try {
            Rectangle  rectangle=domElement.getBoundingClientRect();
            move(browser, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } catch (Throwable e) {
            System.out.println("移动到指定的元素，这个出现错误，认为不重要，忽略");
        }
    }



    static Browser  getBroser(){
        long startMonitor=System.currentTimeMillis();
        System.out.println("开始准备各种逻辑");
        BrowserPreferences.setChromiumSwitches(
                "--disable-gpu",
                "--disable-gpu-compositing",
                "--enable-begin-frame-scheduling"
                ,"--software-rendering-fps=60"

        );
        //构造隐身模式的浏览器
        BrowserPreferences.setUserAgent(UA.getUa(UA.PC));
       // BrowserContextParams params = new BrowserContextParams("user-data");
        BrowserContextParams params = new BrowserContextParams(" /home/sougou/user-data");

        params.setStorageType(StorageType.MEMORY);
        BrowserContext browserContext = new BrowserContext(params);
        Browser browser = new Browser(BrowserType.LIGHTWEIGHT,browserContext);

//        BrowserPreferences preferences = browser.getPreferences();
//        preferences.setImagesEnabled(false);
        // preferences.setJavaScriptEnabled(false);
//        browser.setPreferences(preferences);
        //清空缓存
        browser.getCacheStorage().clearCache();
        browser.getLocalWebStorage().clear();
        browser.getCookieStorage().deleteAll();
        browser.getSessionWebStorage().clear();
        //设置浏览器头，和浏览器尺寸
        EnglishResolution.WidthHeiht widthHeiht= EnglishResolution.getEnglishResolution(EnglishResolution.PC);;
        browser.setSize(widthHeiht.getWidth(),widthHeiht.getHeight());
        System.out.println("组件初始化完成...耗时:"+(System.currentTimeMillis()-startMonitor)/1000+"秒");
        /*************************是否显示界面************************************/
        BrowserView view = new BrowserView(browser);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //不显示标题栏,最大化,最小化,退出按钮
        //frame.setUndecorated(true);
        frame.setSize(widthHeiht.getWidth(),widthHeiht.getHeight());
        frame.add(view, BorderLayout.CENTER);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        /*************************************************************/
        return  browser;
    }

    /***
     * 根据时间段判断是否需要点击;
     */
    static  boolean isClick(){
        int h= Monitor.getTimeH();
        System.out.println("当前小时为:"+h);
        switch (h){
            case 1:{
                return  gailv(0);
            }
            case 2:{
                return  gailv(0);
            }
            case 3:{
                return  gailv(0);
            }
            case 4:{
                return  gailv(0);
            }
            case 5:{
                return  gailv(0);
            }
            case 6:{
                return  gailv(0);
            }
            case 7:{
                return  gailv(0);
            }
            case 8:{
                return  gailv(30);
            }
            case 9:{
                return  gailv(40);
            }
            case 10:{
                return  gailv(70);
            }
            case 11:{
                return  gailv(80);
            }
            case 12:{
                return  gailv(50);
            }
            case 13:{
                return  gailv(80);
            }
            case 14:{
                return  gailv(120);
            }
            case 15:{
                return  gailv(160);
            }
            case 16:{
                return  gailv(200);
            }
            case 17:{
                return  gailv(140);
            }
            case 18:{
                return  gailv(300);
            }
            case 19:{
                return  gailv(720);
            }
            case 20:{
                return  gailv(360);
            }
            case 21:{
                return  gailv(200);
            }
            case 22:{
                return  gailv(80);
            }
            case 23:{
                return  gailv(30);
            }
            case 24:{
                return  gailv(5);
            }
            case 0:{
                return  gailv(5);
            }

        }
        return false;
    }

    static boolean gailv(int baifenbi){
        baifenbi=baifenbi*5;
        if(360==baifenbi){
            return  true;
        }
        if(0==baifenbi)
            return false;
        for(int i=0;i<baifenbi;i++){
            int jishu= L.randomNumber(1,360);
            if(jishu==1)
                return true;
        }
        return false;
    }


}
