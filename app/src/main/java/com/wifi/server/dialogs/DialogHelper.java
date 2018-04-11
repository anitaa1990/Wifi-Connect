package com.wifi.server.dialogs;

import android.app.Activity;

public class DialogHelper {

    private static DialogHelper instance;
    public static DialogHelper getInstance() {
        if(instance == null) instance = new DialogHelper();
        return instance;
    }

    private CustomDialog customDialog;

    public void displayDialog(Activity activity,
                              String message,
                              boolean cancellable) {

        customDialog = new CustomDialog(activity);
        customDialog.setText(message, true);
        customDialog.setCancelable(cancellable);
        if(!activity.isFinishing()) customDialog.show();
    }

    public void displayDialog(Activity activity,
                              String message,
                              int imageResourceId,
                              boolean cancellable,
                              DialogEventListener eventListener) {

        customDialog = new CustomDialog(activity, eventListener);
        customDialog.setText(message, true);
        customDialog.setImg(imageResourceId, true);
        customDialog.setCancelable(cancellable);
        if(!activity.isFinishing()) customDialog.show();
    }

    public void dismissDialog() {
        if(customDialog != null) {
            customDialog.dismiss();
        }
    }
}
