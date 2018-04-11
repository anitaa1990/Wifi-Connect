package com.wifi.server.sender;


import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.wifi.server.R;
import com.wifi.server.base.BaseFragment;
import com.wifi.server.dialogs.AnimationView;


public class SenderFragment extends BaseFragment {

    private static final String INTENT_CONFIG = "wifi_config";

    private View connectView, hotspotView, dataView;
    private AnimationView connectGif, hotspotGif, dataGif;
    private TextView connectTxtView, hotspotTxtView, dataTxtView;

    private WifiP2pDevice wifiP2pDevice;
    public static SenderFragment newInstance(WifiP2pDevice wifiP2pDevice) {
        SenderFragment fragment = new SenderFragment();
        Bundle args = new Bundle();
        args.putParcelable(INTENT_CONFIG, wifiP2pDevice);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            wifiP2pDevice = getArguments().getParcelable(INTENT_CONFIG);
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return enter ?  AnimationUtils.loadAnimation(activity, R.anim.slide_in_right) : super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sender, container, false);

        connectView = rootView.findViewById(R.id.scan_progress);
        connectGif = rootView.findViewById(R.id.gif1);
        connectTxtView = rootView.findViewById(R.id.progress_txt);

        hotspotView = rootView.findViewById(R.id.scan_hotspot);
        hotspotGif = rootView.findViewById(R.id.gif2);
        hotspotTxtView = rootView.findViewById(R.id.progress_hotspot_txt);

        dataView = rootView.findViewById(R.id.scan_data);
        dataGif = rootView.findViewById(R.id.gif3);
        dataTxtView = rootView.findViewById(R.id.progress_data_txt);

        ((SenderActivity)activity).wifiP2PService.connectDevice(wifiP2pDevice);
        onConnectStarted();

        return rootView;
    }


    public void onConnectStarted() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectView.setVisibility(View.VISIBLE);
                connectGif.setMovieResource(R.drawable.loader_2);
            }
        });
    }

    public void onConnectCompleted() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectGif.setVisibility(View.INVISIBLE);
                connectTxtView.setText(activity.getString(R.string.sender_connected));
            }
        });
    }

    public void onHotspotStarted() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hotspotView.setVisibility(View.VISIBLE);
                hotspotGif.setMovieResource(R.drawable.loader_2);
            }
        });
    }

    public void onHotspotCompleted() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hotspotGif.setVisibility(View.INVISIBLE);
                hotspotTxtView.setText(activity.getString(R.string.sender_hotspot_success));
            }
        });
    }

    public void onDataStarted() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataView.setVisibility(View.VISIBLE);
                dataGif.setMovieResource(R.drawable.loader_2);
            }
        });
    }
}
