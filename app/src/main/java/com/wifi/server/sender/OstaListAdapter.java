package com.wifi.server.sender;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wifi.server.R;
import com.wifi.server.model.OstaCard;
import com.wifi.server.utils.Utils;
import com.wifi.server.views.CircleCheckBox;

import java.util.List;

public class OstaListAdapter extends RecyclerView.Adapter<OstaListAdapter.CustomViewHolder> {


    private Activity context;
    private List<OstaCard> ostaCardList;
    private OstaCard selectedOstaCard;

    private OnItemSelectListener listener;
    public OstaListAdapter(Activity context, List<OstaCard> ostaCardList) {
        this.context = context;
        this.ostaCardList = ostaCardList;
    }

    public OstaListAdapter(Activity context, List<OstaCard> ostaCardList, OnItemSelectListener listener) {
        this.context = context;
        this.ostaCardList = ostaCardList;
        this.listener = listener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.osta_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        OstaCard ostaCard = getItem(position);
        holder.itemAmount.setText(String.valueOf(ostaCard.getAmount()));

        String osta = ostaCard.getDcoin();
        holder.itemTitle.setText(String.format(context.getString(R.string.history_osta_txns_title), osta.substring(osta.length()-6)));

        if(Utils.hasExpired(Long.valueOf(ostaCard.getExpiryTime()))) {
            holder.itemDesc.setText(context.getString(R.string.home_expired));
            holder.itemDesc.setTextColor(ContextCompat.getColor(context, R.color.color_text_expired));
            holder.itemTitle.setTextColor(ContextCompat.getColor(context, R.color.color_osta_expired));
            holder.itemAmount.setTextColor(ContextCompat.getColor(context, R.color.color_osta_expired));
            holder.itemCurrency.setTextColor(ContextCompat.getColor(context, R.color.color_osta_expired));
            holder.itemExpired.setVisibility(View.VISIBLE);
            holder.rootView.setEnabled(false);
            holder.itemDesc.setEnabled(false);
            holder.itemTitle.setEnabled(false);
            holder.itemAmount.setEnabled(false);
            holder.itemAmount.setEnabled(false);
            holder.itemExpired.setEnabled(false);

        } else {
            String text = Utils.timeAgo(context, Long.valueOf(ostaCard.getExpiryTime()));
            holder.itemDesc.setText(String.format(context.getString(R.string.home_expiry), text));
            holder.itemDesc.setTextColor(ContextCompat.getColor(context, R.color.acitivity_onboarding_info));
            holder.itemTitle.setTextColor(ContextCompat.getColor(context, R.color.acitivity_onboarding_info));
            holder.itemAmount.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.itemCurrency.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.itemExpired.setVisibility(View.INVISIBLE);
            holder.rootView.setEnabled(true);
            holder.itemDesc.setEnabled(true);
            holder.itemTitle.setEnabled(true);
            holder.itemAmount.setEnabled(true);
            holder.itemAmount.setEnabled(true);
            holder.itemExpired.setEnabled(true);
        }


        if(ostaCard.isChecked()) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);
        } else holder.checkBox.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return ostaCardList.size();
    }

    public OstaCard getItem(int position) {
        return ostaCardList.get(position);
    }

    private void updateUI(int position) {
        for(int i=0; i < ostaCardList.size(); i++) {
            OstaCard ostaCard = getItem(i);
            if(ostaCard.isChecked()) {
                ostaCard.setChecked(false);
                notifyItemChanged(i);
            }
        }
        OstaCard selectedOsta = getItem(position);
        selectedOsta.setChecked(true);
        notifyItemChanged(position);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView itemAmount, itemCurrency;
        private TextView itemTitle, itemDesc, itemExpired;
        private CircleCheckBox checkBox;
        private View rootView;

        public CustomViewHolder(View convertView) {
            super(convertView);
            this.itemAmount = convertView.findViewById(R.id.item_amount);
            this.itemAmount.setOnLongClickListener(null);

            this.itemCurrency = convertView.findViewById(R.id.item_currency);
            this.itemCurrency.setOnLongClickListener(null);

            this.itemTitle = convertView.findViewById(R.id.item_title);
            this.itemTitle.setOnLongClickListener(null);

            this.itemDesc = convertView.findViewById(R.id.item_expiry);
            this.itemDesc.setOnLongClickListener(null);

            this.itemExpired = convertView.findViewById(R.id.item_expiry_txt);
            this.itemExpired.setOnLongClickListener(null);

            this.checkBox = convertView.findViewById(R.id.radio_btn);
            this.checkBox.setOnLongClickListener(null);
            this.checkBox.setOnClickListener(null);

            this.rootView = convertView.findViewById(R.id.root_view);
            this.rootView.setOnLongClickListener(null);
            rootView.setOnClickListener(this);
            itemDesc.setOnClickListener(this);
            itemTitle.setOnClickListener(this);
            itemAmount.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            OstaCard ostaCard = getItem(getLayoutPosition());
            setSelectedOstaCard(ostaCard);
            updateUI(getLayoutPosition());
            if(listener != null) listener.onItemSelected(ostaCard);
        }
    }

    public void refresh(List<OstaCard> ostaCards) {
        ostaCardList.clear();
        this.ostaCardList.addAll(ostaCards);
        notifyDataSetChanged();
    }

    public String getSelectedOstaCard() {
        if(selectedOstaCard != null)
            return selectedOstaCard.getDcoin();
        return "";
    }

    public String getSelectedOstaCardId() {
        if(selectedOstaCard != null)
            return String.valueOf(selectedOstaCard.getCardId());
        return "";
    }

    private void setSelectedOstaCard(OstaCard selectedOstaCard) {
        this.selectedOstaCard = selectedOstaCard;
    }

    public interface OnItemSelectListener {
        void onItemSelected(OstaCard ostaCard);
    }
}
