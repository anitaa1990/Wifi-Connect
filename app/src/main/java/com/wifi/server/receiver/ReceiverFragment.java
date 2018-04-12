package com.wifi.server.receiver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wifi.server.R;
import com.wifi.server.base.BaseFragment;
import com.wifi.server.dialogs.AnimationView;
import com.wifi.server.utils.Utils;


public class ReceiverFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    private Button btnSend;
    private EditText senderTxt;

    private View connectView, hotspotView;
    private AnimationView connectGif, hotspotGif;
    private TextView connectTxtView, hotspotTxtView;

    public static ReceiverFragment newInstance() {
        ReceiverFragment fragment = new ReceiverFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_receiver, container, false);

        connectView = rootView.findViewById(R.id.scan_progress);
        connectGif = rootView.findViewById(R.id.gif1);
        connectTxtView = rootView.findViewById(R.id.progress_txt);

        hotspotView = rootView.findViewById(R.id.scan_hotspot);
        hotspotGif = rootView.findViewById(R.id.gif2);
        hotspotTxtView = rootView.findViewById(R.id.progress_hotspot_txt);

        btnSend = rootView.findViewById(R.id.btn_send);
        btnSend.setEnabled(false);
        btnSend.setOnClickListener(this);

        senderTxt = rootView.findViewById(R.id.sender_txt);
        senderTxt.addTextChangedListener(this);

        return rootView;
    }


    public void onServerConnectStarted() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectView.setVisibility(View.VISIBLE);
                connectGif.setMovieResource(R.drawable.loader_2);
            }
        });
    }

    public void onServerConnectSuccess() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectGif.setVisibility(View.INVISIBLE);
                connectTxtView.setText(activity.getString(R.string.sender_connected));
                onDataTransferStarted();
            }
        });
    }

    public void onServerConnectFailed() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showMessage(activity, activity.getString(R.string.receiver_error_in_connecting));
                connectView.setVisibility(View.VISIBLE);
                connectTxtView.setText(getString(R.string.receiver_init));
            }
        });
    }

    public void onDataTransferStarted() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hotspotView.setVisibility(View.VISIBLE);
                hotspotGif.setMovieResource(R.drawable.loader_2);
                hotspotTxtView.setText(activity.getString(R.string.receiver_confirmation));
            }
        });
    }


    public String getMessage() {
        return senderTxt.getText().toString();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(senderTxt.getText().toString().length() > 0) {
            btnSend.setEnabled(true);
            btnSend.setBackgroundColor(ContextCompat.getColor(activity, R.color.btn_green_bg));

        } else btnSend.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable editable) { }
    @Override
    public void onClick(View view) {
        if(view == btnSend) {
            senderTxt.setEnabled(false);
            btnSend.setEnabled(false);
            onServerConnectStarted();
        }
    }
}