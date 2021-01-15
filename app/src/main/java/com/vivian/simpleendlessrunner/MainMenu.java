package com.vivian.simpleendlessrunner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

public class MainMenu {
    int screenX, screenY;
    RectF playBtn, creditBtn, quitBtn, tutorialBtn;
    int posXFirst, posXSecond;

    Bitmap bg, play, credit, quit, tutorial;

    MainMenu(Bitmap _bg, Bitmap _play, Bitmap _credit, Bitmap _quit, Bitmap _tutorial, int _screenX, int _screenY){
        screenX = _screenX;
        screenY = _screenY;

        bg = _bg;
        bg = Bitmap.createScaledBitmap(bg, screenX, screenY, true);

        play = _play;
        play = Bitmap.createScaledBitmap(play, 300, 91,true);
        credit = _credit;
        credit = Bitmap.createScaledBitmap(credit, 300, 91, true);
        tutorial = _tutorial;
        tutorial = Bitmap.createScaledBitmap(tutorial, 300, 91, true);
        quit = _quit;
        quit = Bitmap.createScaledBitmap(quit, 300, 91, true);

        posXFirst = screenX/4;
        posXSecond = screenX/2;

        playBtn = new RectF(posXFirst, 300, posXFirst + play.getWidth(), 300 + play.getHeight());
        tutorialBtn = new RectF(posXSecond, 300, posXSecond + tutorial.getWidth(), 300 + tutorial.getHeight());
        creditBtn = new RectF(posXFirst, 450, posXFirst + credit.getWidth(), 450 + credit.getHeight());
        quitBtn = new RectF(posXSecond, 450, posXSecond + quit.getWidth(), 450 + quit.getHeight());
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bg, 0,0, paint);
        canvas.drawBitmap(play, posXFirst, 300, paint);
        canvas.drawBitmap(tutorial, posXSecond, 300, paint);
        canvas.drawBitmap(credit, posXFirst, 450, paint);
        canvas.drawBitmap(quit, posXSecond, 450, paint);
    }

    public RectF getPlayButton(){
        return playBtn;
    }

    public RectF getTutorialButton(){
        return tutorialBtn;
    }

    public RectF getCreditButton(){
        return creditBtn;
    }

    public RectF getQuitButton(){
        return quitBtn;
    }
}
