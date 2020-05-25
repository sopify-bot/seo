package main;

import engines.SoGou;
import tool.FileOperation;

public class Main {

    /***
     * 入口函数，运行哪个就把哪个注释解掉;
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("*******************************************");
        System.out.println("*******************************************");
        System.out.println("开始进入刷点击环节..."+ FileOperation.version);
        System.out.println("*******************************************");
        /***
         * 百度快排
         */
       // Baidu.main(args);
        /***
         * 360快排
         */
       // Rank360.main(args);
        /***
         * 搜狗快排
         */
        SoGou.main(args);

    }
}
