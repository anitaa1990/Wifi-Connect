package com.wifi.server.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
