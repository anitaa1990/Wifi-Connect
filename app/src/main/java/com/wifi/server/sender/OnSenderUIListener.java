package com.wifi.server.sender;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;

import java.util.Map;

public interface OnSenderUIListener {


    void redirectToDeviceListScreen();

    /*
     * Device List Details
     * */
    void onScanStarted();
    void onDeviceAvailable(final WifiP2pDeviceList wifiP2pDeviceList);

    void redirectToProcessScreen(WifiP2pDevice wifiP2pDevice);
    void onConnectionCompleted();
    void onDataTransferStarted();
    void onDataTransferCompleted(String s);

    void redirectToSuccessScreen(int imageResourceId, String message);
}
