package com.example.dbgame;


import android.graphics.Rect;
/**
 * @author chirag vijay 27009630
 * Abstract class
 */
public abstract class Object {
    protected int x,y, width, height; //dimensions

    Object(){
        this(10, 10, 240, 20); //default constructor
    }


    Object(int xPos, int yPos, int w, int h){ //overloaded constructor
        x = xPos; //set x pos
        y = yPos; // set y pos
        width = w; // set width
        height = h; // set height
    }


    Rect getCollShape()
    {
        return new Rect(x, y, x + width, y + height); // draw rect around object
    }
}

