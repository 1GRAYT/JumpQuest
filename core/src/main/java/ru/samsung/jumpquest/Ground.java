package ru.samsung.jumpquest;

import com.badlogic.gdx.math.MathUtils;

public class Ground extends Object {
    public int type;
    public float realHeight;

    public Ground(float x, float y) {
        super(x, y);
        type = MathUtils.random(1, 3);
        width = 300;
        height = 300*type;
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
