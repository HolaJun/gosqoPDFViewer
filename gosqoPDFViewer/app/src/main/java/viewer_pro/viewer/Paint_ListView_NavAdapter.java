package viewer_pro.viewer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Paint_ListView_NavAdapter extends BaseAdapter {

    private Context listContext = null;
    private ArrayList<Paint_ListView_ListData> dataList = new ArrayList<>();
    private Typeface nanumgothic;


    public Paint_ListView_NavAdapter(Context listContext, Typeface nanumgothic) {
        super();
        this.listContext = listContext;
        this.nanumgothic = nanumgothic;
    }

    public void addItem(Drawable icon, Drawable check, String colorCode){
        Paint_ListView_ListData newData = new Paint_ListView_ListData(icon, check, colorCode);
        dataList.add(newData);
    }

    public void clearItem(){
        dataList.clear();
    }

    @Override
    public int getCount() {
        return dataList.size();

    }

    @Override
    public Paint_ListView_ListData getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Paint_ListView_Holder holder;
        if(convertView == null){
            holder = new Paint_ListView_Holder();
            LayoutInflater layoutMaker = (LayoutInflater) listContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutMaker.inflate(R.layout.paint_setting_list_layout, null);

            holder.listIcon = convertView.findViewById(R.id.showcoloritems);
            holder.listColorCode = convertView.findViewById(R.id.showcolorcodes);

            convertView.setTag(holder);
        }//if
        else{
            holder = (Paint_ListView_Holder) convertView.getTag();
        }

        Paint_ListView_ListData thisData = dataList.get(i);

        if(thisData.icon != null){

            thisData.icon.setColorFilter(Color.parseColor("#" + thisData.colorCode), PorterDuff.Mode.SRC);
            holder.listIcon.setBackground(thisData.icon);
            holder.listIcon.setVisibility(View.VISIBLE);
            holder.listIcon.setImageDrawable(thisData.check);
        }
        else{
            holder.listIcon.setVisibility(View.GONE);
        }

        holder.listColorCode.setText("#"+thisData.colorCode.toUpperCase());
        holder.listColorCode.setTypeface(nanumgothic);

        return convertView;
    }
}
