package com.longye.androidtopc.fragment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.longye.androidtopc.R;

import java.util.ArrayList;
import java.util.List;

public class DeviceItemAdapter extends BaseAdapter {
    private List<DeviceItem> datas;
    private Activity mContext;

    public class DeviceItem {
        String deviceName;
        String deviceIp;

        public DeviceItem(String deviceName, String deviceIp) {
            this.deviceName = deviceName;
            this.deviceIp = deviceIp;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof DeviceItem) {
                if (deviceIp != null)
                    return deviceIp.equals(((DeviceItem) o).deviceIp);
            }
            return false;
        }
    }

    public DeviceItemAdapter(Activity cxt) {
        this.mContext = cxt;
        datas = new ArrayList<>();
    }

    public void addItem(String deviceName, String ip) {
        DeviceItem item = new DeviceItem(deviceName, ip);
        if (!datas.contains(item)) {
            datas.add(item);
            safeUpdateUI();
        }
    }

    public void clear() {
        datas.clear();
        safeUpdateUI();
    }

    private void safeUpdateUI() {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder vh = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.device_item, null);
            vh = new ViewHolder();
            vh.name = (TextView) view.findViewById(R.id.device_name);
            vh.ip = (TextView) view.findViewById(R.id.device_ip);
            view.setTag(vh);
        }
        vh = (ViewHolder) view.getTag();
        DeviceItem item = datas.get(position);
        vh.name.setText(item.deviceName);
        vh.ip.setText(item.deviceIp);
        return view;
    }

    static class ViewHolder {
        TextView name;
        TextView ip;
    }
}
