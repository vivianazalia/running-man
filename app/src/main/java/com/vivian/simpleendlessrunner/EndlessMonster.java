package com.vivian.simpleendlessrunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.print.PrinterId;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EndlessMonster implements Endlessable{
    private float speed = 8;
    private int screenX, screenY;

    private List<Monster> monsters;

    private Random rand;

    private float xPos;
    Bitmap monsterLand, monsterAir;

    public EndlessMonster(Bitmap _monsterLand, Bitmap _monsterAir, int screenX, int screenY){
        monsters = new ArrayList<>();
        rand = new Random();

        monsterLand = _monsterLand;
        monsterAir = _monsterAir;

        this.screenX = screenX;
        this.screenY = screenY;
    }

    private Monster createMonster(){
        int i = rand.nextInt(2);
        if (i == 0){
            return new Monster(monsterLand, 4, getPosition(), screenY - 160, 50, 110);
        } else {
            return new Monster(monsterAir, 8, getPosition(), screenY - 380, 20, 110);
        }
    }

    public void addList(){
        if (monsters.size() < 10){
            monsters.add(createMonster());
        }
    }

    public void drawObject(Canvas c, Paint p){
        for (int i = 0; i < monsters.size(); i++){
            monsters.get(i).drawMonster(c, p);
        }
    }

    public List getList(){
        return monsters;
    }

    public RectF getRect(int index){
        return monsters.get(index).getCollider();
    }

    public void update(){
        addList();
        if (!monsters.isEmpty()){
            for (int i = 0; i < monsters.size(); i++){
                monsters.get(i).getRect().left -= speed;
                monsters.get(i).getCollider().left -= speed;
                monsters.get(i).getRect().right = (monsters.get(i).getRect().left + monsters.get(i).getWidth());
                monsters.get(i).getCollider().right = monsters.get(i).getCollider().left + monsters.get(i).getWidth() - 130;
            }

            if (monsters.get(0).getRect().right < 0){
                float position = getPosition();
                monsters.get(0).getRect().left = position;
                monsters.get(0).getCollider().left = position + 60;
                monsters.get(0).getRect().right = (monsters.get(0).getRect().left + monsters.get(0).getWidth());
                monsters.get(0).getCollider().right = monsters.get(0).getCollider().left + monsters.get(0).getWidth() - 130;
                monsters.add(monsters.get(0));
                monsters.remove(monsters.get(0));
            }
        }
    }

    private float getPosition(){
        if (monsters.isEmpty()){
            xPos = (float) (screenX + 400 + rand.nextInt(500));
        } else {
            xPos = (float) (monsters.get(monsters.size() - 1).getRect().right + 400 + rand.nextInt(500));
        }
        return xPos;
    }

    public void resetPosition(){
        monsters.clear();
    }

    public void setSpeed(float newSpeed){
        speed = newSpeed;
    }

    public void resetSpeed(){
        speed = 8;
    }
}
