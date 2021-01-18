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
    int buttonHeight, buttonWidth;

    Bitmap bg, play, credit, quit, tutorial;

    MainMenu(Bitmap _bg, Bitmap _play, Bitmap _credit, Bitmap _quit, Bitmap _tutorial, int _screenX, int _screenY){
        screenX = _screenX;
        screenY = _screenY;

        buttonHeight = screenY / 7;
        buttonWidth = (int) (screenX / 4.5f);

        bg = _bg;
        bg = Bitmap.createScaledBitmap(bg, screenX, screenY, true);

        play = _play;
        play = Bitmap.createScaledBitmap(play, buttonWidth, buttonHeight,true);
        credit = _credit;
        credit = Bitmap.createScaledBitmap(credit, buttonWidth, buttonHeight, true);
        tutorial = _tutorial;
        tutorial = Bitmap.createScaledBitmap(tutorial, buttonWidth, buttonHeight, true);
        quit = _quit;
        quit = Bitmap.createScaledBitmap(quit, buttonWidth, buttonHeight, true);

        posXFirst = screenX/4;
        posXSecond = screenX/2;

        playBtn = new RectF(posXFirst, screenY/2 - buttonHeight + 70, posXFirst + play.getWidth(), screenY/2 - buttonHeight + 70 + play.getHeight());
        tutorialBtn = new RectF(posXSecond, screenY/2 - buttonHeight + 70, posXSecond + tutorial.getWidth(), screenY/2 - buttonHeight + 70 + tutorial.getHeight());
        creditBtn = new RectF(posXFirst, screenY/2 + 140, posXFirst + credit.getWidth(), screenY/2 + 140 + credit.getHeight());
        quitBtn = new RectF(posXSecond, screenY/2 + 140, posXSecond + quit.getWidth(), screenY/2 + 140 + quit.getHeight());
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bg, 0,0, paint);
        canvas.drawBitmap(play, posXFirst, screenY/2 - buttonHeight + 70, paint);
        canvas.drawBitmap(tutorial, posXSecond, screenY/2 - buttonHeight + 70, paint);
        canvas.drawBitmap(credit, posXFirst, screenY/2 + 140, paint);
        canvas.drawBitmap(quit, posXSecond, screenY/2 + 140, paint);
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
