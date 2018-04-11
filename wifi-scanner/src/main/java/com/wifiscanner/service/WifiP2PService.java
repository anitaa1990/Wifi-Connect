package com.wifiscanner.service;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;

public interface WifiP2PService {

    void onCreate();
    void onResume();
    void onStop();
    void onDestroy();

    void handleOnWifiStateChanged(int state);
    void initiateDiscovery();


    void connectDevice(WifiP2pDevice wifiP2pDevice);
    void disconnectDevice();

    void handleOnPeersChangedResponse();
    void handleOnPeerConnected();
    void handleOnPeerDisconnected();
    void handleOnDeviceStatusChanged(WifiP2pDevice wifiP2pDevice);

    void startDataTransfer(String message);

    void handleOnPeerServer(WifiP2pInfo wifiP2pInfo);
    void handleOnPeerClient(WifiP2pInfo wifiP2pInfo, String message);
}
