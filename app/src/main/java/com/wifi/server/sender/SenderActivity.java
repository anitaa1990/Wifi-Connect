package com.wifi.server.sender;


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
import com.wifiscanner.listener.WifiP2PConnectionCallback;
import com.wifiscanner.service.WifiP2PService;
import com.wifiscanner.service.WifiP2PServiceImpl;
import com.wifiscanner.utils.Utils;

import java.util.Map;
import static com.wifiscanner.WifiConstants.RECEIVER_PREFS_VALUE;

public class SenderActivity extends BaseActivity implements WifiP2PConnectionCallback, OnSenderUIListener, DialogEventListener {

    private boolean isSuccess = Boolean.FALSE;
    public WifiP2PService wifiP2PService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        initToolBar();
        updateTitle(getString(R.string.title_sender));
        redirectToDeviceListScreen();

        wifiP2PService = new WifiP2PServiceImpl(this, RECEIVER_PREFS_VALUE, this);
        wifiP2PService.onCreate();
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

    @Override
    public void redirectToDeviceListScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                DeviceListFragment.newInstance()).commitAllowingStateLoss();
    }

    @Override
    public void onScanStarted() {
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof DeviceListFragment) ((DeviceListFragment)fragment).onScanStarted();
    }

    @Override
    public void onDeviceAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof DeviceListFragment) ((DeviceListFragment)fragment).onDeviceAvailable(wifiP2pDeviceList);
    }

    @Override
    public void onDeviceUnavailable() {
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof DeviceListFragment) ((DeviceListFragment)fragment).onNoDeviceAvailable();
    }

    @Override
    public void onScanCompleted() {
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof DeviceListFragment) ((DeviceListFragment)fragment).onScanCompleted();
    }

    @Override
    public void redirectToOstaListScreen(Map<String, String> data) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                OstaListFragment.newInstance(data)).commitAllowingStateLoss();
    }

    @Override
    public void redirectToProcessScreen(WifiP2pDevice wifiP2pDevice) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragment_container, SenderFragment.newInstance(wifiP2pDevice))
                .commitAllowingStateLoss();
    }

    @Override
    public void onConnectionStarted() {
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof SenderFragment) ((SenderFragment)fragment).onConnectStarted();
    }

    @Override
    public void onConnectionCompleted() {
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof SenderFragment) ((SenderFragment)fragment).onConnectCompleted();
    }

    @Override
    public void onHotSpotInitiated() {
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof SenderFragment) ((SenderFragment)fragment).onHotspotStarted();
    }

    @Override
    public void onHotSpotCompleted() {
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof SenderFragment) ((SenderFragment)fragment).onHotspotCompleted();
    }

    @Override
    public void onDataTransferStarted() {
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof SenderFragment) ((SenderFragment)fragment).onDataStarted();
    }

    @Override
    public void onDataTransferCompleted(String s) {
        Map<String, String> merchantMap = Utils.convertStringToMap(s);
        redirectToOstaListScreen(merchantMap);
    }

    @Override
    public void redirectToSuccessScreen(final int imageResourceId, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isSuccess) {
                    isSuccess = Boolean.TRUE;
                    DialogHelper.getInstance().displayDialog(SenderActivity.this, message,
                            imageResourceId, false, SenderActivity.this);
                }
            }
        });
    }


    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        return currentFragment;
    }



//    @Override
//    public void onInitiateDiscovery() {
//        onScanStarted();
//    }
//
//    @Override
//    public void onDiscoverySuccess() {
//    }
//
//    @Override
//    public void onDiscoveryFailure() {
//    }
//
//    @Override
//    public void onPeerAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
//        onDeviceAvailable(wifiP2pDeviceList);
//    }
//
//    @Override
//    public void onPeerStatusChanged(WifiP2pDevice wifiP2pDevice) {
//
//    }
//
//    @Override
//    public void onPeerConnectionSuccess() {}
//
//    @Override
//    public void onPeerConnectionFailure() {}
//
//    @Override
//    public void onPeerDisconnectionSuccess() {}
//
//    @Override
//    public void onPeerDisconnectionFailure() {}
//
//    @Override
//    public void onInitializeHotSpot() {
//        System.out.println("###---Hotspot initialized---####");
//        onConnectionCompleted();
//        onHotSpotInitiated();
//        wifiP2PService.getWifiClients();
//    }
//
//    @Override
//    public void onClientConnectionAlive(WifiScanResult c) {
//        System.out.println("###---Client Connection success---####");
//        onHotSpotCompleted();
//        onDataTransferStarted();
//    }
//
//    @Override
//    public void onClientConnectionDead(WifiScanResult c) {
//        System.out.println("###---Client Connection failure---####");
//    }
//
//    @Override
//    public void onWifiClientsScanComplete() {
//        wifiP2PService.handleWifiScanCompletedResponse();
//        System.out.println("###---Client Scan completed---####");
//    }
//
//    @Override
//    public void onDataTransferComplete() {
//        onDataTransferCompleted();
//    }

    @Override
    public void onPositiveButtonClicked(View view) {
        finish();
    }

    @Override
    public void onInitiateDiscovery() {
        onScanStarted();
    }

    @Override
    public void onDiscoverySuccess() {

    }

    @Override
    public void onDiscoveryFailure() {

    }

    @Override
    public void onPeerAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
        onDeviceAvailable(wifiP2pDeviceList);
    }

    @Override
    public void onPeerStatusChanged(WifiP2pDevice wifiP2pDevice) {

    }

    @Override
    public void onPeerConnectionSuccess() {
        System.out.println("###---Client Connection success---####");
        wifiP2PService.startDataTransfer(null);
        onConnectionCompleted();
        onDataTransferStarted();
    }

    @Override
    public void onPeerConnectionFailure() {
        System.out.println("###---Client Connection failed---####");
    }

    @Override
    public void onPeerDisconnectionSuccess() {
        System.out.println("###---Client Disconnection success---####");
    }

    @Override
    public void onPeerDisconnectionFailure() {
        System.out.println("###---Client Disconnection failed---####");
    }

    @Override
    public void onDataTransferring() {}

    @Override
    public void onDataTransferredSuccess() {}

    @Override
    public void onDataTransferredFailure() {}

    @Override
    public void onDataReceiving() {
        onDataTransferStarted();
    }

    @Override
    public void onDataReceivedSuccess(String s) {
        onDataTransferCompleted(s);
    }

    @Override
    public void onDataReceivedFailure() {}
}
