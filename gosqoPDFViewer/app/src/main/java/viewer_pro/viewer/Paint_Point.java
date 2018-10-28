package viewer_pro.viewer;

import android.graphics.Color;

public class Paint_Point {

    private float x;
    private float y;
    private boolean check;
    private int color;

    public Paint_Point(float x, float y, boolean check, int color) {
        this.x = x;
        this.y = y;
        this.check = check;
        this.color = color;
    }

    public int getColor(){
        return color;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean getCheck(){
        return check;
    }
}
