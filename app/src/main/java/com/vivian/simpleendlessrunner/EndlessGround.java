package com.vivian.simpleendlessrunner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class EndlessGround implements Endlessable{
    private int speed = 2;
    private int length;
    private int screenX, screenY;
    private Tile tile;
    Bitmap bitmap;

    private List<Tile> tiles;

    public EndlessGround(Bitmap _bitmap, int screenX, int screenY){
        tiles = new ArrayList<>();
        this.screenX = screenX;
        this.screenY = screenY;
        length = this.screenX / 10;

        bitmap = _bitmap;

        addList();
    }

    public void addList(){
        for (int i = 0; i < (screenX / length) + 1; i++) {
            tile = new Tile(bitmap, i * length, screenY - 100, length, 100);
            tiles.add(tile);
        }
    }

    public List getList(){
        return tiles;
    }

    public void update(){
        if (tiles.size() != 0){
            for (int i = 0; i < tiles.size(); i++){
                tiles.get(i).getRect().left -= speed;
                tiles.get(i).getRect().right = tiles.get(i).getRect().left + length;
            }

            if (tiles.get(0).getRect().right < 0){
                tiles.get(0).getRect().left = tiles.get(tiles.size() - 1).getRect().right;
                tiles.get(0).getRect().right = tiles.get(0).getRect().left + length;
                tiles.add(tiles.get(0));
                tiles.remove(tiles.get(0));
            }
        }
    }

    public void draw(Canvas canvas, Paint paint){
        for (int i = 0; i < 11; i++){
            canvas.drawBitmap(tiles.get(i).getBitmap(), i * length, screenY - tiles.get(i).getBitmap().getHeight(), paint);
        }
    }
}
