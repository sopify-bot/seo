package tool;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.awt.*;
import java.sql.Time;
import java.util.*;
import java.util.List;

public class L
{
   public static int randomNumber(int START,int END){
       if(END<=START)
           return START;
       Random random = new Random();
       return random.nextInt(END - START + 1) + START;
   }

    public static int randomNumber(Integer... e){
        return Arrays.asList(e).get(L.randomNumber(0,e.length-1));
    }



    public static long rm(){
       //6291457+>128+128
        //2097408+128+128
        //+256
       int rs=L.randomNumber(2,1000);
       long temp=3L;
       long lastN=56623104L;
       long c=2097152;
        for(int i=1;i<rs;i++){
            temp=temp*3;
            lastN=temp;
            System.out.println(Math.abs(temp*c));
        }
       return Math.abs(lastN*c)+L.randomNumber(0,1);
   }
    public static long rm1(){
        long c=2097152L,temp=0L;
        for(int i=1;i<L.randomNumber(1,256);i++){
            temp+=256;
        }
        return c+temp+L.randomNumber(0,1);
    }

    /**
     * 点击率模拟
     * @param c
     * @return
     * 1   ,2   ,3,4,5,6
     * 100/n
     */
   public static boolean isClick(String c){
        c= RegexParse.baseParse(c,"click=([\\S]*?);",1);
        HashMap<Integer,Boolean> map=new HashMap<>();
        for(int i=1;i<Integer.valueOf(c.split("/")[0])+1;i++)
            map.put(i,true);
        for(int i=Integer.valueOf(c.split("/")[0])+1;i<101;i++)
            map.put(i,false);
         int R=L.randomNumber(1,100);
        if(map.get(R)==null)
            return false;
        return map.get(R);
   }
    /**
     * 词的权重，每个词被分配到的概率不一样，比如dnf是大词多大，dnf公益服是小词少；
     * @return
     * 20,80,10,30,50
     * 20,100,110,140,190;
     */
    public static String shenChoose(List<String> stringList){
        LinkedHashMap<Integer,String> hashMap=new LinkedHashMap<>();
        int max=0;
        for(String temp:stringList)
        {
            max+=Integer.parseInt(RegexParse.baseParse(temp,"bigWord=([\\d]*+);",1));
            hashMap.put(max,temp);
        }
        int R=L.randomNumber(1,max);
        for(Integer t:hashMap.keySet()){
            if(R<=t)
                return hashMap.get(t);
        }
        return  null;
    }




    private static String TTid=null;
    private static int TTh=0,TTshowNum=0,TTchickNum=0;




    @Accessors(chain = true)
    static  class Task{
        @Getter
        @Setter
        int H;
        @Getter
        @Setter
        int clickNum;
        @Getter
        @Setter
        int showNum;
    }




}
