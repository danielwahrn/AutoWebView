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

import com.github.ybq.android.spinkit.SpinKitView;


public class MainActivity extends Activity {
    ImageButton btnExit, btnSetting;
    SpinKitView spinner;
    SharedPreferences sharedPreferences;
    CountDownTimer downTimer;
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
                spinner.setVisibility(View.VISIBLE);
            }

            public void onFinish()
            {
                gotoWebViewPage();
            }
        }.start();
    }


    private void initComponents() {

        btnExit = findViewById(R.id.btn_Exit);
        btnSetting = findViewById(R.id.btn_setting);
        spinner = findViewById(R.id.spin_kit);

    }

    private void initListeners() {


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downTimer.cancel();
                finish();
                moveTaskToBack(true);
            }

        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downTimer.cancel();
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });


    }

    private void initSetting() {

//        CustomTabsIntent builder = new CustomTabsIntent.Builder().build();
//        builder.intent.putExtra(Intent.EXTRA_DONT_KILL_APP, true);
//        builder.intent.putExtra(CustomTabsIntent.EXTRA_ENABLE_URLBAR_HIDING, true);
//
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        String savedUri = sharedPreferences.getString("savedUri", "https://portal.avemeo.sk/box/macrosoft");
//        edURLName.setText(savedUri);
//
//        final ComponentName component = new ComponentName(this, WebViewActivity.class);
//        this.getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

    }
    private void gotoWebViewPage() {
        Setting setting = new Setting(getBaseContext());
        String url = setting.getURL();

        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "https://" + url;
        spinner.setVisibility(View.GONE);
        Intent browserIntent = new Intent(this, WebViewActivity.class);
        browserIntent.putExtra("uri", url);
        startActivity(browserIntent);

    }

    @Override
    protected void onPause() {
        super.onPause();
//// disable recent button
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//
//        activityManager.moveTaskToFront(getTaskId(), 0);
        downTimer.cancel();
    }

}
