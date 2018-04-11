package com.wifi.server.dialogs;


import android.app.Activity;

public class LoaderHelper {

    private static LoaderHelper instance;
    public static LoaderHelper getInstance() {
        if(instance == null) instance = new LoaderHelper();
        return instance;
    }

    private LoaderDialog loaderDialog;

    public void displayLoader(Activity activity, int drawableId) {
        loaderDialog = new LoaderDialog(activity);
        loaderDialog.setDrawable(drawableId);
        if(!activity.isFinishing()) loaderDialog.show();
    }

    public void dismissDialog() {
        if(loaderDialog != null) {
            loaderDialog.dismiss();
        }
    }
}
