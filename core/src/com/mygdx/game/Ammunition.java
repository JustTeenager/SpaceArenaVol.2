package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.Align;

public class Ammunition extends ActorObj {
    private Texture txtAmmunition;
    private float x;
    private float y;
    private Rectangle recAmmunition;
    private Stage stage;

    Ammunition(Texture txtAmmunition, float x, float y, Stage  stage){
        this.stage=stage;
        this.txtAmmunition=txtAmmunition;
        this.x=x;
        this.y=y;
        setPosition(x,y);
        recAmmunition = new Rectangle();
        recAmmunition.set(getX(),getY(),txtAmmunition.getWidth(),txtAmmunition.getHeight());
        this.setTransform(true);
        this.setOrigin(Align.center);
        this.addAction(Actions.repeat(RepeatAction.FOREVER,
                Actions.sequence(
                        Actions.moveBy(75,75),
                        Actions.moveBy(-75,-75,2.5f))));
        this.stage.addActor(this);
    }

    public void collapse(Player player){
        if (this.recAmmunition.overlaps(player.rectangle) && this.isVisible()){
            player.amountBullets=MainGame.AMOUNT_BULLETS;//восстанавливаем количество пуль до начального значения
            this.setVisible(false);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void platformReact(Platform pl) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //super.draw(batch, parentAlpha);
        batch.draw(txtAmmunition,this.getX(),this.getY());
    }
}