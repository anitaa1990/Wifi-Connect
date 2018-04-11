package com.wifi.server.sender;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.wifi.server.R;
import com.wifi.server.base.BaseFragment;
import com.wifi.server.model.OstaCard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OstaListFragment extends BaseFragment implements View.OnClickListener {

    private static final String INTENT_OSTA = "intent_osta";

    private RecyclerView recyclerView;
    private OstaListAdapter ostaListAdapter;

    private TextView ostaTitle, ostaDesc;
    private EditText editAmount;
    private TextView ostaCodesTitle;

    private View btnProceed;

    private Map<String, String> data;
    public static OstaListFragment newInstance(Map<String, String> data) {
        OstaListFragment fragment = new OstaListFragment();
        Bundle args = new Bundle();
        args.putSerializable(INTENT_OSTA, (Serializable) data);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (Map<String, String>) getArguments().get(INTENT_OSTA);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return enter ?  AnimationUtils.loadAnimation(activity, R.anim.slide_in_right) : super.onCreateAnimation(transit, enter, nextAnim);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_osta_list, container, false);

        ostaTitle = rootView.findViewById(R.id.osta_merchant_title);
        ostaDesc = rootView.findViewById(R.id.osta_merchant_txt);
        editAmount = rootView.findViewById(R.id.edit_amount);
        ostaCodesTitle = rootView.findViewById(R.id.osta_count_txt);

        btnProceed = rootView.findViewById(R.id.view_proceed);
        btnProceed.setOnClickListener(this);

        recyclerView = rootView.findViewById(R.id.osta_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        ostaTitle.setText(activity.getString(R.string.process_osta_merchant_title));
        ostaDesc.setText(data.get("merchantTransactionReferenceId"));
        editAmount.setText(data.get("amount"));
        updateOstaList(com.wifi.server.utils.Utils.loadDummyOstaList(activity));

        return rootView;
    }

    @SuppressLint("StringFormatMatches")
    public void updateOstaList(List<OstaCard> ostaCardList) {
        recyclerView.setVisibility(View.VISIBLE);
        ostaCodesTitle.setText(String.format(activity.getString(R.string.home_get_osta_count), ostaCardList.size()));
        ostaListAdapter = new OstaListAdapter(activity, ostaCardList);
        recyclerView.setAdapter(ostaListAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view == btnProceed) {
            String ostaNumber = ostaListAdapter.getSelectedOstaCard();
            String abc = String.format(getString(R.string.completed), data.get("amount"), ostaNumber);
            ((SenderActivity)activity).redirectToSuccessScreen(R.drawable.ic_p_success, abc);
        }
    }
}
