package com.wifi.server.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifi.server.R;


public class CustomDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private boolean cancellable = false;

    private TextView textView;
    private ImageView img;
    private Button btnPositiveBtn;
    private DialogEventListener eventListener;

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        setDialogView();
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
        setDialogView();
    }

    protected CustomDialog(Context context) {
        super(context, R.style.full_screen_dialog);
        this.mContext = context;
        setDialogView();
    }

    protected CustomDialog(Context context, DialogEventListener eventListener) {
        super(context, R.style.full_screen_dialog);
        this.mContext = context;
        this.eventListener = eventListener;
        setDialogView();
    }


    public void setDialogView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.view_dialog);
        setCancelable(isCancellable());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        textView = this.findViewById(R.id.dialog_txt);
        img = this.findViewById(R.id.dialog_img);
        btnPositiveBtn = this.findViewById(R.id.btn_ok);
        btnPositiveBtn.setOnClickListener(this);
    }


    public ImageView getImg() {
        return img;
    }

    public void setImg(int imageResource, boolean show) {
        showImg(show);
        if(imageResource!= 0)
            img.setImageResource(imageResource);
    }

    public void showImg(boolean show) {
        if (show) {
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.GONE);
        }
    }


    public TextView getDialogText() {
        return textView;
    }

    public void setText(String message, boolean show) {
        showTitle(show);

        SpannableString spannableString = getSpannedText(mContext, 1.8f, message);
        textView.setText(spannableString);
    }

    public void showTitle(boolean show) {
        if(show) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }

    @Override
    public void onClick(View v) {
        if(v == btnPositiveBtn) {
            dismiss();
            if(eventListener != null)
                eventListener.onPositiveButtonClicked(v);
        }
    }


    public static SpannableString getSpannedText(Context context, float textSize, String text) {
        SpannableString spannableString = new SpannableString(text);
        if(text.indexOf("\n") != -1) {
            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.activity_login_edittext_txt)),
                    text.indexOf("\n"), text.length(), 0);
            spannableString.setSpan(new RelativeSizeSpan(textSize), text.indexOf("\n"), text.length(), 0);
        }

        return spannableString;
    }
}
