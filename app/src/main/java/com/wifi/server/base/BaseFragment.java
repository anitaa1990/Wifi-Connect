package com.wifi.server.base;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
