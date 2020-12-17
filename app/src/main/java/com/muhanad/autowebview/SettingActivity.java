package com.muhanad.autowebview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    Button btnSave;
    EditText editURL;
    Setting setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setting = new Setting(getBaseContext());

        btnSave = findViewById(R.id.btn_save);
        editURL = findViewById(R.id.edit_url);
        editURL.setText(setting.getURL());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.setURL(editURL.getText().toString());
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
            }
        });
    }
}
