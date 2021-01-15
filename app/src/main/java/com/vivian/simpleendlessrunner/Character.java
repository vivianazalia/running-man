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

    private float jumpForce;
    private float crouchForce;
    private boolean isJumping;
    private boolean isGrounded;
    private boolean isCrouching;

    private Rect frameToDraw;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 100;
    private int currentFrame = 0;

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

        screenX = _screenX;
        screenY = _screenY;
    }

    public void draw(Canvas canvas, Paint paint){
        getCurrentFrame();
        if (isJumping){
            frameCount = 7;
            canvas.drawBitmap(jump, frameToDraw, pos, paint);
        } else if (isCrouching){
            frameCount = 5;
            canvas.drawBitmap(crouch, frameToDraw , pos, paint);
        } else {
            frameCount = 8;
            canvas.drawBitmap(idle, frameToDraw , pos, paint);
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

    public RectF getRect(){
        return pos;
    }

    public void update(){
        jump();
        if (isGrounded){
            crouch();
        }
    }

    public void jump() {
        if (pos.bottom >= screenY - 100){
            pos.bottom = screenY - 100;
            isJumping = false;
            isGrounded = true;
        } else {
            jumpForce += GRAVITY;
            pos.top += jumpForce;
            pos.bottom = pos.top + frameHeight;
            isGrounded = false;
        }
    }

    public void resetJumpForce(){
        jumpForce = -15;
        pos.top += jumpForce;
        pos.bottom = pos.top + frameHeight;
    }

    public void setIsJumping(boolean b){
        isJumping = b;
    }

    public boolean getIsJumping(){
        return isJumping;
    }

    public void crouch(){
        if (pos.top <= screenY - frameHeight - 100){
            pos.top = screenY - frameHeight - 100;
            isCrouching = false;
        } else {
            crouchForce -= GRAVITY;
            pos.top += crouchForce;
            isCrouching = true;
        }
    }

    public void resetCrouchForce(){
        crouchForce = 12;
        pos.top += crouchForce;
    }

    public void setIsCrouching(boolean b){
        isCrouching = b;
    }

    public boolean getIsCrouching(){
        return isCrouching;
    }

}
