package com.mygdx.game.HUD;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Core.ArenaGame;
import com.mygdx.game.GameObjects.Shooting;
import com.mygdx.game.Core.MainGame;
//класс правого джойстика, который отвечает за стрельбу
public class JoystickRight extends BaseJoystick {
    private Texture circle;
    private Texture circleCur;
    private float curX=0;
    private float curY=0;
    public static Vector2 direction;
    public static boolean isTouchRight= false;
    private float rad = 0;
    public static double angleRight=0;
    public static boolean checkAngleRight= true;//отвечает за проверку угла, если угол лежит в пределах от 90 до 270, то переменная равняется false, иначе true

    public static Shooting shootTemp=new Shooting(5000,5000, ArenaGame.playerStage,new Vector2(5000,5000), MainGame.VELOCITY_BULLETS);

    private static final float CURSOR_RADIUS = 60;
    public JoystickRight(Texture circle, Texture circleCur){
        super();
        this.circle = circle;
        this.circleCur = circleCur;
        direction = new Vector2();
        setDefault();
        setDefaultXY();
        setVisible(false);
    }

    public void isTouch(float x,float y) { // проверяем коснулись ли мы курсора
        if ((x>=1550 && x<=1870) &&(y>=720 && y<=1040)) {
            isTouchRight = true;
        }
    }

    public void setDefault(){// задаётся ширина,высота и радиус большого круга
        setWidth(320);
        setHeight(320);
        rad = 160;

    }
    public void setDefaultXY(){//задаётся левый нижний угол квадрата для большого круга
        setX(1550);
        setY(40);
    }
    public void setWidth(float w){
        super.setWidth(w);
        super.setHeight(w);
        rad = w/2;
    }
    public void setHeight(float h){
        super.setWidth(h);
        super.setHeight(h);
        rad = h/2;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1,1,1,0.5f);
        batch.draw(circle,this.getX(),this.getY(),this.getWidth(),this.getHeight());
        if (MainGame.seconds==0){
            batch.draw(circleCur,this.getX()+rad-CURSOR_RADIUS,
                    this.getY()+rad - CURSOR_RADIUS,
                    2*CURSOR_RADIUS,
                    2*CURSOR_RADIUS);
        }
        else if (isTouchRight){// если курсор двигается
            batch.draw(circleCur,this.getX()+rad-CURSOR_RADIUS+curX,
                    this.getY()+rad - CURSOR_RADIUS+curY,
                    2*CURSOR_RADIUS,
                    2*CURSOR_RADIUS);
        }
        else{
            batch.draw(circleCur,this.getX()+rad-CURSOR_RADIUS,
                this.getY()+rad - CURSOR_RADIUS,
                2*CURSOR_RADIUS,
                2*CURSOR_RADIUS);
        }
    }
    public void changeCur(float x, float y){// функция , которая не даёт курсору выйти за границы большого круга
        float dx = x - rad-1550;
        float dy =720-(y - rad);
        float length = (float) Math.sqrt(dx*dx+dy*dy);
        float k = rad/length;
        if (length<rad){
            this.curX = dx;
            this.curY =dy;
        }
        else{
            this.curX = k*dx;
            this.curY = k*dy;
        }

        direction.x = curX/length;
        direction.y = curY/length;
    }
    public void setAngle(){//функция которая определяет на какой угол отклонён курсор
        angleRight = Math.atan(curY/curX)*180/Math.PI;
        if(angleRight>0 &&curY<0)
            angleRight+=180;
        if(angleRight <0) {
            if (curX < 0)
                angleRight = 180 + angleRight;
            else
                angleRight += 360;
        }

        if (angleRight>0 && angleRight<=90 || angleRight<360 && angleRight>=270){
            checkAngleRight = true;
        }
        else {
            checkAngleRight = false;
        }

    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX>1000 && screenY>700 && MainGame.seconds>0) {
            isTouch(screenX, screenY);
            if (isTouchRight) {
                changeCur(screenX, screenY);
                setVisible(true);
                setAngle();
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screenX>1000 && MainGame.seconds>0) {
            isTouchRight = false;
            angleRight = 0;
        }
        setVisible(false);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (screenX>1000 && screenY>700 && MainGame.seconds>0) {
            changeCur(screenX, screenY);
            setAngle();
        }
        return false;
    }
    //две приватные переменные для регулирования скорострельности
    private float one = Gdx.graphics.getDeltaTime();
    private float two = Gdx.graphics.getDeltaTime()-5;
    public void checkCreateBullet(){//функция для создания пуль
        if (JoystickRight.isTouchRight && JoystickLeft.checkAngleLeft==checkAngleRight && one-two>=6 && !ArenaGame.CURRENT_PLAYER.killed && MainGame.seconds>0 && ArenaGame.CURRENT_PLAYER.amountBullets>0){
            MainGame.isShooted=true;
            shootTemp=new Shooting(ArenaGame.CURRENT_PLAYER.getX(),ArenaGame.CURRENT_PLAYER.getY(),ArenaGame.playerStage,JoystickRight.direction,MainGame.VELOCITY_BULLETS);
            shootTemp.setRotation((float)angleRight);
            ArenaGame.shootings.add(shootTemp);
            two=one;
            ArenaGame.CURRENT_PLAYER.amountBullets--;
        }
        else{
            one++;
        }
    }
}
