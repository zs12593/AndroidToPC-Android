package com.longye.androidtopc.net.protocol;

public class Click extends Cursor {
    public static final int LEFT_BUTTON = 0;
    public static final int RIGHT_BUTTON = 1;

    private int button;//0 左键 1 右键

    public Click(String password, int button) {
        super(password);
        this.button = button;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }
}
