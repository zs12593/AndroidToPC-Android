package com.longye.androidtopc.net.protocol;

public class MoveCursor extends Cursor {
    private int x;
    private int y;

    public MoveCursor(String password, int x, int y) {
        super(password);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
