package com.wifi.server.receiver;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.wifi.server.R;
import com.wifi.server.base.BaseActivity;
import com.wifi.server.dialogs.DialogEventListener;
import com.wifi.server.dialogs.DialogHelper;
import com.wifi.server.utils.Utils;
import com.wifiscanner.listener.WifiP2PConnectionCallback;
import com.wifiscanner.service.WifiP2PService;
import com.wifiscanner.service.WifiP2PServiceImpl;
import static com.wifiscanner.WifiConstants.SENDER_PREFS_VALUE;

public class ReceiverActivity extends BaseActivity implements WifiP2PConnectionCallback, DialogEventListener {

    private boolean isSuccess = Boolean.FALSE;
    public WifiP2PService wifiP2PService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        initToolBar();
        updateTitle(getString(R.string.title_receiver));

        wifiP2PService = new WifiP2PServiceImpl(this, SENDER_PREFS_VALUE, this);
        wifiP2PService.onCreate();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                ReceiverFragment.newInstance()).commitAllowingStateLoss();
    }


    @Override
    protected void onResume() {
        super.onResume();
        wifiP2PService.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        wifiP2PService.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wifiP2PService.onDestroy();
    }


    //    @Override
//    public void onServerConnecting(String ssid) {
//        System.out.println("####Server connecting##");
//        Fragment fragment = getCurrentFragment();
//        if(fragment instanceof ReceiverFragment) ((ReceiverFragment)fragment).onServerConnectStarted(ssid);
//    }
//
//    @Override
//    public void onServerConnectionAlive(String ip, String ssid) {
//        System.out.println("####Server connected##");
//        Fragment fragment = getCurrentFragment();
//        if(fragment instanceof ReceiverFragment) ((ReceiverFragment)fragment).onServerConnectSuccess(ip, ssid);
//    }
//
//    @Override
//    public void onServerConnectionDead(String ssid) {
//        System.out.println("####Server connect failed##");
//        Fragment fragment = getCurrentFragment();
//        if(fragment instanceof ReceiverFragment) ((ReceiverFragment)fragment).onServerConnectFailed(ssid);
//    }
//
//    @Override
//    public void onServerStatusError() {
////        Utils.showMessage(getApplicationContext(), getString(R.string.receiver_disconnected_error));
//    }
//
//    @Override
//    public void onDataTransferError() {
//        System.out.println("####Data transfer error##");
//    }
//
//    @Override
//    public void onDataTransferSuccess(String s) {
//        if(!isSuccess) {
//            isSuccess = Boolean.TRUE;
//            String message = String.format(getString(R.string.receiver_success), s);
//            DialogHelper.getInstance().displayDialog(this, message, R.drawable.ic_p_success, false, this);
//        }
//    }
//
    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        return currentFragment;
    }

    @Override
    public void onPositiveButtonClicked(View view) {
        finish();
    }


    @Override
    public void onInitiateDiscovery() {

    }

    @Override
    public void onDiscoverySuccess() {

    }

    @Override
    public void onDiscoveryFailure() {

    }

    @Override
    public void onPeerAvailable(WifiP2pDeviceList wifiP2pDeviceList) {

    }

    @Override
    public void onPeerStatusChanged(WifiP2pDevice wifiP2pDevice) {

    }

    @Override
    public void onPeerConnectionSuccess() {
        System.out.println("####Receiver connected##");
        wifiP2PService.startDataTransfer(Utils.getMerchantDetails());
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof ReceiverFragment) ((ReceiverFragment)fragment).onServerConnectSuccess();
    }

    @Override
    public void onPeerConnectionFailure() {
        System.out.println("####Server connection failed##");
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof ReceiverFragment) ((ReceiverFragment)fragment).onServerConnectFailed();
    }

    @Override
    public void onPeerDisconnectionSuccess() {

    }

    @Override
    public void onPeerDisconnectionFailure() {

    }

    @Override
    public void onDataTransferring() {
        System.out.println("####Data Receiving##");
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof ReceiverFragment) ((ReceiverFragment)fragment).onDataTransferStarted();
    }

    @Override
    public void onDataTransferredSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isSuccess) {
                    isSuccess = Boolean.TRUE;
                    String message = getString(R.string.receiver_success);
                    DialogHelper.getInstance().displayDialog(ReceiverActivity.this, message, R.drawable.ic_p_success, false, ReceiverActivity.this);
                }
            }
        });
    }

    @Override
    public void onDataTransferredFailure() {
        System.out.println("####Data Receiving Failed##");
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof ReceiverFragment) ((ReceiverFragment)fragment).onDataTransferFailed();
    }

    @Override
    public void onDataReceiving() {}

    @Override
    public void onDataReceivedSuccess(String s) {}

    @Override
    public void onDataReceivedFailure() {}
}