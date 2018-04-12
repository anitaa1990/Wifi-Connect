package com.wifiscanner.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.wifiscanner.listener.WifiP2PConnectionCallback;
import com.wifiscanner.receiver.WiFiDirectBroadcastReceiver;
import com.wifiscanner.server.ServerDataService;
import com.wifiscanner.tasks.DataServerTask;
import com.wifiscanner.utils.PreferenceUtils;
import com.wifiscanner.utils.Utils;

import java.lang.reflect.Method;

import static com.wifiscanner.WifiConstants.ACTION_SEND_FILE;
import static com.wifiscanner.WifiConstants.DEFAULT_WIFI_PORT;
import static com.wifiscanner.WifiConstants.EXTRAS_DATA;
import static com.wifiscanner.WifiConstants.EXTRAS_GROUP_OWNER_ADDRESS;
import static com.wifiscanner.WifiConstants.EXTRAS_GROUP_OWNER_PORT;
import static com.wifiscanner.WifiConstants.EXTRAS_MESSAGE;
import static com.wifiscanner.WifiConstants.EXTRAS_RESULT_RECEIVER;
import static com.wifiscanner.WifiConstants.PREFS_CLIENT_KEY;
import static com.wifiscanner.WifiConstants.RECEIVER_PREFS_VALUE;
import static com.wifiscanner.WifiConstants.SENDER_PREFS_VALUE;
import static com.wifiscanner.WifiConstants.STATUS_CONNECTING;
import static com.wifiscanner.WifiConstants.STATUS_FAILURE;
import static com.wifiscanner.WifiConstants.STATUS_SUCCESS;

public class WifiP2PServiceImpl implements WifiP2PService {

    private Activity activity;

    private WifiP2pManager manager;
    private WifiManager wifiManager;
    private WifiP2pManager.Channel channel;

    private BroadcastReceiver wifiDirectBroadcastReceiver;
    private WifiP2PConnectionCallback wifiP2PConnectionCallback;

    private WifiP2PServiceImpl(Builder builder) {
        this.activity = builder.activity;
        this.wifiP2PConnectionCallback = builder.wifiP2PConnectionCallback;
        PreferenceUtils.setStringValues(activity, PREFS_CLIENT_KEY, builder.prefsKey);
    }

