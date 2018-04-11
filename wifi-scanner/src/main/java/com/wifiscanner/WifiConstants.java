package com.wifiscanner;

public interface WifiConstants {

    String PREFS_KEY = "prefs_sender_key";
    String PREFS_CLIENT_KEY = "client_key";
    String SENDER_PREFS_VALUE = "sender";
    String RECEIVER_PREFS_VALUE = "receiver";

    String ACTION_SEND_FILE = "com.an.wifi.scanner.SEND_FILE";

    String EXTRAS_DATA = "osta_data";
    String EXTRAS_MESSAGE = "status_message";
    String EXTRAS_GROUP_OWNER_PORT = "go_port";
    String EXTRAS_INET_ADDRESS = "inet_address";
    String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    String EXTRAS_RESULT_RECEIVER = "result_receiver";

    int STATUS_SUCCESS = 001;
    int STATUS_FAILURE = 003;
    int STATUS_CONNECTING = 002;

    int SOCKET_TIMEOUT = 5000;

    int DEFAULT_WIFI_PORT = 52287;
}
