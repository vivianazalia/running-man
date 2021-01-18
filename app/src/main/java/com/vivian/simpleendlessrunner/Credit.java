package com.vivian.simpleendlessrunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Credit {
    int screenX, screenY;
    RectF backButton;

    Bitmap bg, backBtn;

    Credit(Bitmap _bg, Bitmap _backBtn, int _screenX, int _screenY){
        screenX = _screenX;
        screenY = _screenY;

        bg = _bg;
        bg = Bitmap.createScaledBitmap(bg, screenX, screenY, true);
        backBtn = _backBtn;
        backBtn = Bitmap.createScaledBitmap(backBtn, (int) (screenX / 4.5f), screenY / 7, true);

        backButton = new RectF(30, screenY - 180, 30 + backBtn.getWidth(), (screenY - 180) + backBtn.getHeight());
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bg, 0,0, paint);
        canvas.drawBitmap(backBtn, 30, screenY - 180, paint);
    }

    public RectF getBackButton(){
        return backButton;
    }
}
