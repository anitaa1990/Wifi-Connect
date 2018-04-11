package com.wifi.server;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.wifi.server.utils.NavigatorUtils;

public class HomeActivity extends Activity implements View.OnClickListener {

    private Button btnPay, btnReceive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnPay = findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(this);

        btnReceive = findViewById(R.id.btn_receive);
        btnReceive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnPay) {
            NavigatorUtils.redirectToSenderScreen(this);

        } else if(v == btnReceive) {
            NavigatorUtils.redirectToReceiverScreen(this);
        }
    }
}
