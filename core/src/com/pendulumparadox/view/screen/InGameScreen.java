package com.pendulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;



public class InGameScreen extends Screen
{
    private Label lifeLabel;
    private Label lifeCount;
    private Label ammoLabel;
    private Label ammoCount;
    private Image weapon;
    private Image leftImg;
    private Image rightImg;
    private Image jumpImg;
    private Image shootImg;
    private Texture weaponTexture;
    private Texture leftTexture;
    private Texture rightTexture;
    private Texture jumpTexture;
    private Texture shootTexture;
    private BitmapFont font24;
    private Skin skin;

    private int lifeCounter;
    private int ammoCounter;

    private Table hudTable;
    private Table moveBtnTable;
    private Table actionBtnTable;

    @Override
    public void create()
    {
        super.create();

        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        //table for holding HUD objects
        hudTable = new Table();
        hudTable.top();
        hudTable.setFillParent(true);

        //table for holding move buttons. left bottom side of screen
        moveBtnTable = new Table();
        moveBtnTable.bottom().left();
        moveBtnTable.setFillParent(true);
        moveBtnTable.setDebug(true);

        //table for holding shoot and jump buttons. Right bottom side of screen
        actionBtnTable = new Table();
        actionBtnTable.bottom().right();
        actionBtnTable.setFillParent(true);
        actionBtnTable.setDebug(true);

        //set input processor
        Gdx.input.setInputProcessor(stage);


        lifeCounter = 3;
        ammoCounter = 100;

        //create labels for lives and ammo
        lifeLabel = new Label("LIVES: ", labelStyle);
        lifeCount = new Label(String.format("%01d", lifeCounter),labelStyle);
        ammoLabel = new Label("Ammo:", labelStyle);
        ammoCount = new Label(String.format("%03d", ammoCounter), labelStyle);

        //HUD Texture for weapon, move buttons and action buttons
        weaponTexture = new Texture("ak47.png");
        leftTexture = new Texture("leftArrow.png");
        rightTexture = new Texture("rightArrow.png");
        jumpTexture = new Texture("jumpButton.png");
        shootTexture = new Texture("shootButton.png");

        //image for weapon button, move button action button
        weapon = new Image(weaponTexture);
        leftImg = new Image(leftTexture);
        rightImg = new Image(rightTexture);
        jumpImg = new Image(jumpTexture);
        shootImg = new Image(shootTexture);

        //add clicklistneres for buttons
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

        //add ammo, lives and weapon to HUD table
        hudTable.add(ammoLabel).expandX().align(Align.left);
        hudTable.add(lifeLabel).expandX().align(Align.right);
        hudTable.row();
        hudTable.add(ammoCount).align(Align.left).padLeft(10);
        hudTable.add(lifeCount).align(Align.right).padRight(20);
        hudTable.row();
        hudTable.add(weapon).expandX().width(100).height(50).align(Align.left);

        //add move buttons to movebtnTable
        moveBtnTable.add(leftImg).width(60).height(60).padLeft(30).padBottom(30);
        moveBtnTable.add(rightImg).width(60).height(60).padBottom(30).padLeft(10);

        //add action buttons to actionBtnTable
        actionBtnTable.add(shootImg).width(60).height(60).padBottom(30).padRight(10);
        actionBtnTable.add(jumpImg).width(60).height(60).padRight(30).padBottom(30);

        //add tables to Stage
        stage.addActor(hudTable);
        stage.addActor(moveBtnTable);
        stage.addActor(actionBtnTable);
    }

    private void initFonts(){
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/freeagentbold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 24;
        params.color = Color.WHITE;
        this.font24 = generator.generateFont(params);
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
        weaponTexture.dispose();
        leftTexture.dispose();
        rightTexture.dispose();
        shootTexture.dispose();
        jumpTexture.dispose();
        font24.dispose();
    }
}