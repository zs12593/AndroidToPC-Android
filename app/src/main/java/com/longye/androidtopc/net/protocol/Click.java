package com.longye.androidtopc.net.protocol;

public class Click extends Cursor {
    public static final int LEFT_BUTTON = 0;
    public static final int RIGHT_BUTTON = 1;

    public static final int STATE_DOWN = 0;
    public static final int STATE_UP = 1;
    public static final int STATE_DOWN_UP = 2;

    private int button;//0 左键 1 右键
    private int state;

    public Click(String password, int button, int state) {
        super(password);
        this.button = button;
        this.state = state;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
