package com.mygdx.game.Client;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
//основной класс для передачи информации,по сути - собственный тип данных
public class CoordBox {

    private int playerIdentify;
    public boolean flipped;
    public Vector2 boxPositionPlayer;
    public int boxPlayerAnimNumber;
    public Rectangle boxRectanglePlayer;
    public int boxHp;
    public Vector2 boxBulletsPosition;
    public Rectangle boxRectangleShoot;
    public Double boxAngles;
    public String boxTime;
    public int seconds;
    //основной конструктор для передачи данных(с пулями)
    public CoordBox(int playerIdentify, Vector2 positionPlayer, int playerAnimNumber, boolean flipped, Rectangle rectanglePlayer, int hp,
                    Vector2 bulletsPosition, Double angle, Rectangle rectangleShoot){
        this.playerIdentify=playerIdentify;
        this.flipped=flipped;
        boxPositionPlayer=positionPlayer;
        boxPlayerAnimNumber=playerAnimNumber;
        boxRectanglePlayer=rectanglePlayer;
        boxHp=hp;

        boxBulletsPosition = bulletsPosition;
        boxAngles = angle;
        boxRectangleShoot = rectangleShoot;
    }
    //самый первый пакет данных
    public CoordBox(int playerIdentify){
        this.playerIdentify=playerIdentify;
    }
    //основной конструктор для передачи данных(без пуль)
    public CoordBox(int playerIdentify, Vector2 positionPlayer, int playerAnimNumber, boolean flipped, Rectangle rectanglePlayer, int hp){
        this.playerIdentify=playerIdentify;
        this.flipped=flipped;
        boxPositionPlayer=positionPlayer;
        boxPlayerAnimNumber=playerAnimNumber;
        boxRectanglePlayer=rectanglePlayer;
        boxHp=hp;
    }

    CoordBox(){}

    public int getPlayerIdentify() {
        return playerIdentify;
    }

    public void setPlayerIdentify(int playerIdentify) {
        this.playerIdentify = playerIdentify;
    }
}

