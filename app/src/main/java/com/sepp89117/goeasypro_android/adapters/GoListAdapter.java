// File: app/src/main/java/com/sepp89117/goeasypro_android/adapters/GoListAdapter.java

package com.sepp89117.goeasypro_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.sepp89117.goeasypro_android.R;
import com.sepp89117.goeasypro_android.gopro.GoProDevice;

import java.util.ArrayList;

public class GoListAdapter extends BaseAdapter {
    private final ArrayList<GoProDevice> goProDevices;
    private final Context context;
    private final LayoutInflater inflater;

    public GoListAdapter(ArrayList<GoProDevice> goProDevices, Context context) {
        this.goProDevices = goProDevices;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return goProDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return goProDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.go_list_item, null);
            holder = new ViewHolder();
            // Use the real IDs from your XML file
            holder.name = convertView.findViewById(R.id.name);
            holder.model = convertView.findViewById(R.id.model);
            holder.statusText = convertView.findViewById(R.id.flat_mode); // We will use 'flat_mode' for status
            holder.connectionIcon = convertView.findViewById(R.id.bt_symbol); // We will use 'bt_symbol' for the icon
            holder.connectionText = convertView.findViewById(R.id.rssi); // The text below the icon
            // You can add other views like battery, sd_card etc. here if needed
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the device for this position
        GoProDevice device = goProDevices.get(position);

        // --- THIS IS THE FINAL LOGIC ---

        // Set basic info (this part will likely be similar to your original adapter)
        holder.name.setText(device.displayName);
        holder.model.setText(device.modelName);


        // Set connection status based on the device state
        if (device.isUsbConnected()) {
            holder.statusText.setText("Connected (USB)");
            holder.connectionIcon.setImageResource(R.drawable.ic_usb); // The USB icon we created
            holder.connectionIcon.setColorFilter(ContextCompat.getColor(context, R.color.connection_green));
            holder.connectionText.setVisibility(View.INVISIBLE); // Hide the "NC" or RSSI text for USB
        } else if (device.btConnectionStage == GoProDevice.BT_CONNECTED) {
            String status = "Connected";
            if(device.isBusy) status += " (Busy)";
            holder.statusText.setText(status); // Or you could set the actual camera mode here
            holder.connectionIcon.setImageResource(R.drawable.ic_baseline_bluetooth_24); // The original BT icon
            holder.connectionIcon.setColorFilter(ContextCompat.getColor(context, R.color.connection_blue));
            holder.connectionText.setVisibility(View.VISIBLE);
            holder.connectionText.setText(String.valueOf(device.rssi)); // Assumes 'rssi' field exists on GoProDevice
        } else {
            holder.statusText.setText("Disconnected");
            holder.connectionIcon.setImageResource(R.drawable.ic_baseline_bluetooth_24); // The original BT icon
            holder.connectionIcon.setColorFilter(ContextCompat.getColor(context, R.color.connection_grey));
            holder.connectionText.setVisibility(View.VISIBLE);
            holder.connectionText.setText("NC");
        }

        // TODO: You will need to re-add any logic from your original adapter that sets
        // the battery level, sd card status, preset, etc.
        // This solution focuses only on fixing the connection status display.

        return convertView;
    }

    // This ViewHolder MUST match the variables used above
    private static class ViewHolder {
        TextView name;
        TextView model;
        TextView statusText;
        ImageView connectionIcon;
        TextView connectionText;
        // Add other views like ImageView batt_symbol, TextView memory, etc.
    }
}