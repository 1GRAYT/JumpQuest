package ru.samsung.jumpquest;

public class Object {
    public int type;
    public float x, y;
    public float width, height;
    public float vx, vy;

    public Object(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        x += vx;
        y += vy;
    }

    public float scrX() {
        return x-width/2;
    }

    public float scrY() {
        return y-height/2;
    }

    public boolean overlap(Object o) {
        return Math.abs(x-o.x) < width/2 + o.width/2 && Math.abs(y-o.y) < height/2 + o.height/2;
    }
}
