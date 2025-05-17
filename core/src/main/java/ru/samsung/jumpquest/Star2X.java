package ru.samsung.jumpquest;

import com.badlogic.gdx.math.MathUtils;

public class Star2X extends Object {
    public Star2X(float x, float y) {
        super(x, y);
        width = 100;
        height = 100;
    }

    public void move() {
        super.move();
        if(!ScreenGame.isGameOver) {
            vx -= 0.02;
        } else {
            vx = 0;
        }
    }

    public boolean outOfScreen() {
        return x < -width/2;
    }

}
