package viewer_pro.viewer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by gosqo on 2017-07-25.
 */

public class Swipe_ViewPagerAdapter extends FragmentStatePagerAdapter {


    private String path;

    //파라미터 -> FragManager, Pager 최대값
    Swipe_ViewPagerAdapter(FragmentManager fm, String path) {
        super(fm);
        this.path = path;
    }

    @Override
    public Fragment getItem(int position) {
        //현재 페이지 텍스트 뷰 에 1만큼 더한 텍스트 뷰를 갖는 페이지를 생성함.
        return _PDF_View_Fragment.newInstance(path);
    }

    @Override
    public int getCount() {
        return 1;
    }
}
