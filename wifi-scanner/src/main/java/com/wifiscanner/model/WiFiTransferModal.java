package com.wifiscanner.model;

import java.io.Serializable;

public class WiFiTransferModal implements Serializable {

    private String message;
    private String inetAddress;

    public WiFiTransferModal(String message, String inetAddress) {
        this.message = message;
        this.inetAddress = inetAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(String inetAddress) {
        this.inetAddress = inetAddress;
    }

    @Override
    public String toString() {
        return "message: " + message + ", inetAddress: " + inetAddress;
    }
}
