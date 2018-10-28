package viewer_pro.viewer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by gosqo on 2017-07-21.
 */

class Paint_Painter extends View {

    //페인트 모드 변수
    public static int strokeWidth = 10;
    public static String strokeColor = "ff2d3c";

    ArrayList <Paint_Point> drawList = new ArrayList<>();







    public Paint_Painter(Context context) {
        super(context);
    }



    public Paint_Painter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);

        for(int index=1; index<drawList.size(); index++){
            paint.setColor(drawList.get(index).getColor());
            if(!drawList.get(index).getCheck()){
                continue;
            }
            canvas.drawLine(drawList.get(index-1).getX(), drawList.get(index-1).getY(), drawList.get(index).getX(), drawList.get(index).getY(), paint);
        }
    }//onDraw



    public void clearPaint(){
        drawList.clear();
        invalidate();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d("pt_touched", "touch");

        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawList.add(new Paint_Point(x, y, false, Color.parseColor("#"+strokeColor)));
                Log.d("pt_Saving_xy", "          [SAVE : moving coordinate");
                break;
            case MotionEvent.ACTION_MOVE:
                if(_Stat_Storage.isPaintEnabled) {
                    drawList.add(new Paint_Point(x, y, true, Color.parseColor("#"+strokeColor)));
                    Log.d("pt_Saving_xy", "          [SAVE : make line");
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }//switch
        invalidate();
        return true;
    }//onTouch

}//class