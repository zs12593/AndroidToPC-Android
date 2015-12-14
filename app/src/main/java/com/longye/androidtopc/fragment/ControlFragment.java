package com.longye.androidtopc.fragment;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.longye.androidtopc.MainActivity;
import com.longye.androidtopc.R;
import com.longye.androidtopc.net.manager.ReceiveCallback;
import com.longye.androidtopc.net.manager.UDPReceiveManager;
import com.longye.androidtopc.net.manager.UDPSendManager;
import com.longye.androidtopc.net.protocol.Click;
import com.longye.androidtopc.net.protocol.ConnectedData;
import com.longye.androidtopc.net.protocol.Protocol;

public class ControlFragment extends Fragment {
    private TextView mSensitivity;
    private float mSensitivityNum = 1.0f;
    private Point mLastTouch = new Point();
    private long mTouchDownTime = 0;

    private ReceiveCallback mReceive = new ReceiveCallback() {
        @Override
        public void onReceive(Protocol protocol) {
            ((MainActivity) ControlFragment.this.getActivity()).showFragment(MainActivity.FragmentFlag.DeviceList);
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UDPSendManager.sendUnConnect(ConnectedData.ip, ConnectedData.password,
                    ControlFragment.this.getActivity());
        }
    };

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int id = v.getId();
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchDownTime = System.currentTimeMillis();
                    if (id == R.id.left_btn) {
                        UDPSendManager.sendClick(ConnectedData.ip, ConnectedData.password,
                                Click.LEFT_BUTTON, Click.STATE_DOWN, ControlFragment.this.getActivity());
                    } else if (id == R.id.right_btn) {
                        UDPSendManager.sendClick(ConnectedData.ip, ConnectedData.password,
                                Click.RIGHT_BUTTON, Click.STATE_DOWN, ControlFragment.this.getActivity());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (id == R.id.touch_view) {
                        UDPSendManager.sendMoveCursor(ConnectedData.ip, ConnectedData.password,
                                Math.round((x - mLastTouch.x) * mSensitivityNum),
                                Math.round((y - mLastTouch.y) * mSensitivityNum),
                                ControlFragment.this.getActivity());
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (id == R.id.left_btn) {
                        UDPSendManager.sendClick(ConnectedData.ip, ConnectedData.password,
                                Click.LEFT_BUTTON, Click.STATE_UP, ControlFragment.this.getActivity());
                    } else if (id == R.id.right_btn) {
                        UDPSendManager.sendClick(ConnectedData.ip, ConnectedData.password,
                                Click.RIGHT_BUTTON, Click.STATE_UP, ControlFragment.this.getActivity());
                    } else if (id == R.id.touch_view) {
                        if (System.currentTimeMillis() - mTouchDownTime < 100) {
                            UDPSendManager.sendClick(ConnectedData.ip, ConnectedData.password,
                                    Click.LEFT_BUTTON, Click.STATE_DOWN_UP, ControlFragment.this.getActivity());
                        } else {
                            UDPSendManager.sendMoveCursor(ConnectedData.ip, ConnectedData.password,
                                    Math.round((x - mLastTouch.x) * mSensitivityNum),
                                    Math.round((y - mLastTouch.y) * mSensitivityNum),
                                    ControlFragment.this.getActivity());
                        }
                    }
                    break;
            }
            if (id == R.id.touch_view) {
                mLastTouch.x = x;
                mLastTouch.y = y;
            }
            return true;
        }
    };

    private SeekBar.OnSeekBarChangeListener mSeekBarChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (progress == 5) {
                    mSensitivityNum = 1.0f;
                    mSensitivity.setText("1.0");
                } else if (progress < 5) {
                    mSensitivityNum = 1.0f - (5 - progress) * 0.1f;
                } else if (progress > 5) {
                    mSensitivityNum = 1.0f - (5 - progress) * 0.2f;
                }
                mSensitivity.setText(Double.toString(mSensitivityNum).substring(0, 3));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.controller_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        Button mUnConnect = (Button) view.findViewById(R.id.unConnect);
        Button mLeftBtn = (Button) view.findViewById(R.id.left_btn);
        Button mRightBtn = (Button) view.findViewById(R.id.right_btn);
        mSensitivity = (TextView) view.findViewById(R.id.sensitivity);
        SeekBar mSeekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        View mTouchView = view.findViewById(R.id.touch_view);

        mUnConnect.setOnClickListener(mOnClickListener);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChange);
        mLeftBtn.setOnTouchListener(mTouchListener);
        mRightBtn.setOnTouchListener(mTouchListener);
        mTouchView.setOnTouchListener(mTouchListener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UDPReceiveManager.getInstance().addReceiveCallback(ReceiveCallback.ReceiveType.Offline, mReceive);
        UDPReceiveManager.getInstance().addReceiveCallback(ReceiveCallback.ReceiveType.UnConnectResponse, mReceive);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            UDPReceiveManager.getInstance().removeReceiveCallback(ReceiveCallback.ReceiveType.Offline);
            UDPReceiveManager.getInstance().removeReceiveCallback(ReceiveCallback.ReceiveType.UnConnectResponse);
        } else {
            UDPReceiveManager.getInstance().addReceiveCallback(ReceiveCallback.ReceiveType.Offline, mReceive);
            UDPReceiveManager.getInstance().addReceiveCallback(ReceiveCallback.ReceiveType.UnConnectResponse, mReceive);
        }
    }

}
