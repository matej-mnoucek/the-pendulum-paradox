package com.pendulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.pendulumparadox.observer.Event;
import com.pendulumparadox.observer.EventArgs;
import com.pendulumparadox.presenter.GamePresenter;


public class InGameScreen extends BaseScreen
{
    private Label lifeLabel;
    private Label lifeCount;
    private Label ammoLabel;
    private Label ammoCount;
    private Texture weaponTexture;
    private Image weapon;
    private Button btnSound;
    private Button btnLeft;
    private Button btnRight;
    private Touchpad btnJump;
    private Touchpad btnShoot;
    private BitmapFont font24;
    private Skin skin;

    private int lifeCounter;
    private int ammoCounter;

    private Table hudTable;
    private Table moveBtnTable;
    private Table actionBtnTable;

    private float shootTimer;

    // Button events
    // EventArgs == no parameters sent with the message
    // Subclassing EventArgs == a way how to pass custom data
    Event<EventArgs> shootEvent = new Event<>();
    Event<EventArgs> jumpEvent = new Event<>();
    Event<EventArgs> leftEvent = new Event<>();
    Event<EventArgs> rightEvent = new Event<>();
    Event<EventArgs> soundEvent = new Event<>();

    private boolean soundOn;


    public InGameScreen()
    {

        //super(camera);

        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;

        this.skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        this.soundOn = true;

        //table for holding HUD objects
        hudTable = new Table();
        hudTable.top();
        hudTable.setFillParent(true);

        //table for holding move buttons. left bottom side of screen
        moveBtnTable = new Table();
        moveBtnTable.bottom().left();
        moveBtnTable.setFillParent(true);
        moveBtnTable.setDebug(false);

        //table for holding shoot and jump buttons. Right bottom side of screen
        actionBtnTable = new Table();
        actionBtnTable.bottom().right();
        actionBtnTable.setFillParent(true);
        actionBtnTable.setDebug(false);


        lifeCounter = 3;
        ammoCounter = 100;

        //create labels for lives and ammo
        lifeLabel = new Label("LIVES: ", labelStyle);
        lifeCount = new Label(String.format("%01d", lifeCounter),labelStyle);
        ammoLabel = new Label("Ammo:", labelStyle);
        ammoCount = new Label(String.format("%03d", ammoCounter), labelStyle);


        //HUD Texture for weapon, move buttons and action buttons
        weaponTexture = new Texture("sprites/weapons/ak47.png");


        //image for weapon button, move button action button
        weapon = new Image(weaponTexture);
        btnSound = new Button(skin, "sound");
        btnSound.setChecked(true);

        btnLeft = new Button(skin, "left");
        btnRight = new Button(skin, "right");
        btnJump = new Touchpad(3, skin, "default");
        btnJump.setColor(1, 0,0,1);
        btnShoot = new Touchpad(3, skin, "default");
        btnShoot.setColor(0,0,1,1);


        //add clicklistneres for buttons
        btnSound.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                soundEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        btnLeft.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        btnRight.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        btnJump.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jumpEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        btnShoot.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                shootEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                shootEvent.invoke(null);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        //add ammo, lives and weapon to HUD table
        hudTable.add(ammoLabel).expandX().align(Align.left);
        hudTable.add(lifeLabel).expandX().align(Align.right);
        hudTable.row();
        hudTable.add(ammoCount).align(Align.left).padLeft(10);
        hudTable.add(lifeCount).align(Align.right).padRight(20);
        hudTable.row();
        hudTable.add(weapon).width(100).height(50).align(Align.left);
        hudTable.add(btnSound).align(Align.right).padRight(20);

        //add move buttons to movebtnTable
        moveBtnTable.add(btnLeft).width(60).height(60).padLeft(40).padBottom(15);
        moveBtnTable.add(btnRight).width(60).height(60).padBottom(40).padLeft(20);

        //add action buttons to actionBtnTable
        actionBtnTable.add(btnShoot).width(60).height(60).padBottom(40).padRight(20);
        actionBtnTable.add(btnJump).width(60).height(60).padRight(30).padBottom(15);

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

    public void setSoundOn(boolean isSoundOn){
        if(isSoundOn){
            btnSound.setChecked(true);
            this.soundOn = true;
        } else{
            btnSound.setChecked(false);
            this.soundOn = false;
        }
    }

    @Override
    public void show() {

    }


    @Override
    public void resize(int width, int height) {

    }

    public Stage getStage(){
        return this.stage;
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

    public Event<EventArgs> getSoundEvent(){ return soundEvent; }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
    }

    public void decrementAmmo(){
        this.ammoCounter -= 1;
        this.ammoCount.setText(String.format("%03d", ammoCounter));
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
        weaponTexture.dispose();
        font24.dispose();
        skin.dispose();
    }
}