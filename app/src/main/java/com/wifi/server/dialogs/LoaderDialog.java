package com.wifi.server.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.wifi.server.R;


public class LoaderDialog extends Dialog {

    private Context mContext;
    private boolean cancellable = false;

    private AnimationView animationView;

    public LoaderDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        setDialogView();
    }

    protected LoaderDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
        setDialogView();
    }


    public LoaderDialog(Context context) {
        super(context, R.style.full_screen_dialog);
        this.mContext = context;
        setDialogView();
    }

    public void setDialogView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.view_loader);
        setCancelable(cancellable);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        animationView = this.findViewById(R.id.gif);
    }

    public void setDrawable(int drawableId) {
        animationView.setVisibility(View.VISIBLE);
        animationView.setMovieResource(drawableId);
    }
}
