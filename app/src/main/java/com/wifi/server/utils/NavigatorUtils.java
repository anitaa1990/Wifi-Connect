package com.wifi.server.utils;

import android.app.Activity;
import android.content.Intent;

import com.wifi.server.R;
import com.wifi.server.receiver.ReceiverActivity;
import com.wifi.server.sender.SenderActivity;

public class NavigatorUtils {

    public static void redirectToSenderScreen(Activity activity) {
        Intent intent = new Intent(activity, SenderActivity.class);
        activity.finish();
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void redirectToReceiverScreen(Activity activity) {
        Intent intent = new Intent(activity, ReceiverActivity.class);
        activity.finish();
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
