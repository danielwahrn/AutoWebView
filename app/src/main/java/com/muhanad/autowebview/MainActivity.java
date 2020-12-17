package com.muhanad.autowebview;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.browser.customtabs.CustomTabsIntent;


public class MainActivity extends Activity {
    Button btnEnter;
    Button btnStop;
    ImageButton btnExit, btnSetting;
    EditText edURLName;
    TextView txtTimer;
    CountDownTimer downTimer;
    FrameLayout loading;
    SharedPreferences sharedPreferences;
//    private HomeKeyLocker mHomeKeyLocker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        initListeners();

        initSetting();

        initCountDownTimer();

    }

    private void initCountDownTimer() {
        downTimer  = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
//                txtTimer.setText(""+ millisUntilFinished / 1000);
                loading.setVisibility(View.VISIBLE);
            }

            public void onFinish()
            {
                gotoWebViewPage();
            }
        }.start();
    }


    private void initComponents() {

        btnEnter = findViewById(R.id.btn_Enter);
        btnStop = findViewById(R.id.btn_Stop);
        btnExit = findViewById(R.id.btn_Exit);
        btnSetting = findViewById(R.id.btn_setting);
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        txtTimer = findViewById(R.id.txt_Timer);

        edURLName = findViewById(R.id.ed_URL_Name);
        edURLName.setText("https://portal.avemeo.sk/box/macrosoft");

    }

    private void initListeners() {
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWebViewPage();
            }

        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downTimer.cancel();
                finish();
                moveTaskToBack(true);
            }

        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               downTimer.cancel();
            }

        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downTimer.cancel();
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        edURLName.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if(downTimer != null) {
                     downTimer.cancel();
                 }
             }

             @Override
             public void afterTextChanged(Editable s) {
                 SharedPreferences.Editor editor = sharedPreferences.edit();
                 String str = s.toString();
                 editor.putString("savedUri", s.toString());
                 editor.commit();

             }
         }

        );
    }

    private void initSetting() {

        CustomTabsIntent builder = new CustomTabsIntent.Builder().build();
        builder.intent.putExtra(Intent.EXTRA_DONT_KILL_APP, true);
        builder.intent.putExtra(CustomTabsIntent.EXTRA_ENABLE_URLBAR_HIDING, true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String savedUri = sharedPreferences.getString("savedUri", "https://portal.avemeo.sk/box/macrosoft");
        edURLName.setText(savedUri);

        final ComponentName component = new ComponentName(this, WebViewActivity.class);
        this.getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

    }
    private void gotoWebViewPage() {
        Setting setting = new Setting(getBaseContext());
        String url = setting.getURL();

//                webView.getSettings().setLoadsImagesAutomatically(true);
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//                webView.loadUrl(url);
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "https://" + url;
///chrome view
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        startActivity(browserIntent);
        loading.setVisibility(View.GONE);
        Intent browserIntent = new Intent(this, WebViewActivity.class);
        browserIntent.putExtra("uri", url);
        startActivity(browserIntent);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {///disable back button
            Log.d("Test", "Back button pressed!");
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
// disable recent button
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

}
