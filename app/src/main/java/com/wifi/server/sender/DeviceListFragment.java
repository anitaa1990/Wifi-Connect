package com.wifi.server.sender;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.wifi.server.R;
import com.wifi.server.RecyclerItemClickListener;
import com.wifi.server.WifiListAdapter;
import com.wifi.server.base.BaseFragment;
import com.wifi.server.dialogs.AnimationView;

import java.util.ArrayList;

public class DeviceListFragment extends BaseFragment implements RecyclerItemClickListener.OnItemClickListener {


    private View progressView;
    private TextView emptyView;
    private AnimationView animationView;

    private RecyclerView recyclerView;
    private WifiListAdapter wifiListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static DeviceListFragment newInstance() {
        DeviceListFragment fragment = new DeviceListFragment();
        return fragment;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return enter ?  AnimationUtils.loadAnimation(activity, R.anim.slide_in_right) : super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_list, container, false);

        progressView = rootView.findViewById(R.id.scan_progress);
        animationView = rootView.findViewById(R.id.gif1);


        recyclerView = rootView.findViewById(R.id.scanned_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        wifiListAdapter = new WifiListAdapter(activity, new ArrayList<WifiP2pDevice>());
        recyclerView.setAdapter(wifiListAdapter);

        emptyView = rootView.findViewById(R.id.empty_view);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);

        ((SenderActivity)activity).wifiP2PService.onCreate();
        ((SenderActivity)activity).wifiP2PService.onResume();

        return rootView;
    }

    public void onScanStarted() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressView.setVisibility(View.VISIBLE);
                animationView.setMovieResource(R.drawable.loader_2);
            }
        });
    }

    public void onScanCompleted() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressView.setVisibility(View.GONE);
            }
        });
    }

    public void onDeviceAvailable(final WifiP2pDeviceList wifiP2pDeviceList) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(recyclerView.isEnabled()) {
                    progressView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    wifiListAdapter.addDevices(new ArrayList<>(wifiP2pDeviceList.getDeviceList()));
                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(activity, DeviceListFragment.this));
                }
            }
        });
    }

    public void onNoDeviceAvailable() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(wifiListAdapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                } else {
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        ((SenderActivity)activity).redirectToProcessScreen(wifiListAdapter.getItem(position));
        recyclerView.setEnabled(false);
    }
}
