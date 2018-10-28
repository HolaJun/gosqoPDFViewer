package viewer_pro.viewer;

import android.widget.SeekBar;
import android.widget.TextView;

public class PDF_View_PageChangeListener implements com.github.barteksc.pdfviewer.listener.OnPageChangeListener  {
    TextView tv;
    SeekBar skb;



    public void getViews(TextView tv, SeekBar skb){
        this.tv = tv;
        this.skb = skb;
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        tv.setText((page+1)+"/"+pageCount);
        skb.setMax(pageCount-1);
        skb.setProgress(page);
        _Stat_Storage.lastPage = page;
    }
}
