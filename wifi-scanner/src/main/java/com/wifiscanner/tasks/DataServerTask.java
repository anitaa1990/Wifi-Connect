package com.wifiscanner.tasks;

import android.os.AsyncTask;

import com.wifiscanner.listener.WifiP2PConnectionCallback;
import com.wifiscanner.model.WiFiTransferModal;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DataServerTask extends AsyncTask<String, String, String> {

    private int port;
    private WifiP2PConnectionCallback wifiP2PConnectionCallback;
    public DataServerTask(WifiP2PConnectionCallback wifiP2PConnectionCallback, int port) {
        this.wifiP2PConnectionCallback = wifiP2PConnectionCallback;
        this.port = port;
    }


    @Override
    protected String doInBackground(String... strings) {

        WiFiTransferModal obj = null;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket client = serverSocket.accept();

            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            obj = (WiFiTransferModal) ois.readObject();
            ois.close();
            serverSocket.close();
            wifiP2PConnectionCallback.onDataReceiving();

            return obj.getMessage();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null) {
            wifiP2PConnectionCallback.onDataReceivedSuccess(s);
        } else wifiP2PConnectionCallback.onDataReceivedFailure();
    }
}
