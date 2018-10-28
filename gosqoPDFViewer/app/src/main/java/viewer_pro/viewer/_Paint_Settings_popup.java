package viewer_pro.viewer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static viewer_pro.viewer._Main_PDF_Displayer.swipeViewPager;

public class _Paint_Settings_popup extends Activity { //다이얼 로그로 사용하기 위하여 액티비티를 상속

    //사용자 객체 선언 부----------------------------------------------------------------------------------
    private String[] colorcodes = {"FFADC5", "CCD1FF", "FFDDA6", "FC9EBD", "FFCCCC", "A8C8F9", "ff2d3c", "ff9500", "ffd200", "4cd901", "6de0ff", "5882fa", "606060", "110000"}; //컬러코드 문자열 배열
    private Paint_ListView_NavAdapter colorListAdapter;
    private int paint_Size, colorListPosition;
    private String usedColorCode;
    private Typeface nanumgothic;

    TextView tv_paint_size, tv_popup_title;
    SeekBar skbr_paint_adjust;

    ListView colorList;

    ImageButton btn_close_popup;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint_setting_dialog);

        //사용자 객체와 XML 위젯 연결 부------------------------------------------------------------------
        tv_paint_size = findViewById(R.id.tv_paintsize);
        tv_popup_title = findViewById(R.id.tv_popup_title);
        skbr_paint_adjust = findViewById(R.id.skbr_paint_adjust);

        btn_close_popup = findViewById(R.id.btn_close_colorpopup);

        colorList = findViewById(R.id.list_color_select);

        nanumgothic = Typeface.createFromAsset(getAssets(), "font/nanumgothic.ttf");

        colorListAdapter = new Paint_ListView_NavAdapter(this, nanumgothic);
        tv_paint_size.setTypeface(nanumgothic);
        tv_popup_title.setTypeface(nanumgothic);




        //GettingStatsFromSharedPreference---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        SharedPreferences static_stats = getSharedPreferences("stats", MODE_PRIVATE);

        usedColorCode = static_stats.getString("Save_color_code", "");
        paint_Size = static_stats.getInt("Save_paint_size", 0);

        boolean isMatched = false;

        for(int index=0; index<colorcodes.length; index++){
            if(usedColorCode.equals(colorcodes[index])){
                colorListPosition = index;
                isMatched = true;
                colorListAdapter.clearItem();
                refreshColorLists();
            }
        }
        if(!isMatched){
            colorListPosition = -1;
            colorListAdapter.clearItem();
            refreshColorLists();
        }

        Log.d("pop_sp_LoadColorStat", "          [LOAD : Color Stat Loaded From SharedPreference, Loaded Value : " + usedColorCode + "]");
        Log.d("pop_sp_LoadPaintSize", "          [LOAD : Paint Size Loaded From SharedPreference, Loaded Value : " + Integer.toString(paint_Size) + "]");
        Log.d("pop_sp_LoadedValues", "          [LOAD : All Values Are Loaded On Activity]");






        //위젯 객체 및 액티비티 기본 세팅 부--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        tv_paint_size.setText(Integer.toString(paint_Size+1));
        colorListAdapter.clearItem();
        colorList.setAdapter(colorListAdapter);
        colorList.setOnItemClickListener(colorListClickListener);

        refreshColorLists();






        //SettingsSeekBarProgressStat--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        skbr_paint_adjust.setProgress(paint_Size);
        skbr_paint_adjust.getProgressDrawable().setColorFilter( Color.WHITE, PorterDuff.Mode.SRC_IN );
        tv_paint_size.setText(Integer.toString(paint_Size+1));
        Log.d("pop_SeekBarAutoSet", "[ SeekBar AutoSetted, Sizes : " + skbr_paint_adjust.getProgress() + "]");



        //버튼 이벤트 ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        btn_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //SeekBar_PaintSizeAdjust-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        skbr_paint_adjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            //MoveThumbs
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                paint_Size = skbr_paint_adjust.getProgress();
                tv_paint_size.setText(Integer.toString(paint_Size+1));

                Log.d("pop_SeekBarChangeEvent", "          [onProgressChanged Progress : " + Integer.toString(skbr_paint_adjust.getProgress()) + ", Progress Sended : " + Integer.toString(paint_Size) + " ]");

            }

            //StartTouch
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //StopTouch
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }//onCreate----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------











    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences static_stats = getSharedPreferences("stats", MODE_PRIVATE);
        SharedPreferences.Editor static_statsEdit = static_stats.edit();

        static_statsEdit.putString("Save_color_code", colorcodes[colorListPosition]);
        static_statsEdit.putInt("Save_paint_size", paint_Size);
        static_statsEdit.apply();
        static_statsEdit.commit();


        Paint_Painter.strokeColor = colorcodes[colorListPosition];
        Paint_Painter.strokeWidth = paint_Size;


        Log.d("pop_pnt_SendColorStat", "          [SEND : ColorCode Sended to frg_paint_section " + "(#" + Integer.toHexString(colorListPosition) + ") ]");
        Log.d("pop_pnt_SendPaintSize", "          [SEND : PaintSize Sended to frg_paint_section " + "(" + Integer.toString(paint_Size) + ") ]");

        Log.d("pop_sp_SaveColorStat", "          [SAVE : Color Code Saved On SharedPreference, Saved Value : " + colorcodes[colorListPosition] + ")]");
        Log.d("pop_sp_SavePaintSize", "          [SAVE : Paint Size Saved On SharedPreference, Saved Value : " + paint_Size + "]");
        Log.d("pop_sp_SaveValues", "          [SAVE : All Values Are Saved On SharedPreference]");
    }






    AdapterView.OnItemClickListener colorListClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // TODO Auto-generated method stub
            colorListPosition = position;
            colorListAdapter.clearItem();
            refreshColorLists();
        }
    };






    public void refreshColorLists() {

        for(int index = 0; index<colorcodes.length; index++){
            if(index == colorListPosition) {
                colorListAdapter.addItem(getResources().getDrawable(R.drawable.null_colorcircle), getResources().getDrawable(R.drawable.mycheck), colorcodes[index]);
            }
            else{
                colorListAdapter.addItem(getResources().getDrawable(R.drawable.null_colorcircle), null, colorcodes[index]);
            }
        }

        //다끝나면 리스트뷰를 갱신시킴
        colorListAdapter.notifyDataSetChanged();

    }//refreshfiles

}//Paint_Settings_popu