package com.wifiscanner.listener;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;

public interface WifiP2PConnectionCallback {

    void onInitiateDiscovery();
    void onDiscoverySuccess();
    void onDiscoveryFailure();


    void onPeerAvailable(WifiP2pDeviceList wifiP2pDeviceList);
    void onPeerStatusChanged(WifiP2pDevice wifiP2pDevice);
    void onPeerConnectionSuccess();
    void onPeerConnectionFailure();
    void onPeerDisconnectionSuccess();
    void onPeerDisconnectionFailure();

    void onDataTransferring();
    void onDataTransferredSuccess();
    void onDataTransferredFailure();

    void onDataReceiving();
    void onDataReceivedSuccess(String s);
    void onDataReceivedFailure();
}
