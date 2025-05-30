package ru.samsung.jumpquest;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Star extends Object {
    public int price;

    public Star(float x, float y) {
        super(x, y);
        width = 100;
        height = 100;
        price = MathUtils.random(30, 90);
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
