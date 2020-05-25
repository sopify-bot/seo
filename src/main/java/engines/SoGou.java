package engines;

import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyModifiers;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyModifiersBuilder;
import com.teamdev.jxbrowser.chromium.BrowserMouseEvent.BrowserMouseEventBuilder;
import com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseButtonType;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseButtonType.PRIMARY;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseEventType.*;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseScrollType.WHEEL_BLOCK_SCROLL;

public class SoGou {
   static  boolean show=true;
   static EnglishResolution.WidthHeiht widthHeiht = EnglishResolution.getEnglishResolution(EnglishResolution.PC);
    public static void main(String[] args) throws Exception {
        new Thread(new Monitor(168)).start();
        if("1".equals(PropertyConfig.getPropertyV("ipChange").trim()))
        {
            MNET.ipChange();
            System.out.println("换IP....");
        }else {
            System.out.println("忘记没有换IP吗？");
        }
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
                System.out.println("当前准备运行的是:"+doman+">>"+searchwords);
                if(item!=null&&doman!=null){
                    logic(browser, doman, searchwords, widthHeiht);
                }
            }
        }catch (Throwable e){
            System.exit(0);
        }
    }

    static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    static void logic(Browser browser, String doman, String searchwords, EnglishResolution.WidthHeiht widthHeiht) throws Exception {
        browser.getContext().getNetworkService().setNetworkDelegate(new DefaultNetworkDelegate(){
            @Override
            public void onBeforeSendHeaders(BeforeSendHeadersParams params) {
                try {
                    if(params.getURL().contains("&et=")||params.getURL().contains("&si=")){
                        String Referer="https://www.qq.com/";
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
                                    if(browser.getURL().contains(doman)&&!browser.getURL().contains("sogou"))
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
                if(browser.getURL().contains("site")){
                    //开始鼠标晃动+自动点击等功能;
                    DOMDocument doc = browser.getDocument();
                    List<DOMElement> rb = doc.findElements(By.className("rb"));
                    List<DOMElement> vrwrap = doc.findElements(By.className("vrwrap"));
                    System.out.println("主页已经加载完毕:是否有点击元素组" + (rb != null) + (vrwrap != null) + ">url:" + browser.getURL());
                    //判断主页是否加载完毕，然后进行点击;
                    boolean isLoad = true;
                    for (DOMElement item : rb) {
                        if (item.getInnerHTML().contains(doman)) {
                            isLoad = false;
                            System.out.println("********找到匹配项,开始准备点击1*********");
                            excute(browser, item, widthHeiht);
                            System.out.println("*******************************");
                            System.out.println(item.getInnerText());
                            System.out.println("*******************************");
                            break;
                        }
                    }
                    ;
                    if (isLoad)
                        for (DOMElement item : vrwrap) {
                            if (item.getInnerHTML().contains(doman)) {
                                System.out.println("********找到匹配项,开始准备点击2*********");
                                excute(browser, item, widthHeiht);
                                System.out.println("*******************************");
                                System.out.println(item.getInnerText());
                                System.out.println("*******************************");
                                break;
                            }
                        }
                }
            }
        });
        String url="https://www.sogou.com/tx?site="+doman.trim()+"&query="+searchwords.trim();
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
            Basic.randomMove(browser,widthHeiht.width,widthHeiht.height);
            Rectangle  rectangle = domElement.getBoundingClientRect();
            System.out.println(">>>>>" + rectangle.x + "|" + rectangle.y + "|" + rectangle.width + "|" + rectangle.height + "|" + widthHeiht.width + "|" + widthHeiht.height);
            domElement=domElement.findElement(By.xpath("h3/a"));
            Basic.move(browser, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            //browser.executeJavaScript("window.scrollTo(" + rectangle.x + "," + (rectangle.y-(100+(L.randomNumber(1,68)))) + ");");
            Basic.randomMove(browser,widthHeiht.width,widthHeiht.height);
            Basic.targetMove(browser,domElement);
            System.out.println("___________________________________________________________");
            domElement.scrollToBottom();
            rectangle = domElement.getBoundingClientRect();
            forwardMouseClickEvent(browser, PRIMARY, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
