package ru.samsung.jumpquest;

import static ru.samsung.jumpquest.Main.*;

public class Sky extends Object {
    public Sky(float x, float y) {
        super(x, y);
        width = SCR_WIDTH;
        height = SCR_HEIGHT;
        vx = -1;
    }

    @Override
    public void move() {
        super.move();
        if(x<-SCR_WIDTH) x = SCR_WIDTH;
    }
}
