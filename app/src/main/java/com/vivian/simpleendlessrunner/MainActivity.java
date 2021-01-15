package com.vivian.simpleendlessrunner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import java.io.Console;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends Activity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
    }

    class GameView extends SurfaceView implements Runnable {
        Thread gameThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playing;

        Canvas canvas;
        Paint paint;
        Display display;

        long fps;
        private long timeThisFrame;

        Character character;
        EndlessGround endlessGround;
        EndlessMonster endlessMonster;
        Point size;
        MainMenu mainMenu;
        Credit credit;
        Tutorial tutorial;

        boolean gameOver = false;

        private int score = 0;
        private int highScore = 0;
        long startTime = 0;

        Bitmap bg, bgMainMenu, playBtn, creditBtn, quitBtn, backBtn, tutorialBtn, pauseBtn, resumeBtn;
        Bitmap bmCharacter, tileGround, mosnterLand, monsterAir, charaJump, charaCrouch;
        Bitmap tutorialScene, mainmenuBtn, restartBtn, creditScene;

        private float x1, y1, y2;
        private float minimalLengthSwipe = 100;

        private int n = 1;
        private float newSpeed = 9;

        private State.STATE state = State.STATE.MENU;

        private RectF restartButton, mainMenuButton, pauseButton, resumeButton;

        public GameView(Context context) {
            super(context);

            display = getWindowManager().getDefaultDisplay();
            size = new Point();
            display.getSize(size);

            ourHolder = getHolder();
            paint = new Paint();

            bg = BitmapFactory.decodeResource(this.getResources(), R.drawable.background);
            bgMainMenu = BitmapFactory.decodeResource(this.getResources(), R.drawable.bgmainmenu);
            tutorialScene = BitmapFactory.decodeResource(this.getResources(), R.drawable.tutorialscene);
            bmCharacter = BitmapFactory.decodeResource(this.getResources(), R.drawable.astronot);
            charaJump = BitmapFactory.decodeResource(this.getResources(), R.drawable.jump);
            charaCrouch = BitmapFactory.decodeResource(this.getResources(), R.drawable.menunduk);
            playBtn = BitmapFactory.decodeResource(this.getResources(), R.drawable.play);
            creditBtn = BitmapFactory.decodeResource(this.getResources(), R.drawable.credit);
            quitBtn = BitmapFactory.decodeResource(this.getResources(), R.drawable.quit);
            backBtn = BitmapFactory.decodeResource(this.getResources(), R.drawable.back);
            tutorialBtn = BitmapFactory.decodeResource(this.getResources(), R.drawable.tutorial);
            pauseBtn = BitmapFactory.decodeResource(this.getResources(), R.drawable.pause);
            tileGround = BitmapFactory.decodeResource(this.getResources(), R.drawable.tile);
            mosnterLand = BitmapFactory.decodeResource(this.getResources(), R.drawable.land);
            monsterAir = BitmapFactory.decodeResource(this.getResources(), R.drawable.air);
            resumeBtn = BitmapFactory.decodeResource(this.getResources(), R.drawable.resume);
            mainmenuBtn = BitmapFactory.decodeResource(this.getResources(), R.drawable.mainmenu);
            restartBtn = BitmapFactory.decodeResource(this.getResources(), R.drawable.restart);
            creditScene = BitmapFactory.decodeResource(this.getResources(), R.drawable.creditscene);

            bg = Bitmap.createScaledBitmap(bg, size.x, size.y, true);
            tutorialScene = Bitmap.createScaledBitmap(tutorialScene, size.x, size.y, true);
            pauseBtn = Bitmap.createScaledBitmap(pauseBtn, 105, 99, true);
            tileGround = Bitmap.createScaledBitmap(tileGround, size.x/10, 100, true);
            resumeBtn = Bitmap.createScaledBitmap(resumeBtn, 300, 91, true);
            mainmenuBtn = Bitmap.createScaledBitmap(mainmenuBtn, 300, 91, true);
            restartBtn = Bitmap.createScaledBitmap(restartBtn, 300, 91, true);

            mainMenu = new MainMenu(bgMainMenu, playBtn, creditBtn, quitBtn, tutorialBtn, size.x, size.y);
            credit = new Credit(creditScene, backBtn, size.x, size.y);
            tutorial = new Tutorial(tutorialScene, backBtn, size.x, size.y);

            endlessGround = new EndlessGround(tileGround, size.x, size.y );
            character = new Character(bmCharacter, charaJump, charaCrouch, 100, size.y - 100, size.x, size.y);
            endlessMonster = new EndlessMonster(mosnterLand, monsterAir, size.x, size.y);

            restartButton = new RectF(size.x / 2 - 400, size.y / 2, size.x / 2 - 400 + restartBtn.getWidth(), size.y / 2 + restartBtn.getHeight());
            mainMenuButton = new RectF(size.x / 2, size.y / 2, size.x / 2 + mainmenuBtn.getWidth(), size.y / 2 + mainmenuBtn.getHeight());
            pauseButton = new RectF(size.x - 200, 100, size.x - pauseBtn.getWidth(), 100 + pauseBtn.getHeight());
            resumeButton = new RectF(size.x / 2 - 400, size.y / 2, size.x / 2 - 400 + resumeBtn.getWidth(), size.y / 2 + resumeBtn.getHeight());

            playing = true;
        }

        @Override
        public void run() {
            while (playing) {
                long startFrameTime = System.currentTimeMillis();

                if (!gameOver && state == State.STATE.GAMEPLAY){
                    update();
                }

                draw();

                timeThisFrame = System.currentTimeMillis() - startFrameTime;

                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame;
                }
            }
        }

        public void draw() {
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();

                canvas.drawColor(Color.argb(255, 26, 128, 182));

                paint.setColor(Color.argb(255, 249, 129, 0));
                paint.setTextSize(45);

                //draw main menu
                if (state == State.STATE.MENU){
                    mainMenu.draw(canvas, paint);
                }

                if (state == State.STATE.GAMEPLAY){
                    canvas.drawBitmap(bg, 0, 0, paint);
                    //canvas.drawText("FPS : " + fps, 20, 40, paint);

                    //draw button pause
                    canvas.drawBitmap(pauseBtn, size.x - 200, 50, paint);

                    //draw character
                    paint.setColor(Color.argb(255, 100, 249, 15));
                    character.draw(canvas, paint);

                    //draw ground
                    endlessGround.draw(canvas, paint);

                    //draw monster
                    paint.setColor(Color.argb(255, 210, 129, 100));
                    endlessMonster.drawObject(canvas, paint);

                    //draw score
                    canvas.drawText("Score: " + score, (float)size.x / 2, 80, paint);

                    //draw highscore
                    canvas.drawText("Highscore: " + highScore, (float) size.x / 4, 80, paint);
                }

                if (state == State.STATE.PAUSE){
                    canvas.drawBitmap(bg, 0, 0, paint);
                    paint.setTextSize(70);
                    canvas.drawText("PAUSE", (float) ((size.x / 2) - 150), (float) size.y / 2 - 100, paint);
                    canvas.drawBitmap(resumeBtn, size.x/2 - 400, size.y/2, paint);
                    canvas.drawBitmap(mainmenuBtn, size.x/2, size.y/2, paint);
                }

                //draw gameover panel
                if (gameOver && state == State.STATE.GAMEOVER){
                    canvas.drawBitmap(bg, 0, 0, paint);
                    //draw highscore
                    canvas.drawText("Highscore: " + highScore, (float) size.x / 2 - 170, 80, paint);

                    //draw text gameover
                    paint.setTextSize(70);
                    canvas.drawText("GAME OVER", (float) ((size.x / 2) - 230), (float) size.y / 2 - 100, paint);
                    canvas.drawBitmap(restartBtn, size.x/2 - 400, size.y/2, paint);
                    //canvas.drawRect(restartButton,paint);
                    canvas.drawBitmap(mainmenuBtn, size.x/2, size.y/2, paint);
                }

                //draw credit panel
                if (state == State.STATE.CREDIT){
                    credit.draw(canvas, paint);
                }

                //draw tutorial panel
                if (state == State.STATE.TUTORIAL){
                    tutorial.draw(canvas, paint);
                }

                ourHolder.unlockCanvasAndPost(canvas);
            }
        }

        public void update() {
            character.update();
            endlessGround.update();
            endlessMonster.update();

            if (System.currentTimeMillis() - startTime >= 100){
                startTime = System.currentTimeMillis();
                score += 1;
            }

            for (int i = 0; i < endlessMonster.getList().size(); i++){
                if (RectF.intersects(character.getRect(), endlessMonster.getRect(i))){
                    gameOver = true;
                    state = State.STATE.GAMEOVER;
                    if (score >= highScore){
                        highScore = score;
                    }
                    resetScore();
                }
            }

            if (score >= 150 * n){
                if (newSpeed >= 15){
                    newSpeed = 15;
                } else {
                    endlessMonster.setSpeed(newSpeed);
                    newSpeed++;
                    n++;
                }
            }
        }

        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }
        }

        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        private void resetScore(){
            score = 0;
            n = 1;
            newSpeed = 9;
        }

        //Override class View
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    x1 = motionEvent.getX();
                    y1 = motionEvent.getY();

                    if (state == State.STATE.MENU){
                        if (mainMenu.getPlayButton().contains(x1, y1)){
                            state = State.STATE.GAMEPLAY;
                        } else if (mainMenu.getCreditButton().contains(x1, y1)){
                            state = State.STATE.CREDIT;
                        } else if (mainMenu.getTutorialButton().contains(x1, y1)){
                            state = State.STATE.TUTORIAL;
                        }
                    }

                    if (state == State.STATE.CREDIT && credit.getBackButton().contains(x1, y1)){
                        state = State.STATE.MENU;
                    }

                    if (state == State.STATE.TUTORIAL && tutorial.getBackButton().contains(x1, y1)){
                        state = State.STATE.MENU;
                    }

                    if (pauseButton.contains(x1, y1)){
                        state = State.STATE.PAUSE;
                    }

                    if (gameOver && state == State.STATE.GAMEOVER || state == State.STATE.PAUSE){
                        if (restartButton.contains(x1, y1)){
                            state = State.STATE.GAMEPLAY;
                            gameOver = false;
                            endlessMonster.resetPosition();
                            endlessMonster.resetSpeed();
                        } else if (mainMenuButton.contains(x1, y1)){
                            state = State.STATE.MENU;
                            gameOver = false;
                            endlessMonster.resetPosition();
                            endlessMonster.resetSpeed();
                            resetScore();
                        } else if (resumeButton.contains(x1, y1)){
                            state = State.STATE.GAMEPLAY;
                        }
                    }

                    if (state == State.STATE.MENU && mainMenu.getQuitButton().contains(x1, y1)){
                        finish();
                        System.exit(0);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    y2 = motionEvent.getY();

                    float y = y2 - y1;

                    if (Math.abs(y) > minimalLengthSwipe){
                        //bottom swipe
                        if(y2 > y1){
                            //nunduk jika di tanah
                            if (!character.getIsCrouching()){
                                character.resetCrouchForce();
                                character.setIsCrouching(true);
                            }
                        } else{ //top swipe
                            //lompat
                            if (!character.getIsJumping()){
                                character.resetJumpForce();
                                character.setIsJumping(true);
                            }
                        }
                    }
                    //isMoving = false;
                    break;
            }

            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        gameView.pause();
    }
}