package viewer_pro.viewer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class _Permission_Request_Page extends AppCompatActivity {

    TextView txtv_info;
    ImageButton ibtn_check_permission;
    Typeface type;
    public static Context mCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._permission_request_page);

        txtv_info = (TextView) findViewById(R.id.tv_info);
        ibtn_check_permission = (ImageButton) findViewById(R.id.btn_check_permission);

        type = Typeface.createFromAsset(getAssets(), "font/coolvetica.ttf");
        txtv_info.setTypeface(type);

        ibtn_check_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(_Permission_Request_Page.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        });

    }//onCreate

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case 1: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            startActivity(new Intent(_Permission_Request_Page.this, _Main_PDF_Displayer.class));
                            finish();
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }//run
                    }, 0);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Must to accept before using app.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }//onRequestPermissionsResult







}
