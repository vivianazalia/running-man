package com.vivian.simpleendlessrunner;

import android.app.Notification;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.icu.text.SymbolTable;
import android.os.Debug;

public class Character {
    private RectF pos;
    private Bitmap idle, jump, crouch;

    private final float GRAVITY = 0.5f;
    private int screenX, screenY;

    private int frameWidth = 164;
    private int frameHeight = 200;
    private int frameCount = 8;

    private float jumpForce = -10;
    private boolean isJumping = false;
    private boolean isCrouching = false;

    private Rect frameToDraw;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 100;
    private int currentFrame = 0;

    private RectF collider;

    //Constructor Character class
    public Character(Bitmap _idle, Bitmap _jump, Bitmap _crouch, int x, int y, int _screenX, int _screenY){
        idle = _idle;
        jump = _jump;
        crouch = _crouch;

        idle = Bitmap.createScaledBitmap(idle, frameWidth * 8, frameHeight, false);
        jump = Bitmap.createScaledBitmap(jump, frameWidth * 7, frameHeight, false);
        crouch = Bitmap.createScaledBitmap(crouch, frameWidth * 5, frameHeight, false);

        pos = new RectF(x, y - frameHeight, x + frameWidth, y);
        frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
        collider = new RectF(x + 50, y - frameHeight, (x + 50) + frameWidth - 70, y);

        screenX = _screenX;
        screenY = _screenY;
    }

    public void draw(Canvas canvas, Paint paint){
        getCurrentFrame();
        if (isJumping){
            frameCount = 7;
            canvas.drawBitmap(jump, frameToDraw, pos, null);
        } else if (isCrouching){
            frameCount = 5;
            canvas.drawBitmap(crouch, frameToDraw , pos, null);
        } else {
            frameCount = 8;
            canvas.drawBitmap(idle, frameToDraw, pos, null);
        }
    }

    public void getCurrentFrame() {
        long time = System.currentTimeMillis();
        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;
            currentFrame++;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }
        }

        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;
    }

    public RectF getCollider(){
        return collider;
    }

    public void update(){
        if (isJumping){
            jump();
        } else if (isCrouching){
            crouch();
        }
    }

    public void jump() {
        if (collider.bottom >= screenY - 100){
            currentFrame = 0;
            collider.bottom -= 150;
        } else if (collider.bottom < screenY - 100 && currentFrame >= frameCount - 1){
            collider.bottom = screenY - 100;
            pos.top = screenY - frameHeight - 100;
            pos.bottom = screenY - 100;
            jumpForce = -10;
            isJumping = false;
        } else if (collider.bottom < screenY - 100){
            jumpForce += GRAVITY;
            pos.top += jumpForce;
            pos.bottom = pos.top + jump.getHeight();
        }
    }

    public void setIsJumping(boolean b){
        isJumping = b;
    }

    public boolean getIsJumping(){
        return isJumping;
    }

    public void crouch(){
        if (collider.top <= screenY - frameHeight - 100){
            currentFrame = 0;
            collider.top += 150;
            isCrouching = true;
        } else if (collider.top > screenY - frameHeight -100 && currentFrame >= frameCount - 1){
            collider.top = screenY - frameHeight - 100;
            isCrouching = false;
        }
    }

    public void setIsCrouching(boolean b){
        isCrouching = b;
    }

    public boolean getIsCrouching(){
        return isCrouching;
    }

    public void resetState(){
        isJumping = false;
        isCrouching = false;
    }

}
