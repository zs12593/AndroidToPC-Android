package com.longye.androidtopc.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.longye.androidtopc.MainActivity;
import com.longye.androidtopc.R;
import com.longye.androidtopc.net.manager.ReceiveCallback;
import com.longye.androidtopc.net.manager.UDPReceiveManager;
import com.longye.androidtopc.net.manager.UDPSendManager;
import com.longye.androidtopc.net.protocol.ConnectFeedback;
import com.longye.androidtopc.net.protocol.Cursor;
import com.longye.androidtopc.net.protocol.Offline;
import com.longye.androidtopc.net.protocol.Online;
import com.longye.androidtopc.net.protocol.Protocol;
import com.longye.androidtopc.net.protocol.ProtocolParam;

import java.net.InetAddress;

public class DeviceFragment extends Fragment {
    private DeviceItemAdapter mAdapter;
    private ReceiveCallback mReceive = new ReceiveCallback() {
        @Override
        public void onReceive(Protocol protocol) {
            InetAddress address = protocol.getHost();
            ProtocolParam param = protocol.getParam();
            if (param instanceof Online) {
                Online online = (Online) protocol.getParam();
                mAdapter.addItem(online.getDeviceName(), address.getHostAddress());
                if (online.getOnlineType() == Online.SELF_ONLINE) {
                    UDPSendManager.sendOnlineMessage(address.getHostAddress(), DeviceFragment.this.getActivity());
                }
            } else if (param instanceof Offline) {
                mAdapter.removeItem(address.getHostAddress());
            } else if (param instanceof ConnectFeedback) {
                ConnectFeedback feedback = (ConnectFeedback) protocol.getParam();
                if (feedback.isAccess()) {
                    Cursor.CONNECTED_PASSWORD = feedback.getPassword();
                    ((MainActivity) getActivity()).showFragment(MainActivity.FragmentFlag.Controller);
                }
            }
        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showConnectDialog(position);
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UDPSendManager.sendOnlineMessage(null, DeviceFragment.this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new DeviceItemAdapter(this.getActivity());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(mOnItemClickListener);
        Button refresh = (Button) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clear();
                UDPSendManager.sendOnlineMessage(null, DeviceFragment.this.getActivity());
            }
        });

        return view;
    }

    private void showConnectDialog(int position) {
        final DeviceItemAdapter.DeviceItem item = (DeviceItemAdapter.DeviceItem) mAdapter.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("配对").setMessage("请求配对\"" + item.deviceName + "\"\n(IP: " + item.deviceIp + ")？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UDPSendManager.sendConnectRequest(item.deviceIp, DeviceFragment.this.getActivity());
            }
        }).setNegativeButton("取消", null).show();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            UDPReceiveManager.getInstance().removeReceiveCallback(ReceiveCallback.ReceiveType.Online);
            UDPReceiveManager.getInstance().removeReceiveCallback(ReceiveCallback.ReceiveType.Offline);
            UDPReceiveManager.getInstance().removeReceiveCallback(ReceiveCallback.ReceiveType.ConnectFeedback);
        } else {
            UDPReceiveManager.getInstance().addReceiveCallback(ReceiveCallback.ReceiveType.Online, mReceive);
            UDPReceiveManager.getInstance().addReceiveCallback(ReceiveCallback.ReceiveType.Offline, mReceive);
            UDPReceiveManager.getInstance().addReceiveCallback(ReceiveCallback.ReceiveType.ConnectFeedback, mReceive);
        }
    }
}
