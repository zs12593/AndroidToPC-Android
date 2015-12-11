package com.longye.androidtopc.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.longye.androidtopc.R;
import com.longye.androidtopc.net.manager.ReceiveCallback;
import com.longye.androidtopc.net.manager.UDPReceiveManager;
import com.longye.androidtopc.net.manager.UDPSendManager;
import com.longye.androidtopc.net.protocol.Online;
import com.longye.androidtopc.net.protocol.Protocol;

import java.net.InetAddress;

public class DeviceFragment extends Fragment {
    private DeviceItemAdapter mAdapter;
    private ReceiveCallback mOnlineReceive = new ReceiveCallback() {
        @Override
        public void onReceive(Protocol protocol) {
            final InetAddress address = protocol.getHost();
            Online online = (Online) protocol.getParam();
            mAdapter.addItem(online.getDeviceName(), address.getHostAddress());
            if (online.getOnlineType() == Online.SELF_ONLINE) {
                UDPSendManager.sendOnlineMessage(address.getHostAddress());
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UDPReceiveManager.getInstance().addReceiveCallback(ReceiveCallback.ReceiveType.Online, mOnlineReceive);
        UDPSendManager.sendOnlineMessage(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new DeviceItemAdapter(this.getActivity());
        listView.setAdapter(mAdapter);
        mAdapter.addItem("AA", "192.168.0.1");
        Button refresh = (Button) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clear();
                UDPSendManager.sendOnlineMessage(null);
            }
        });

        return view;
    }

}
