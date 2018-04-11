package com.wifi.server;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pDevice;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.CustomViewHolder> {

    private Context context;
    private List<WifiP2pDevice> wifiP2pDevices;
    public WifiListAdapter(Context context, List<WifiP2pDevice> wifiP2pDevices) {
        this.context = context;
        this.wifiP2pDevices = wifiP2pDevices;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_list_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        WifiP2pDevice device = getItem(position);
        holder.itemTitle.setText(device.deviceName);

        if(device.status == WifiP2pDevice.CONNECTED) {
            holder.itemDesc.setText(context.getString(R.string.connected));
            holder.itemDesc.setTextColor(Color.GREEN);

        } else {
            holder.itemDesc.setText(device.deviceAddress);
            holder.itemDesc.setTextColor(ContextCompat.getColor(context, R.color.fragment_add_bank_desc));
        }
    }

    @Override
    public int getItemCount() {
        return wifiP2pDevices.size();
    }

    public WifiP2pDevice getItem(int position) {
        return wifiP2pDevices.get(position);
    }


    public void addDevices(List<WifiP2pDevice> devices) {
        wifiP2pDevices.clear();
        wifiP2pDevices.addAll(devices);
        notifyDataSetChanged();
    }

    public void addDevice(WifiP2pDevice device) {
        wifiP2pDevices.add(device);
        notifyDataSetChanged();
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTitle, itemDesc;
        public CustomViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDesc = itemView.findViewById(R.id.item_desc);
        }
    }
}
