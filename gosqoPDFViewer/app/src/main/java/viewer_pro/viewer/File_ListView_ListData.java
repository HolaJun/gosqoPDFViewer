package viewer_pro.viewer;

import android.graphics.drawable.Drawable;

public class File_ListView_ListData {

    public Drawable icon;
    public String name, path;

    public File_ListView_ListData(Drawable icon, String name, String path) {
        this.icon = icon;
        this.name = name;
        this.path = path;
    }
}
