package com.infoshare.bowlingscorecard;
  
import java.util.ArrayList;  
import java.lang.Runnable;  
import android.os.Handler;  
import android.widget.TextView;  
import android.content.Context;  
import android.widget.HorizontalScrollView;  
import android.widget.LinearLayout;  
import android.widget.RelativeLayout;

public class tickertape extends HorizontalScrollView {  
       
     private LinearLayout tickertape;  
     private int scrollMax;  
     private int scrollPos =     0;  
     private Handler handler;  
     private double speed = 2.0;  
       
     ArrayList<TextView> itemsList = new ArrayList<TextView>();  
     private String items[] ={
    		 "Use proper grip"
    		 ,"Concentrate on the target"
    		 ,"Approach in a straight line"
    		 ,"Keep your shoulders square to the target"
    		 ,"Use a smooth delivery"
    		 ,"Keep your arm close to your body"
    		 ,"Release the ball past the foul line"
    		 ,"Follow though to your target"
    		 ,"Don't try and throw too hard"
    		 ,"Don't approach too fast"};  
       
     /**  
      * CONSTRUCTOR  
      * @param context  
      */  
     public tickertape(Context context) {  
          super(context);  
            
          //SET PROPS  
          this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));  
          
          //ADD TICKERTAPE  
          tickertape = new LinearLayout(context);  
          tickertape.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));  
          this.addView(tickertape);  
            
          //ADD TEXT  
          for (int i=0;i<items.length;i++){  
             TextView txt = new TextView(context);  
      txt.setText(items[i]);  
      txt.setId(i);  
      txt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));  
  
      txt.setPadding(50, 0, 0, 0);   
      txt.setBackgroundColor(0x00FFFFFF);  
      tickertape.addView(txt);  
      itemsList.add(txt);  
        }  
          
          //disable scroll bar  
        this.setHorizontalScrollBarEnabled(false);  
          
        //set color  
    this.setBackgroundColor(0xffFFFFFF);  
          
    //start  
    getScrollMaxAmount();  
        startAutoScrolling();        
          
     }  
       
     /**  
      * GET SCROLL AMOUNT  
      * Sets scrollMax based upon first item in itemList  
      */  
     public void getScrollMaxAmount(){  
       scrollMax = itemsList.get(0).getWidth();  
  }  
         
     /**  
      * START AUTOSCROLL  
      * replaced TimerTask with Handler  
      */  
  public void startAutoScrolling(){  
         
       handler = new Handler();  
        Runnable runnable = new Runnable() {  
             public void run() {  
                 moveScrollView();  
                 //again  
              handler.postDelayed(this, 30);  
                
             }  
           };  
       handler.postDelayed(runnable, 30);  
     }  
    
  /**  
  * MOVE SCROLLVIEW  
  * scroll HorizontalScrollView  
  */  
  public void moveScrollView(){  
       scrollPos = (int) (this.getScrollX() + speed);  
          if(scrollPos >= scrollMax){  
               tickertape.removeView(itemsList.get(0));  
               tickertape.addView(itemsList.get(0));  
               itemsList.add(itemsList.get(0));  
               itemsList.remove(0);  
               getScrollMaxAmount();  
               scrollPos =     0;  
          }  
          this.scrollTo(scrollPos, 0);  
     }  
}  

