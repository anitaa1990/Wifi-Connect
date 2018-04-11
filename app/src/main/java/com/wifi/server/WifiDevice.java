package com.wifi.server;

import android.net.wifi.p2p.WifiP2pDevice;

public class WifiDevice extends WifiP2pDevice {

    private transient boolean isConnected;

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
