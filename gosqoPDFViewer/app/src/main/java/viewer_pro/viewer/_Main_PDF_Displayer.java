package viewer_pro.viewer;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class _Main_PDF_Displayer extends AppCompatActivity {

    //사용자 객체 선언 부------------------------------------------------------------------------------------------------------------------------------------------
    private List<Swipe_ViewPagerAdapter> each_adapter_instance = new ArrayList<>(); //pdf 탭의 인스턴스 마다 할당 된 어뎁터를 저장
    private LayoutInflater inflater; //PDF 탭 을 커스터마이징 하기 위함
    private int current_Tab_index = 0; //현재 탭의 인덱스
    private Typeface nanumgothic;

    public static Swipe_ViewPager swipeViewPager;
    public static Context mainContext;

    ImageButton btn_topmenu_open, btn_topmenu_close, btn_color_picker, btn_add_tabs, btn_clear_tabs, btn_close_app;
    LinearLayout top_menu_ready, top_menu; //상단 기능 메뉴바
    TabLayout myPDFTabs;


    @Override //앱 실행시 onCreate 시작, 액티비티 생명주기 검색하여 참조
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._main_pdf_displayer);


        //사용자 객체와 XML 위젯 연결 부------------------------------------------------------------------------------------------------------------------------------------------
        mainContext = this;


        btn_topmenu_open = (ImageButton) findViewById(R.id.btn_topmenu_open);
        btn_topmenu_close = (ImageButton) findViewById(R.id.btn_topmenu_close);
        btn_color_picker = (ImageButton) findViewById(R.id.btn_color_picker);
        btn_add_tabs = (ImageButton) findViewById(R.id.btn_add_taps);
        btn_clear_tabs = (ImageButton) findViewById(R.id.btn_clear_taps);
        btn_close_app = (ImageButton) findViewById(R.id.btn_close_app);

        top_menu_ready = (LinearLayout) findViewById(R.id.top_menu_ready);
        top_menu = (LinearLayout) findViewById(R.id.top_menu);

        myPDFTabs = (TabLayout) findViewById(R.id.tab_PDF_Group);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        nanumgothic = Typeface.createFromAsset(getAssets(), "font/nanumgothic.ttf");






        //위젯 객체 기본 세팅 부------------------------------------------------------------------------------------------------------------------------------------------
        top_menu.setVisibility(View.INVISIBLE);
        swipeViewPager = (Swipe_ViewPager) findViewById(R.id.pdf_container);





        //앱 종료 버튼------------------------------------------------------------------------------------------------------------------------------------------------
        btn_close_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //데이타 처리 후 어플리 케이션 종료
            }
        });

        //탭 추가 기능 버튼 -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        btn_add_tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), _File_Finder_popup.class));
            }
        });

        //메뉴 on-----------------------------------------------------------------------------------------------------------------------------------------
        btn_topmenu_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                top_menu.setVisibility(View.VISIBLE);
                top_menu_ready.setVisibility(View.INVISIBLE);
            }
        });

        //메뉴 off-----------------------------------------------------------------------------------------------------------------------------------------
        btn_topmenu_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                top_menu.setVisibility(View.INVISIBLE);
                top_menu_ready.setVisibility(View.VISIBLE);
            }
        });

        //컬러 피커 Open-----------------------------------------------------------------------------------------------------------------------------------------
        btn_color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), _Paint_Settings_popup.class));
            }
        });

        //탭 모두 삭제 버튼 -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        btn_clear_tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPDFTabs.removeAllTabs(); //탭을 모두 삭제
                each_adapter_instance.clear(); //모든 어댑터를 삭제
                swipeViewPager.setAdapter(null); //ViewPager의 할당 어댑터를 삭제함
                /*                page_bar.setMax(0); //SeekBar의 최대 크기를 0으로.*/
                Log.d("tabcont_RemoveTabs", "          [CLEAR : Remove all tabs on myPDFTabs, INDEX NUMBER : ");
                /*                textview_pcount.setVisibility(View.INVISIBLE); //페이지 카운팅 안보임.*/
                Log.d("pagecount_setv", "INVISIBLE");
            }
        });






        //탭 셀렉트 리스너---------------------------------------------------------------------------------------------------------------------------------------------------------
        myPDFTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            int pageindex_value = 0;

            @Override
            public void onTabSelected(TabLayout.Tab tab) { //탭이 선택될때

                Swipe_ViewPagerAdapter selectedAdapter = each_adapter_instance.get(tab.getPosition());
                swipeViewPager.setAdapter(selectedAdapter); //현재 선택된 탭 인덱스 번호의 어댑터를 넣어 줌
                current_Tab_index = tab.getPosition();



                LinearLayout nowTab = (LinearLayout) tab.getCustomView(); //선택된 탭의 텍스트 색을 검정색으로 바꿈
                TextView nowTabTitle;
                nowTabTitle = nowTab.findViewById(R.id.tc_tab_title);
                nowTabTitle.setTextColor(Color.parseColor("#000000"));
                nowTabTitle.setTypeface(nanumgothic);


                Log.d("tabcont_SelectTabs", "          [SELECT : Select tab on myPDFtabs, INDEX : " + tab.getPosition() + ", TITLE : " + tab.getText() + "]");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { //탭 선택 상태가 해제시


                LinearLayout nowTab = (LinearLayout) tab.getCustomView(); //선택된 탭의 텍스트 색을 회색으로 바꿈
                if (nowTab != null) {
                    TextView nowTabTitle;
                    nowTabTitle = nowTab.findViewById(R.id.tc_tab_title);
                    nowTabTitle.setTextColor(Color.parseColor("#848484"));
                }

                Log.d("tabcont_SelectTabs", "          [UNSELECT : Select tab on myPDFtabs, INDEX : " + tab.getPosition() + ", TITLE : " + tab.getText() + "]");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }//onCreate







    public void getPDF(String filePath) {
        Log.d("getPDF", filePath);

        int parseCount = 0;

        File pdf = new File(filePath);
        String getTabname = pdf.getName();//.substring(0, pdf.getName().length()-4);
        String tabname = "";
        if (getTabname.getBytes().length > 20) {
            char [] tabNameToChar = getTabname.toCharArray();
            for(char word : tabNameToChar){
                String wordToString = Character.toString(word);
                tabname += wordToString;

                parseCount++;
                Log.d("tabNameCheck", parseCount+"     "+tabname);
                if(tabname.getBytes().length > 20){
                    break;
                }//if
            }//for
        }//if
        else{
            tabname = getTabname;
        }

        each_adapter_instance.add(new Swipe_ViewPagerAdapter(getSupportFragmentManager(), pdf.getAbsolutePath())); //어댑터 배열에 탭 인덱스 위치에 어댑터 추가. (페이지 매개 변수는 테스트 용)


        LinearLayout pdf_customtab = (LinearLayout) inflater.inflate(R.layout.pdf_customtab, null);

        final ImageButton pdf_tabclose;
        TextView pdf_tabtitle;

        pdf_tabclose = pdf_customtab.findViewById(R.id.tc_tab_clse); //탭 종료 버튼 선언 및 이벤트 처리
        pdf_tabtitle = pdf_customtab.findViewById(R.id.tc_tab_title); //탭 이름 선언, 문자열 값은 테스트 용임.
        pdf_tabtitle.setText(tabname); //탭 이름


        //탭 종료 버튼 (탭 추가시 생성, 이벤트 구현 필요)-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        pdf_tabclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View target = (View) v.getParent().getParent(); //GetTabIndex From TabLayout
                int index = ((ViewGroup) target.getParent()).indexOfChild(target);

                Log.v("tabcont_DelTabs", "          [Clicked Button's parent tap index = " + index + "]");


                //TabDeleteOptions
                if (index > 0 && index < myPDFTabs.getTabCount() && (index == current_Tab_index)) { //내가 지금 보고있는 탭을 지울 경우
                    Log.d("DelTabs", "          [내가 지금 보고 있는탭 지움]");

                    myPDFTabs.removeTabAt(index); //현재 탭 삭제
                    each_adapter_instance.remove(index); //현재 탭의 어뎁터 삭제


                }//if

                else if (index == 0 && (index == current_Tab_index) && (myPDFTabs.getTabCount() - 1) != 0) { //내가 지금 보고있는 탭이 0번이고 이 탭을 지울 경우
                    Log.d("DelTabs", "          [내가 지금 보고 있는탭이 0번, 지움]");

                    myPDFTabs.removeTabAt(index); //현재 탭 삭제
                    each_adapter_instance.remove(index); //현재 탭의 어뎁터 삭제

                }//else if

                else if (index != current_Tab_index) { //내가 지금 보고 있지 않은 탭을 지울 경우
                    Log.d("DelTabs", "          [내가 지금 보고있지 않은 탭을 지움]");
                    myPDFTabs.removeTabAt(index); //선택한 탭 삭제
                    each_adapter_instance.remove(index); //선택한 탭의 어뎁터 삭제
                }//else if

                else if (index == 0 && (myPDFTabs.getTabCount() - 1) == 0) {
                    Log.d("DelTabs", "          [내가 지금 보고있는 탭을 지움, 그 탭이 존재하는 유일한 탭 일 경우]");
                    myPDFTabs.removeTabAt(index); //선택한 탭 삭제
                    each_adapter_instance.remove(index); //선택한 탭의 어뎁터 삭제

                    swipeViewPager.setAdapter(null); //뷰 페이져의 어뎁터를 null값으로
                    Log.d("pagecount_setv", "INVISIBLE");

                }//else if
                //TabDeleteOptions

                index = 0;

            }//onClick
        });//Tab Delete Listner


        myPDFTabs.addTab(myPDFTabs.newTab().setCustomView(pdf_customtab)); //myPDFTabs -> 탭 인덱스 위치에 탭 생성
        Log.d("tabcont_AddTabs", "          [ADD : Add new tab on myPDFTabs, TAB'S INDEX NUMBER : ");
    }//getPDF
//읽기 권한 요청
}//class