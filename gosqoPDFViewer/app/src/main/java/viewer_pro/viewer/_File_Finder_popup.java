package viewer_pro.viewer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class _File_Finder_popup extends Activity { //다이얼 로그로 사용하기 위하여 액티비티를 상속

    private File_ListView_NavAdapter directoryListAdapter;
    private TextView directoryText;
    private String directoryPath;
    private Typeface nanumgothic;

    ListView directoryList;
    ImageButton btnClosePopup, btnSearchPdf;
    EditText editSearchPdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_finder_dialog);

        //위젯 및 변수 할당 코드-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        nanumgothic = Typeface.createFromAsset(getAssets(), "font/nanumgothic.ttf");

        directoryListAdapter = new File_ListView_NavAdapter(this, nanumgothic);
        directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        directoryList = findViewById(R.id.list_directory); //파일 목록을 표현하는 리스트 뷰
        directoryText = findViewById(R.id.text_directory); //파일 경로를 표현하는 텍스트 뷰

        btnClosePopup = findViewById(R.id.btn_close_popop);
        btnSearchPdf = findViewById(R.id.btn_search_pdf);

        editSearchPdf = findViewById(R.id.edit_search_pdf);


        directoryList.setAdapter(directoryListAdapter);
        directoryList.setOnItemClickListener(mItemClickListener);
        directoryText.setTypeface(nanumgothic);
        refreshFiles();


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //검색 버튼 ------------------------------------------------------------------------
        btnSearchPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSearchPdf.setClickable(false);
                directoryListAdapter.clearItem();
                searchFile(directoryPath, editSearchPdf.getText().toString());
                directoryListAdapter.notifyDataSetChanged();

                directoryText.setText("Search Result");
                btnSearchPdf.setClickable(true);
            }
        });

    }//onCreate






    //리스트뷰 클릭 리스너
    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // TODO Auto-generated method stub
            File_ListView_ListData clickedData = directoryListAdapter.getItem(position);//클릭된 위치의 값을 가져옴
            String name;


            //디렉토리이면
            if (clickedData.name.startsWith("[") && clickedData.name.endsWith("]")) {
                name = clickedData.name.substring(1, clickedData.name.length() - 1); //[]부분을 제거해줌
                String Path = directoryPath + "/" + name;
                directoryPath = Path;//현재를 Path로 바꿔줌
                refreshFiles();//리프레쉬
            } else if (clickedData.name.equals("UP TO PATH")) {
                String[] pathSplit = directoryPath.split("/");
                String[] pathSplitFinal = directoryPath.split("/" + pathSplit[pathSplit.length - 1]);
                directoryPath = pathSplitFinal[0];
                refreshFiles();//리프레쉬
            } else {//디렉토리가 아니면 토스트 메세지를 뿌림
                ((_Main_PDF_Displayer)_Main_PDF_Displayer.mainContext).getPDF(clickedData.path);
                finish();
            }
        }
    };






    public void refreshFiles() {

        String nowPath = directoryPath.replace(Environment.getExternalStorageDirectory().getAbsolutePath(), "Storage");
        directoryText.setText(nowPath);//현재 PATH를 가져옴
        directoryListAdapter.clearItem();//배열리스트를 지움

        File current = new File(directoryPath);//현재 경로로 File클래스를 만듬
        File[] fileLists = current.listFiles();


        //파일이 있다면?

        if (fileLists != null) {
            if (!directoryPath.equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                directoryListAdapter.addItem(getResources().getDrawable(R.drawable.up_to_path), "UP TO PATH", "");
            }
            //디렉토리만 먼저 넣음--------------------------------------------------------------------------------------------------------------------------------------------
            for (int index = 0; index < fileLists.length; index++) {
                if (fileLists[index].isDirectory()) {
                    Log.d("file_finder_dir", "directory find : " + fileLists[index].getName());
                    directoryListAdapter.addItem(getResources().getDrawable(R.drawable.folder), "[" + fileLists[index].getName() + "]", fileLists[index].getAbsolutePath());
                }//if
            }//for

            //디렉토리가 아닌 나머지를 가져옴---------------------------------------------------------------------------------------------------------------------------------
            for (int index = 0; index < fileLists.length; index++) {
                if (!fileLists[index].isDirectory()) {

                    int pos = fileLists[index].getName().lastIndexOf(".");
                    String ext = fileLists[index].getName().substring(pos + 1);


                    if (ext.equals("pdf")) {
                        Log.d("file_finder_pdf", "pdf file find : " + fileLists[index].getName());
                        directoryListAdapter.addItem(getResources().getDrawable(R.drawable.pdf), fileLists[index].getName(), fileLists[index].getAbsolutePath());
                    }//if
                    else {
                        Log.d("file_finder_ignore", "this file ignored : " + fileLists[index].getName());
                    }
                }//bigger if
            }//for
        }//if
        else {
            ActivityCompat.requestPermissions(_File_Finder_popup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        //다끝나면 리스트뷰를 갱신시킴
        directoryListAdapter.notifyDataSetChanged();

    }//refreshfiles






    public void searchFile(String nowPath, String fileName) {

        File nowDirectory = new File(nowPath);
        File[] fileList = nowDirectory.listFiles();

        for (File subFiles : fileList) {
            int position = subFiles.getName().lastIndexOf(".");
            String ext = subFiles.getName().substring(position + 1);

            if (subFiles.isFile() && ext.equals("pdf")) {//하위 파일이 pdf 파일 일 경우
                if(subFiles.getName().toLowerCase().contains(fileName)){//파일 이름이 검색어를 포함 할 경우
                    directoryListAdapter.addItem(getResources().getDrawable(R.drawable.pdf), subFiles.getName(), subFiles.getAbsolutePath());
                    Log.d("file_finder_search", "[FILE FOUND file name : " + subFiles.getName() + ", file path : " + subFiles.getAbsolutePath() + "]");
                }
                else{
                    Log.d("file_finder_search", "[FILE PASS file name : " + subFiles.getName() + ", file path : " + subFiles.getAbsolutePath() + "]");
                }
            }//if
            else if (subFiles.isDirectory()) {//디렉토리 일 경우
                Log.d("file_finder_search", "[DIRECTORY FOUND file name : " + subFiles.getName() + ", file path : " + subFiles.getAbsolutePath() + "]");
                searchFile(nowDirectory.getAbsolutePath()+"/"+subFiles.getName(), fileName);
                // 서브디렉토리가 존재하면 재귀적 방법으로 다시 탐색
            }//else if
        }//for
    }//search File



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case 1: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    finish();
                }
                else{
                    finish();
                }
            }
        }
    }//onRequestPermissionsResult



}//class
