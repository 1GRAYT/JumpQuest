package ru.samsung.jumpquest;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Star extends Object {
    public float realHeight;
    public int price;
    public int phase, nPhases = 8;
    public boolean isDead;
    private long timeLastPhase, timePhaseInterval = 50;

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

    public void changePhase() {
        if (TimeUtils.millis() > timeLastPhase + timePhaseInterval) {
            if (++phase == nPhases) {
                isDead = true;
                phase = nPhases - 1;
            }
            timeLastPhase = TimeUtils.millis();
        }

    }

    public boolean outOfScreen() {
        return x < -width/2;
    }

}
