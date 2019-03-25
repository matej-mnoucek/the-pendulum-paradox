package com.pendulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.pendulumparadox.observer.Event;
import com.pendulumparadox.observer.EventArgs;

import java.util.BitSet;

public class InGameScreen extends BaseScreen
{
    Label lifeLabel;
    Label lifeCount;
    Label ammoLabel;
    Label ammoCount;
    Image weapon;
    Image leftImg;
    Image rightImg;
    Image jumpImg;
    Image shootImg;

    Skin skin;

    int lifeCounter;
    int ammoCounter;

    // Button events
    // EventArgs == no parameters sent with the message
    // Subclassing EventArgs == a way how to pass custom data
    Event<EventArgs> shootEvent = new Event<>();
    Event<EventArgs> jumpEvent = new Event<>();
    Event<EventArgs> leftEvent = new Event<>();
    Event<EventArgs> rightEvent = new Event<>();

    public InGameScreen()
    {
        super();

        BitmapFont font = new BitmapFont();
        font.getData().scale(0.2f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table hudTable = new Table();
        hudTable.top();
        hudTable.setFillParent(true);

        Table moveBtnTable = new Table();
        moveBtnTable.bottom().left();
        moveBtnTable.setFillParent(true);
        //moveBtnTable.setDebug(true);
        Table actionBtnTable = new Table();
        actionBtnTable.bottom().right();
        actionBtnTable.setFillParent(true);
        //actionBtnTable.setDebug(true);

        Gdx.input.setInputProcessor(stage);


        lifeCounter = 3;
        ammoCounter = 100;


        lifeLabel = new Label("LIVES: ", labelStyle);
        lifeCount = new Label(String.format("%01d", lifeCounter),labelStyle);
        ammoLabel = new Label("Ammo:", labelStyle);
        ammoCount = new Label(String.format("%03d", ammoCounter), labelStyle);
        weapon = new Image(new Texture("ak47.png"));
        leftImg = new Image(new Texture("leftArrow.png"));
        rightImg = new Image(new Texture("rightArrow.png"));
        jumpImg = new Image(new Texture("jumpButton.png"));
        shootImg = new Image(new Texture("shootButton.png"));

        leftImg.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // This is just invoking the already defined event
                // We are not passing any data == null for EventArgs argument
                leftEvent.invoke(null);
            }
        });
        rightImg.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightEvent.invoke(null);
            }
        });
        jumpImg.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jumpEvent.invoke(null);
            }
        });
        shootImg.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                shootEvent.invoke(null);
            }
        });

        hudTable.add(ammoLabel).expandX().align(Align.left);
        hudTable.add(lifeLabel).expandX().align(Align.right);
        hudTable.row();
        hudTable.add(ammoCount).align(Align.left).padLeft(10);
        hudTable.add(lifeCount).align(Align.right).padRight(20);
        hudTable.row();
        hudTable.add(weapon).expandX().width(100).height(50).align(Align.left);
        moveBtnTable.add(leftImg).width(60).height(60).padLeft(30).padBottom(30);
        moveBtnTable.add(rightImg).width(60).height(60).padBottom(30).padLeft(10);
        actionBtnTable.add(shootImg).width(60).height(60).padBottom(30).padRight(10);
        actionBtnTable.add(jumpImg).width(60).height(60).padRight(30).padBottom(30);
        stage.addActor(hudTable);
        stage.addActor(moveBtnTable);
        stage.addActor(actionBtnTable);
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    public Event<EventArgs> getShootEvent() {
        return shootEvent;
    }

    public Event<EventArgs> getJumpEvent() {
        return jumpEvent;
    }

    public Event<EventArgs> getLeftEvent() {
        return leftEvent;
    }

    public Event<EventArgs> getRightEvent() {
        return rightEvent;
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
