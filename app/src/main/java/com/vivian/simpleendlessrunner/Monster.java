package com.vivian.simpleendlessrunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Monster {
    private RectF whereToDraw;
    private Rect frameToDraw;
    Bitmap img;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 100;
    private int currentFrame = 0;
    private int frameCount;

    private int frameWidth;
    private int frameHeight;

    public Monster(Bitmap _img, int _frameCount, float x, int y, int _frameWidth, int _frameHeight){
        //width = _width;
        frameCount = _frameCount;
        frameWidth = _frameWidth;
        frameHeight = _frameHeight;

        img = _img;
        img = Bitmap.createScaledBitmap(img, frameWidth * frameCount, frameHeight, true);

        whereToDraw = new RectF(x, y, x + img.getWidth(), y + img.getHeight());
        frameToDraw = new Rect(0,0, frameWidth, frameHeight);
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

    public void drawMonster(Canvas canvas, Paint paint){
        getCurrentFrame();;
        canvas.drawBitmap(img, frameToDraw, whereToDraw, paint);
    }

    public RectF getRect(){
        return whereToDraw;
    }

    public float getWidth(){
        return img.getWidth();
    }
}
