package com.longye.androidtopc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.longye.androidtopc.net.manager.UDPReceiveManager;
import com.longye.androidtopc.net.manager.UDPSendManager;

public class MainActivity extends Activity {
    public enum FragmentFlag {
        DeviceList, Controller
    }

    private Fragment mDeviceFragment;
    private Fragment mControlFragment;
    private boolean isDeviceShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UDPReceiveManager.createInstance(this).startReceiveListen();

        FragmentManager fragmentManager = getFragmentManager();
        mDeviceFragment = fragmentManager.findFragmentById(R.id.device_frag);
        mControlFragment = fragmentManager.findFragmentById(R.id.control_frag);
        isDeviceShow = false;
        showFragment(FragmentFlag.DeviceList);
    }

    @Override
    protected void onDestroy() {
        UDPReceiveManager.release();
        UDPSendManager.sendOfflineMessage(this);

        super.onDestroy();
    }

    public void showFragment(FragmentFlag flag) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (flag == FragmentFlag.DeviceList) {
            if (!isDeviceShow) {
                isDeviceShow = true;
                ft.hide(mControlFragment);
                ft.show(mDeviceFragment);
            }
        } else if (flag == FragmentFlag.Controller) {
            if (isDeviceShow) {
                isDeviceShow = false;
                ft.hide(mDeviceFragment);
                ft.show(mControlFragment);
            }
        }

        ft.commit();
    }

}