    @Override
    public synchronized void onCreate() {
        manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(activity.getApplicationContext(), activity.getMainLooper(), null);
        this.wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public synchronized void onResume() {
        if (!wifiManager.isWifiEnabled()) wifiManager.setWifiEnabled(true);
        wifiDirectBroadcastReceiver = new WiFiDirectBroadcastReceiver(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        activity.registerReceiver(wifiDirectBroadcastReceiver, intentFilter);
    }

    @Override
    public synchronized void onStop() {
        try {
            activity.unregisterReceiver(wifiDirectBroadcastReceiver);
            deletePersistentGroups();
            disconnectDevice();
        } catch (Exception e) {}
    }

    @Override
    public synchronized void onDestroy() {

    }

    @Override
    public synchronized void handleOnWifiStateChanged(int state) {
        if(wifiP2PConnectionCallback != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wifiP2PConnectionCallback.onInitiateDiscovery();
                }
            });
        }
        initiateDiscovery();
    }

    @Override
    public synchronized void initiateDiscovery() {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if(wifiP2PConnectionCallback != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wifiP2PConnectionCallback.onDiscoverySuccess();
                        }
                    });
                }
            }

            @Override
            public void onFailure(int i) {
                if(wifiP2PConnectionCallback != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wifiP2PConnectionCallback.onDiscoveryFailure();
                        }
                    });
                }
            }
        });
    }

    @Override
    public synchronized void connectDevice(WifiP2pDevice wifiP2pDevice) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiP2pDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        config.groupOwnerIntent = 15;
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {}
            @Override
            public void onFailure(int reason) {
                if(wifiP2PConnectionCallback != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wifiP2PConnectionCallback.onPeerConnectionFailure();
                        }
                    });
                }
            }
        });
    }

    @Override
    public synchronized void disconnectDevice() {
        if(manager == null) return;
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {}
            @Override
            public void onFailure(int i) {
                if(wifiP2PConnectionCallback != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wifiP2PConnectionCallback.onPeerDisconnectionFailure();
                        }
                    });
                }
            }
        });
    }

    @Override
    public synchronized void handleOnPeersChangedResponse() {
        manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(final WifiP2pDeviceList wifiP2pDeviceList) {
                if(wifiP2PConnectionCallback != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wifiP2PConnectionCallback.onPeerAvailable(wifiP2pDeviceList);
                        }
                    });
                }
            }
        });
    }

    @Override
    public synchronized void handleOnPeerConnected() {
        if(wifiP2PConnectionCallback != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wifiP2PConnectionCallback.onPeerConnectionSuccess();
                    boolean isClient = RECEIVER_PREFS_VALUE.equalsIgnoreCase(PreferenceUtils.getStringValues(activity, PREFS_CLIENT_KEY));
                    if(isClient) startDataTransfer(null);
                }
            });
        }
    }

    @Override
    public synchronized void handleOnPeerDisconnected() {
        if(wifiP2PConnectionCallback != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wifiP2PConnectionCallback.onPeerDisconnectionSuccess();
                }
            });
        }
    }

    @Override
    public synchronized void handleOnDeviceStatusChanged(WifiP2pDevice wifiP2pDevice) {
        if(wifiP2pDevice.status == WifiP2pDevice.CONNECTED) {
//            handleOnPeerConnected();
        }
    }

    @Override
    public void startDataTransfer(final String message) {
        manager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                boolean isGroupOwner = wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner;
                boolean isClient = RECEIVER_PREFS_VALUE.equalsIgnoreCase(PreferenceUtils.getStringValues(activity, PREFS_CLIENT_KEY));

                if(isClient && isGroupOwner) {
                    handleOnPeerServer(wifiP2pInfo);

                } else if(!isClient && !isGroupOwner && message != null) {
                    handleOnPeerClient(wifiP2pInfo, message);
                }
            }
        });
    }

    @Override
    public void handleOnPeerServer(WifiP2pInfo wifiP2pInfo) {
        new DataServerTask(wifiP2PConnectionCallback, DEFAULT_WIFI_PORT).execute();
    }

    @Override
    public void handleOnPeerClient(WifiP2pInfo wifiP2pInfo, String message) {
        Intent serviceIntent = new Intent(activity, ServerDataService.class);
        serviceIntent.setAction(ACTION_SEND_FILE);
        serviceIntent.putExtra(EXTRAS_DATA, message);
        serviceIntent.putExtra(EXTRAS_GROUP_OWNER_ADDRESS, wifiP2pInfo.groupOwnerAddress == null ? "" : wifiP2pInfo.groupOwnerAddress.getHostAddress());
        serviceIntent.putExtra(EXTRAS_GROUP_OWNER_PORT, DEFAULT_WIFI_PORT);
        serviceIntent.putExtra(EXTRAS_RESULT_RECEIVER, new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, final Bundle resultData) {
                if(resultCode == DEFAULT_WIFI_PORT && resultData != null) {
                    int status = Integer.valueOf(String.valueOf(resultData.get(EXTRAS_MESSAGE)));
                    switch (status) {
                        case STATUS_CONNECTING:
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    wifiP2PConnectionCallback.onDataTransferring();
                                }
                            });
                            break;

                        case STATUS_SUCCESS:
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    wifiP2PConnectionCallback.onDataTransferredSuccess();
                                }
                            });
                            break;

                        case STATUS_FAILURE:
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    wifiP2PConnectionCallback.onDataTransferredFailure();
                                }
                            });
                            break;
                    }
                }
            }
        });
        activity.startService(serviceIntent);
    }


    private void deletePersistentGroups() {
        try {
            Method[] methods = WifiP2pManager.class.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals("deletePersistentGroup")) {
                    // Delete any persistent group
                    for (int netid = 0; netid < 32; netid++) {
                        methods[i].invoke(manager, channel, netid, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Builder {
        private String prefsKey;
        private Activity activity;
        private WifiP2PConnectionCallback wifiP2PConnectionCallback;

        public Builder setSender(Activity activity) {
            this.activity = activity;
            this.prefsKey = RECEIVER_PREFS_VALUE;
            return this;
        }

        public Builder setReceiver(Activity activity) {
            this.activity = activity;
            this.prefsKey = SENDER_PREFS_VALUE;
            return this;
        }

        public Builder setWifiP2PConnectionCallback(WifiP2PConnectionCallback wifiP2PConnectionCallback) {
            this.wifiP2PConnectionCallback = wifiP2PConnectionCallback;
            return this;
        }

        public WifiP2PServiceImpl build() {
            return new WifiP2PServiceImpl(this);
        }
    }
}
