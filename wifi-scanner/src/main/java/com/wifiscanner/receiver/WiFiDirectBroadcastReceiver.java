package com.wifiscanner.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

import com.wifiscanner.service.WifiP2PServiceImpl;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2PServiceImpl wifiP2PService;
    public WiFiDirectBroadcastReceiver(WifiP2PServiceImpl wifiP2PService) {
        this.wifiP2PService = wifiP2PService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                wifiP2PService.handleOnWifiStateChanged(state);
                break;

            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                // request available peers from the wifi p2p manager. This is an
                // asynchronous call and the calling activity is notified with a
                // callback on PeerListListener.onPeersAvailable()
                wifiP2PService.handleOnPeersChangedResponse();
                break;


            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                // Broadcast when a device's details have changed,
                // such as the device's name
                WifiP2pDevice wifiP2pDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                wifiP2PService.handleOnDeviceStatusChanged(wifiP2pDevice);
                break;


            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if(networkInfo.isConnected()) wifiP2PService.handleOnPeerConnected();
                else wifiP2PService.handleOnPeerDisconnected();
                break;
        }
    }
}
