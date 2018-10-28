package viewer_pro.viewer;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class File_ListView_NavAdapter extends BaseAdapter {

    private Context listContext = null;
    private ArrayList<File_ListView_ListData> dataList = new ArrayList<>();
    private Typeface nanumgothic;


    public File_ListView_NavAdapter(Context listContext, Typeface nanumgothic) {
        super();
        this.listContext = listContext;
        this.nanumgothic = nanumgothic;
    }

    public void addItem(Drawable icon, String name, String path){
        File_ListView_ListData newData = new File_ListView_ListData(icon, name, path);
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
    public File_ListView_ListData getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        File_ListView_Holder holder;
        if(convertView == null){
            holder = new File_ListView_Holder();
            LayoutInflater layoutMaker = (LayoutInflater) listContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutMaker.inflate(R.layout.file_finder_list_layout, null);

            holder.listIcon = (ImageView) convertView.findViewById(R.id.showicons);
            holder.listName = (TextView) convertView.findViewById(R.id.showitems);

            convertView.setTag(holder);
        }//if
        else{
            holder = (File_ListView_Holder) convertView.getTag();
        }

        File_ListView_ListData thisData = dataList.get(i);

        if(thisData.icon != null){
            holder.listIcon.setVisibility(View.VISIBLE);
            holder.listIcon.setImageDrawable(thisData.icon);
        }
        else{
            holder.listIcon.setVisibility(View.GONE);
        }

        holder.listName.setText(thisData.name);
        holder.listName.setTypeface(nanumgothic);

        return convertView;
    }
}
