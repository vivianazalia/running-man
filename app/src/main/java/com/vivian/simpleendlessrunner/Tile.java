package com.vivian.simpleendlessrunner;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.provider.FontRequest;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    private RectF rect;
    private Bitmap tile;

    public Tile(Bitmap _tile, int x, int y, int length, int height){
        rect = new RectF(x, y, x + length, y + height);
        tile = _tile;
    }

    public Bitmap getBitmap(){
        return tile;
    }

    public RectF getRect(){
        return rect;
    }
}
