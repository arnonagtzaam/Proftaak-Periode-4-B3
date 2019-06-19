package com.appsfromholland.mqtt_example.gameLogics;

import java.io.Serializable;


/*
Een Square is onderdeel van een PlayField. Die er 9 bevat.
 */
public class Square {

    public enum SquareProperty {
        BLANK, RED, BLUE
    }

    public SquareProperty property;
    private int position;
    private boolean isClicked;

    public Square(int position, SquareProperty property){
        this.position = position;
        this.property = property;
        this.isClicked = false;
    }

    public int getPosition() {
        return position;
    }

    public SquareProperty getProperty() {
        return property;
    }

    public void setProperty(SquareProperty property) {
        this.property = property;
    }

    public void setClicked(){
        isClicked = true;
    }

    public boolean isClicked(){
        return isClicked;
    }
}
