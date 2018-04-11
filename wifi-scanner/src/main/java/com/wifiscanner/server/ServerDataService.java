package com.wifiscanner.server;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import java.net.InetAddress;
import java.net.Socket;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;

import com.wifiscanner.model.WiFiTransferModal;
import static com.wifiscanner.WifiConstants.ACTION_SEND_FILE;
import static com.wifiscanner.WifiConstants.EXTRAS_DATA;
import static com.wifiscanner.WifiConstants.EXTRAS_GROUP_OWNER_ADDRESS;
import static com.wifiscanner.WifiConstants.EXTRAS_GROUP_OWNER_PORT;
import static com.wifiscanner.WifiConstants.EXTRAS_INET_ADDRESS;
import static com.wifiscanner.WifiConstants.EXTRAS_MESSAGE;
import static com.wifiscanner.WifiConstants.EXTRAS_RESULT_RECEIVER;
import static com.wifiscanner.WifiConstants.SOCKET_TIMEOUT;
import static com.wifiscanner.WifiConstants.STATUS_CONNECTING;
import static com.wifiscanner.WifiConstants.STATUS_FAILURE;
import static com.wifiscanner.WifiConstants.STATUS_SUCCESS;

public class ServerDataService extends IntentService {

    private ResultReceiver resultReceiver;

    public ServerDataService() {
        super("DataTransferService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case ACTION_SEND_FILE:

                String data = intent.getExtras().getString(EXTRAS_DATA);
                int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);
                String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);
//                InetAddress inetAddress = (InetAddress) intent.getExtras().getSerializable(EXTRAS_INET_ADDRESS);
                resultReceiver = (ResultReceiver) intent.getExtras().get(EXTRAS_RESULT_RECEIVER);

                updateUI(port, STATUS_CONNECTING);

                Socket socket = new Socket();

                try {
                    socket.bind(null);
                    socket.connect((new InetSocketAddress(host, port)), SOCKET_TIMEOUT);

                    OutputStream stream = socket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(stream);
                    WiFiTransferModal transObj = new WiFiTransferModal(data, host);
                    oos.writeObject(transObj);

                    oos.close();
                    updateUI(port, STATUS_SUCCESS);

                } catch (Exception e) {
                    e.printStackTrace();
                    updateUI(port, STATUS_FAILURE);
                } finally {
                    if (socket != null) {
                        if (socket.isConnected()) {
                            try {
                                socket.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
    }


    private void updateUI(int port,
                          int status) {
        Bundle b = new Bundle();
        b.putString(EXTRAS_MESSAGE, String.valueOf(status));
        if(resultReceiver != null) {
            resultReceiver.send(port, b);
        }
    }
}
