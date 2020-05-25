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
import com.teamdev.jxbrowser.chromium.swing.DefaultNetworkDelegate;
import config.UA;
import tool.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseButtonType.PRIMARY;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseEventType.*;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseScrollType.WHEEL_BLOCK_SCROLL;

/***
 1 我猜测是不是还是展示量+点击量+点击率；是关键；目前看下来，站外才能展示量，点击2种办法；
 内和外，我准备都试试;


 1 总体采用不结束进程的方式来处理;在线程内处理；
 2 还有这次测试要注意浏览器版本问题；我怀疑有这种可能性；
 */


public class Rank360 {
   static  boolean show=true;
   static EnglishResolution.WidthHeiht widthHeiht = EnglishResolution.getEnglishResolution(EnglishResolution.PC);
    public static void main(String[] args) throws Exception {
        new Thread(new Monitor(168)).start();
        Basic.init();
        Browser browser = Basic.getBroser();
        BrowserPreferences.setUserAgent(UA.getUa(UA.PC));
        browser.setSize(widthHeiht.getWidth(), widthHeiht.getHeight());
        String doman=null,  searchwords=null;
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
                int l= L.randomNumber(0,1);
                String url="https://www.so.com/s?q="+searchwords.trim()+"&site="+doman+"&rg=1";
                System.out.println("当前准备运行的是:"+doman+">>"+searchwords);
                if(item!=null&&doman!=null){
                    logic(browser, doman, searchwords, widthHeiht,url);
                }
            }
        }catch (Throwable e){
            System.exit(0);
        }
    }

    static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    static boolean isload=false;
    static void logic(Browser browser, String doman, String searchwords, EnglishResolution.WidthHeiht widthHeiht, String url) throws Exception {
        if("1".equals(PropertyConfig.getPropertyV("ipChange").trim()))
            MNET.ipChange();

            browser.getContext().getNetworkService().setNetworkDelegate(new DefaultNetworkDelegate(){
                @Override
                public void onBeforeSendHeaders(BeforeSendHeadersParams params) {
                    try {
                        if(params.getURL().contains("&et=")||params.getURL().contains("&si=")){
                            String Referer="https://www.so.com/";
                            params.getHeadersEx().setHeader("Referer",Referer);
                            params.getHeaders().setHeader("Referer",Referer);;
                        }
                    } catch (Exception e) {
                        System.out.println("替换referre失败...");
                    }

                }
            });
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
                                System.out.println("进入监听");
                                while (true){
                                    if (browser.getURL().contains(doman) && !browser.getURL().contains("so.com"))
                                    {
                                        System.out.println("============================================================");
                                        System.out.println("$$$$$$$$$$$$$$$$$$$$点击成功:"+browser.getURL());
                                        System.out.println("============================================================");
                                        browser.dispose();
                                        System.exit(0);
                                    }
                                    try {
                                        Thread.sleep(500);
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
                System.out.println("进入....加载完毕..");
                    if(event.getBrowser().getDocument().findElements(By.xpath("//*[@id=\"main\"]/ul/li"))!=null){
                        for(DOMElement item:event.getBrowser().getDocument().findElements(By.xpath("//*[@id=\"main\"]/ul/li"))){
                            if(item.getInnerText().contains(doman)){
                                System.out.println("********找到匹配项,开始准备点击*********");
                                isload=true;
                                excute(browser,item,widthHeiht);
                                System.out.println("*******************************");
                                System.out.println(item.getInnerText());
                                System.out.println("*******************************");
                                break ;
                            }
                        };
                    }
                }
        });
        System.out.println("url:"+url);
        browser.loadURL(url);
        new Thread(new Monitor(68)).start();

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
            Basic.randomMove(browser,widthHeiht.width,widthHeiht.height);
            Rectangle  rectangle = domElement.getBoundingClientRect();
            System.out.println(">>>>>" + rectangle.x + "|" + rectangle.y + "|" + rectangle.width + "|" + rectangle.height + "|" + widthHeiht.width + "|" + widthHeiht.height);
            domElement=domElement.findElement(By.xpath("h3/a"));
            Basic.move(browser, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            //browser.executeJavaScript("window.scrollTo(" + rectangle.x + "," + (rectangle.y-(100+(L.randomNumber(1,68)))) + ");");
            Basic.randomMove(browser,widthHeiht.width,widthHeiht.height);
            Basic.targetMove(browser,domElement);
            // domElement.click();
            System.out.println("___________________________________________________________");
            domElement.scrollToBottom();
            rectangle = domElement.getBoundingClientRect();
            forwardMouseClickEvent(browser, PRIMARY, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
