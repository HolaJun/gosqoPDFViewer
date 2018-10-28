package viewer_pro.viewer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

public class _SplashActivity extends AppCompatActivity {

    TextView tv_gosqo, tv_pdf, tv_viewer;
    Typeface coolvetica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._splash);

        coolvetica = Typeface.createFromAsset(getAssets(), "font/coolvetica.ttf");
        tv_gosqo = (TextView) findViewById(R.id.tv_gosqo);
        tv_gosqo.setTypeface(coolvetica);
        tv_pdf = (TextView) findViewById(R.id.tv_pdf);
        tv_pdf.setTypeface(coolvetica);
        tv_viewer = (TextView) findViewById(R.id.tv_viewer);
        tv_viewer.setTypeface(coolvetica);

        checkPermission();

    }//onCreate

    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//퍼미션이 없음
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {//사용자가 권한을 임의로 취소 시킨 경우
                Log.d("permission_ask", "permission loose by user, move to permission request activity");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(_SplashActivity.this, _Permission_Request_Page.class));
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }//run
                }, 3000);

            }
            else {//권한이 없을 경우
                Log.d("permission_ask", "permission not found, move to permission request activity");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(_SplashActivity.this, _Permission_Request_Page.class));
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }//run
                }, 3000);
            }
        }//bigger if
        else {//퍼미션이 있음
            Log.d("permission_ask", "permission was accepted, move to Main");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(_SplashActivity.this, _Main_PDF_Displayer.class));
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }//run
            }, 3000);
        }

    }//checkPermission




}//class
