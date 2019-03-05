![152px](https://user-images.githubusercontent.com/38810169/40613662-45cd4bea-62a1-11e8-9584-cb389821cbaf.png)

# Wifi-Connect

<a href="https://android-arsenal.com/details/1/6930"><img src="https://img.shields.io/badge/Android%20Arsenal-Wifi--Connect-blue.svg?style=flat" alt="Android Arsenal" data-canonical-src="https://img.shields.io/badge/Android%20Arsenal-Wifi--Connect-blue.svg?style=flat" style="max-width:100%;"></a>

A simple wrapper module to connect two devices and share data using Wifi-Direct.

This library provides an easy way to access the wifi direct api provided by Android without having to deal with all the boilerplate stuff going on inside.


<h2>Sample App</h2>
<p><a href="https://play.google.com/store/apps/details?id=com.wifi.server" target="_blank"><img width="150" alt="Get it on Google Play" src="https://camo.githubusercontent.com/ccb26dee92ba45c411e669aae47dcc0706471af7/68747470733a2f2f706c61792e676f6f676c652e636f6d2f696e746c2f656e5f67622f6261646765732f696d616765732f67656e657269632f656e5f62616467655f7765625f67656e657269632e706e67" data-canonical-src="https://play.google.com/intl/en_gb/badges/images/generic/en_badge_web_generic.png" style="max-width:100%;"></a></p>

<p>Download the sample app on the Google Play Store and check out all the features</p>
<p>
<a href="https://lh3.googleusercontent.com/np6mWFTBu0GFah9BhO53E2ew5RgychG79Jh9x2Yp1wX_Omb2Eal_3bIL-amWXeDQVX8=h310-rw" target="_blank"><img src="https://github.com/anitaa1990/Wifi-Connect/blob/master/media/Screenshot_20180416-080937.png" width="180" style="max-width:100%;"></a>
<a href="https://lh3.googleusercontent.com/9q35IMOhQsRAoxCfxHsxBd2S62Ke_yXG_ivgFZwxoCQqVYEFJ7nqu11j_k0QALASYDE=h310-rw" target="_blank"><img src="https://github.com/anitaa1990/Wifi-Connect/blob/master/media/Screenshot_20180416-080943.png" width="180" style="max-width:100%;"></a>
<a href="https://lh3.googleusercontent.com/9q35IMOhQsRAoxCfxHsxBd2S62Ke_yXG_ivgFZwxoCQqVYEFJ7nqu11j_k0QALASYDE=h310-rw" target="_blank"><img src="https://github.com/anitaa1990/Wifi-Connect/blob/master/media/Screenshot_20180416-080954.png" width="180" style="max-width:100%;"></a>        
</p>

<img src="https://img.shields.io/badge/API-14%2B-orange.svg?style=flat" alt="API" data-canonical-src="https://img.shields.io/badge/API-14%2B-orange.svg?style=flat" style="max-width:100%;">


<h2>How to integrate the library in your app?</h2>
<b>Gradle Dependecy - Java</b></br>

```gradle
dependencies {
    implementation 'com.wifiscanner:wifiscanner:0.1.2'
}
```

<b>Maven Dependecy - Java</b></br>
```xml
<dependency>
  <groupId>com.wifiscanner</groupId>
  <artifactId>wifiscanner</artifactId>
  <version>0.1.2</version>
  <type>pom</type>
</dependency>
```


<h2>Usage</h2>
<b>Step 1:</b >Define the AndroidManifest.xml class:

```<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

Declare the following service:
```
<service android:enabled="true" android:name="com.wifiscanner.server.ServerDataService" />
```

<b>Note: Currently the app does not support runtime permissions.</b>


</br><b>Step 2:</b> Define the wrapper class in the ```onCreate()``` method of the activity 

```
WifiP2PService wifiP2PService = new WifiP2PServiceImpl.Builder()
                .setSender(this)
                .setWifiP2PConnectionCallback(this)
                .build();
        wifiP2PService.onCreate();
```
We need to initialize the wrapper class and declare the activity as either the <b>Sender</b> or the <b>Receiver</b>.
For instance, in order to declare the activity as a Sender class, we use ```.setSender(this)```.
In order to declare the activity as a Receiver class, we use ```.setReceiver(this)``` 

The Receiver - will act as the server. It will fetch the list of available nearby devices and select a device to connect with. 
The Sender - will act as the client. Once a connection request is made from the server, the sender will accept the connection and pass data to the Receiver 



</br><b>Step 3:</b> Add the following method in the ```onResume``` method of the activity
```
wifiP2PService.onResume();
```
This method will register the broadcast receiver that will listen for the changes in Wifi network.



</br><b>Step 4:</b> Add the following method in the ```onStop``` method of the activity
```
wifiP2PService.onStop();
```
This method will unregister the broadcast receiver since the activity is no longer at the top of the activity.


</br><b>Step 5:</b> Implement the WifiP2PConnectionCallback interface in your activity
```
public class MainActivity extends AppCompatActivity implements WifiP2PConnectionCallback {
```
Functions in the WifiP2PConnectionCallback interface

| Function Name | Usage  |
| ------------- | -----:|
| ```initiateDiscovery()``` | This callback method is called when the scan is first initiated |
| ```onDiscoverySuccess()``` | This callback method is called the discovery is successful |
| ```onDiscoveryFailure()``` | This callback method is called the discovery is not successful |
| ```onPeerAvailable()``` | This callback method is called with a list of all peer available devices |
| ```onPeerStatusChanged()``` | This status of the peers that are discovered is changing |
| ```onPeerConnectionSuccess()``` | called when a connection to a nearby device is successful |
| ```onPeerConnectionFailure()``` | called when a connection to a nearby device is not successful |
| ```onPeerDisconnectionSuccess()``` | called when a disconnection to a nearby device is successful |
| ```onPeerDisconnectionFailure()``` | called when a disconnection to a nearby device is not successful |
| ```onDataTransferring()``` | called when data transfer has been initiated |
| ```onDataTransferredSuccess()``` | called when data transfer has completed & is successful |
| ```onDataTransferredFailure()``` | called when data transfer has completed & is not successful |
| ```onDataReceiving()``` | Called when the receiver has started to receive the data from sender |
| ```onDataReceivedSuccess(String message)``` | Called when the receiver has received the data successfully |
| ```onDataReceivedFailure()``` | Called when the data transferred is completed but data is not received |


</br>Methods available in the Receiver class:
Method to connect to an available device:  - you need to pass the device that is to be connected
```wifiP2PService.connectDevice(wifiP2pDevice);```


</br>Method to start data transfer - you need to pass the message that needs to be shared to the client
```wifiP2PService.startDataTransfer("Helllooo");```


</br>And that's it folks!</br></br>

<h2>Created and maintained by:</h2>
<p>Anitaa Murthy  murthyanitaa@gmail.com</p>
<p><a href="https://twitter.com/anitaa_1990"> <img src="https://github.com/anitaa1990/DeviceInfo-Sample/blob/master/media/twitter-icon.png" alt="Twitter" style="max-width:100%;"> </a><a href="https://plus.google.com/104409749442569901352"> <img src="https://github.com/anitaa1990/DeviceInfo-Sample/blob/master/media/google-icon.png" alt="Google Plus" style="max-width:100%;"> </a><a href="https://www.linkedin.com/in/anitaa-murthy-41531699"> <img src="https://github.com/anitaa1990/DeviceInfo-Sample/blob/master/media/linkedin-icon.png" alt="Linkedin" style="max-width:100%;"> </a></p>


