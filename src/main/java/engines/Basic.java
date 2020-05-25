package engines;

import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import tool.L;
import config.UA;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseButtonType.PRIMARY;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseEventType.*;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseEventType.MOUSE_WHEEL;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseScrollType.WHEEL_BLOCK_SCROLL;

public class Basic {


    public  static  void randomMove(Browser browser,int width,int height) {
        long startT=System.currentTimeMillis();
        System.out.println("随机晃动鼠标开始");
        for(int i = 0; i< L.randomNumber(1,2); i++){
            int sx=0;
            int sy=0;
            int ex=width;
            int ey=height;
            int xxx=0,yyy=0;
            int time=L.randomNumber(100,200),timei=L.randomNumber(0,1);
            int z=50,zi=0;
            int s=L.randomNumber(0,3);
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
            for(int qq=0;qq<L.randomNumber(1,2);qq++){
                if(s==0){
                    xxx+=L.randomNumber(z-(zi+1),z-(zi++)+L.randomNumber(0,5));
                    yyy+=L.randomNumber(z-(zi+1),z-(zi++));
                }else if(s==1){
                    xxx-=L.randomNumber(z-(zi+1),z-(zi++)+L.randomNumber(0,5));
                    yyy-=L.randomNumber(z-(zi+1),z-(zi++));
                }else if(s==2){
                    xxx+=L.randomNumber(z-(zi+1),z-(zi++));
                    yyy-=L.randomNumber(z-(zi+1),z-(zi++)+L.randomNumber(0,5));
                }else if(s==3){
                    xxx-=L.randomNumber(z-(zi+1),z-(zi++)+L.randomNumber(0,5));
                    yyy+=L.randomNumber(z-(zi+1),z-(zi++));
                }
                yyy+=L.randomNumber(z-(zi+2),z-(zi++));;
                if(xxx<0||xxx>width||yyy<0||yyy>height)
                    break;
                move(browser,xxx,yyy,width,height);
                try {
                    time=(time<20)?20:(time-L.randomNumber(timei+3,timei+25));
                    time=time<1?1:time;
                    Thread.sleep(time);
                } catch (Exception e) {
                }
            }
            for(int j=0;j<L.randomNumber(0,2);j++){
                xxx=L.randomNumber(0,width);
                yyy=L.randomNumber(0,height);
                int ccdd=L.randomNumber(0,3);
                for(int ii=0;ii<30;ii++){
                    if(ccdd==0)
                    {
                        xxx+=ii+L.randomNumber(0,ii);
                        yyy+=ii+L.randomNumber(0,ii+L.randomNumber(0,5));
                    }
                    if(ccdd==1)
                    {
                        xxx-=ii+L.randomNumber(0,ii+L.randomNumber(0,5));
                        yyy-=ii+L.randomNumber(0,ii);
                    }
                    if(ccdd==2)
                    {
                        xxx-=ii+L.randomNumber(0,ii);
                        yyy+=ii+L.randomNumber(0,ii+L.randomNumber(0,5));
                    }
                    if(ccdd==3)
                    {
                        xxx+=ii+L.randomNumber(0,ii+L.randomNumber(0,5));
                        yyy-=ii+L.randomNumber(0,ii);
                    }
                    move(browser,xxx,yyy,width,height);
                    try {
                        time=(time<20)?20:(time-L.randomNumber(timei+3,timei+25));
                        time=time<1?1:time;
                        Thread.sleep(time+L.randomNumber(0,10));
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

    public static  void excute( Browser browser,DOMElement domElement) {
        Rectangle  rectangle=domElement.getBoundingClientRect();
        System.out.println(">>>>>"+rectangle.x+"|"+rectangle.y+"|"+rectangle.width+"|"+ rectangle.height);
        Basic.move(browser, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        try {
            Thread.sleep(L.randomNumber(100,300),L.randomNumber(100,5000));
            Basic.forwardMouseClickEvent(browser, PRIMARY, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            //domElement.click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static  void  targetMove(Browser browser,DOMElement domElement){
        try {
            Rectangle  rectangle=domElement.getBoundingClientRect();
            move(browser, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } catch (Throwable e) {
            System.out.println("移动到指定的元素，这个出现错误，认为不重要，忽略");
        }
    }
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


    private static void forwardMousePressEvent(Browser browser,
                                               BrowserMouseEvent.MouseButtonType buttonType,
                                               int x,
                                               int y,
                                               int globalX,
                                               int globalY) {
        BrowserMouseEvent.BrowserMouseEventBuilder builder = new BrowserMouseEvent.BrowserMouseEventBuilder();
        builder.setEventType(MOUSE_PRESSED)
                .setButtonType(buttonType)
                .setX(x)
                .setY(y)
                .setGlobalX(globalX)
                .setGlobalY(globalY)
                .setClickCount(1)
                .setModifiers(new BrowserKeyEvent.KeyModifiersBuilder().mouseButton().build());
        browser.forwardMouseEvent(builder.build());
    }

    private static void forwardMouseReleaseEvent(Browser browser,
                                                 BrowserMouseEvent.MouseButtonType buttonType,
                                                 int x,
                                                 int y,
                                                 int globalX,
                                                 int globalY) {
        BrowserMouseEvent.BrowserMouseEventBuilder builder = new BrowserMouseEvent.BrowserMouseEventBuilder();
        builder.setEventType(MOUSE_RELEASED)
                .setButtonType(buttonType)
                .setX(x)
                .setY(y)
                .setGlobalX(globalX)
                .setGlobalY(globalY)
                .setClickCount(1)
                .setModifiers(BrowserKeyEvent.KeyModifiers.NO_MODIFIERS);
        browser.forwardMouseEvent(builder.build());
    }

    public static void forwardMouseClickEvent(Browser browser,
                                               BrowserMouseEvent.MouseButtonType buttonType,
                                               int x,
                                               int y,
                                               int globalX,
                                               int globalY) throws InterruptedException {
        forwardMousePressEvent(browser, buttonType, x, y, globalX, globalY);
        Thread.sleep(L.randomNumber(10,100));
        forwardMouseReleaseEvent(browser, buttonType, x, y, globalX, globalY);
    }

    private static void forwardMouseScrollEvent(Browser browser,
                                                int unitsToScroll,
                                                int x,
                                                int y) {
        BrowserMouseEvent.BrowserMouseEventBuilder builder = new BrowserMouseEvent.BrowserMouseEventBuilder();
        builder.setEventType(MOUSE_WHEEL)
                .setX(x)
                .setY(y)
                .setGlobalX(0)
                .setGlobalY(0)
                .setScrollBarPixelsPerLine(25)
                .setScrollType(WHEEL_BLOCK_SCROLL)
                .setUnitsToScroll(unitsToScroll);
        browser.forwardMouseEvent(builder.build());
    }


    static Browser  getBroser(){
        long startTime=System.currentTimeMillis();
        System.out.println("开始准备各种逻辑");
        BrowserPreferences.setChromiumSwitches(
                "--disable-gpu",
                "--disable-gpu-compositing",
                "--enable-begin-frame-scheduling"
                ,"--software-rendering-fps=60"

        );
        //构造隐身模式的浏览器
        BrowserPreferences.setUserAgent(UA.getUa(UA.PC));
        //BrowserContextParams params = new BrowserContextParams("user-data");
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
        EnglishResolution.WidthHeiht widthHeiht=EnglishResolution.getEnglishResolution(EnglishResolution.PC);;
        browser.setSize(widthHeiht.getWidth(),widthHeiht.getHeight());
        System.out.println("组件初始化完成...耗时:"+(System.currentTimeMillis()-startTime)/1000+"秒");
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


   public static int  findLocation(Browser browser,String domain,String searchWords){
        try {
            for(int i=1;i<30;i++){
                String url="https://www.sogou.com/web?query="+searchWords+"&page="+i;
                Browser.invokeAndWaitFinishLoadingMainFrame(browser, new Callback<Browser>() {
                    @Override
                    public void invoke(Browser value) {
                        //加载url
                        System.out.println("加载url..."+url);
                        value.loadURL(url);
                        System.out.println("wait to load...");
                    }
                });
                if(browser.getDocument().getDocumentElement().getInnerHTML().contains(domain))
                {
                    System.out.println("域名"+domain+"当前页"+browser.getURL());
                    return i;
                }
                Thread.sleep(L.randomNumber(500,2000));
            }
        }catch (Throwable e){
            e.printStackTrace();
            return 0;
        }
        return 0;
    }


    public static int  findLocationForBaidu(Browser browser,String domain,String searchWords){
        try {
            for(int i=1;i<30;i++){
                String url="https://www.baidu.com/s?wd="+searchWords+"&pn="+i;
                Browser.invokeAndWaitFinishLoadingMainFrame(browser, new Callback<Browser>() {
                    @Override
                    public void invoke(Browser value) {
                        //加载url
                        System.out.println("加载url..."+url);
                        value.loadURL(url);
                        System.out.println("wait to load...");
                    }
                });
                if(browser.getDocument().getDocumentElement().getInnerHTML().contains(domain))
                {
                    System.out.println("域名"+domain+"当前页"+browser.getURL());
                    return i;
                }
                Thread.sleep(L.randomNumber(500,2000));
            }
        }catch (Throwable e){
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

}
