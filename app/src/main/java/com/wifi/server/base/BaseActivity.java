package com.wifi.server.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.wifi.server.R;

public class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected TextView toolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initToolBar() {
        toolbar = findViewById(R.id.tool_bar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
    }

    public void updateTitle(String title) {
        toolbarTitle.setText(title);
    }
}
