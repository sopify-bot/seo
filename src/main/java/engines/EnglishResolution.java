package engines;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import tool.L;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
public class EnglishResolution {


  private  static List<WidthHeiht> pcWidthHeight=null;
  private  static List<WidthHeiht> mobileWidthHeight=null;
  static {
      //pc 屏幕分辨率
      pcWidthHeight= new ArrayList<>();
      pcWidthHeight.add(new WidthHeiht(1280,800));
      pcWidthHeight.add(new WidthHeiht(1920,1200));
      pcWidthHeight.add(new WidthHeiht(1440,900));
      pcWidthHeight.add(new WidthHeiht(1680,1050));
      pcWidthHeight.add(new WidthHeiht(1920,1080));
      pcWidthHeight.add(new WidthHeiht(1280,1024));

      mobileWidthHeight= new ArrayList<>();
      mobileWidthHeight.add(new WidthHeiht(414,736));

  }

    public static int PC=1;
    public static int Mobile=0;


    public static WidthHeiht getEnglishResolution(int pcOrmobile){
            if(pcOrmobile==1){

                return  pcWidthHeight.get(L.randomNumber(0,(pcWidthHeight.size()-1)));
            }else{
                return  mobileWidthHeight.get(L.randomNumber(0,(mobileWidthHeight.size()-1)));
            }
    }

    @Accessors(chain = true)
    @AllArgsConstructor
    public  static  class WidthHeiht{
        @Getter@Setter
        int width;
        @Getter@Setter
        int height;
    }


}
