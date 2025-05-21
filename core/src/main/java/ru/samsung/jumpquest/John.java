package ru.samsung.jumpquest;

import static ru.samsung.jumpquest.Main.*;

import com.badlogic.gdx.utils.TimeUtils;

public class John extends Object{
    public int phase, nPhases = 2;
    private long timeLastPhase, timePhaseInterval = 150;
    private float gravity = -0.5f;
    public boolean isOnGround = false;
    public float jumpForce = 0;
    private float maxJumpForce = 30f;
    private float speedOfCharging = 1f;
    private long touchStartTime = 0;
    public boolean isCharging = false;
    public John(float x, float y) {
        super(x, y);
        width = 200;
        height = 200;
    }

    @Override
    public void move() {
        super.move();
        vy += gravity;
        changePhase();
        outOfScreen();

        if (isCharging) {
            jumpForce += speedOfCharging;
            if (jumpForce > maxJumpForce) jumpForce = maxJumpForce;
        }
    }

    private void outOfScreen() {
        if(x < width/2) {
            vx = 0;
            x = width/2;
        }
        if(x > SCR_WIDTH - width/2) {
            vx = 0;
            x = SCR_WIDTH - width/2;
        }
        if(y > SCR_HEIGHT - height/2) {
            vy = 0;
            y = SCR_HEIGHT - height/2;
        }
    }

    private void changePhase() {
        if(TimeUtils.millis() > timeLastPhase + timePhaseInterval){
            if(++phase==nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    public void startCharging() {
        isCharging = true;
        touchStartTime = TimeUtils.millis();

    }

    public void endJump() {
        if (isCharging) {
            isCharging = false;
            if (isOnGround) {
                vy = jumpForce;
            }
            isOnGround = false;
            jumpForce = 0;
        }
    }
}
