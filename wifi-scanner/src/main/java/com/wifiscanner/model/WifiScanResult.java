package com.wifiscanner.model;

public class WifiScanResult {

    private String ip;
    private String mac;
    public WifiScanResult(String ipAddress, String mac) {
        this.ip = ipAddress;
        this.mac = mac;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;
        WifiScanResult v = (WifiScanResult) o;
        return ip.equals(v.ip);
    }

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }
}
