package viewer_pro.viewer;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

public class PDF_View_OnDrawListener implements com.github.barteksc.pdfviewer.listener.OnDrawListener{

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
        canvas.drawColor(Color.BLUE);
    }
}
