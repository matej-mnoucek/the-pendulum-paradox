package com.thependulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class InGameScreen extends Screen
{
    private int lifeCounter;
    private int ammoCounter;
    Boolean upPressed;

    @Override
    public void create()
    {
        super.create();

        BitmapFont font = new BitmapFont();
        font.getData().scale(0.2f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

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


        Label lifeLabel = new Label("LIVES: ", labelStyle);
        Label lifeCount = new Label(String.format("%01d", lifeCounter),labelStyle);
        Label ammoLabel = new Label("Ammo:", labelStyle);
        Label ammoCount = new Label(String.format("%03d", ammoCounter), labelStyle);
        Image weapon = new Image(new Texture("ak47.png"));
        Image leftImg = new Image(new Texture("leftArrow.png"));
        Image rightImg = new Image(new Texture("rightArrow.png"));
        Image jumpImg = new Image(new Texture("jumpButton.png"));
        Image shootImg = new Image(new Texture("shootButton.png"));

        leftImg.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnLeftTouched();
            }
        });
        rightImg.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnRightTouched();
            }
        });
        jumpImg.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnJumpTouched();
            }
        });
        shootImg.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnShootTouched();
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
    public void resize(int width, int height) {

    }

    public void btnShootTouched(){
        assert true;
    }
    public void btnJumpTouched(){
        assert true;
    }
    public void btnRightTouched(){
        assert true;
    }
    public void btnLeftTouched(){
        assert true;
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    public void dispose() {
    }
}
