package com.thependulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;

/**
 * InGame UI aka HUD
 */
public class InGameScreen extends BaseScreen
{
    private Label lifeLabel;
    private Label lifeCount;
    private Label scoreLabel;
    private Label scoreCount;

    private Image weapon;
    private Button btnSound;
    private Button btnMenu;
    private Button btnLeft;
    private Button btnRight;
    private Touchpad btnJump;
    private Touchpad btnShoot;
    private BitmapFont font24;
    private Skin skin;

    private int lifeCounter;
    private int scoreCounter;

    private Table hudTable;
    private Table moveBtnTable;
    private Table actionBtnTable;


    // Button events
    // EventArgs == no parameters sent with the message
    // Subclassing EventArgs == a way how to pass custom data
    Event<EventArgs> shootEvent = new Event<>();
    Event<EventArgs> stopShootEvent = new Event<>();
    Event<EventArgs> jumpEvent = new Event<>();
    Event<EventArgs> leftEvent = new Event<>();
    Event<EventArgs> stopLeftEvent = new Event<>();
    Event<EventArgs> rightEvent = new Event<>();
    Event<EventArgs> stopRightEvent = new Event<>();
    Event<EventArgs> soundEvent = new Event<>();
    Event<EventArgs> MenuEvent = new Event<>();

    private boolean soundOn;


    public InGameScreen()
    {
        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;

        this.skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        this.soundOn = true;

        // Table for holding HUD objects
        hudTable = new Table();
        hudTable.top();
        hudTable.setFillParent(true);

        // Table for holding move buttons, left bottom side of screen
        moveBtnTable = new Table();
        moveBtnTable.bottom().left();
        moveBtnTable.setFillParent(true);
        moveBtnTable.setDebug(false);

        // Table for holding shoot and jump buttons, right bottom side of screen
        actionBtnTable = new Table();
        actionBtnTable.bottom().right();
        actionBtnTable.setFillParent(true);
        actionBtnTable.setDebug(false);


        lifeCounter = 0;
        scoreCounter = 0;

        // Create labels for lives and ammo
        lifeLabel = new Label("LIVES: ", labelStyle);
        lifeCount = new Label(String.format("%01d", lifeCounter),labelStyle);
        scoreLabel = new Label("SCORE:", labelStyle);
        scoreCount = new Label(String.format("%03d", scoreCounter), labelStyle);

        btnSound = new Button(skin, "sound");
        btnSound.setChecked(true);

        btnMenu = new TextButton("Main menu", skin);

        btnLeft = new Button(skin, "left");
        btnLeft.setColor(1,1,1,0.8f);
        btnRight = new Button(skin, "right");
        btnRight.setColor(1,1,1,0.8f);
        btnJump = new Touchpad(3, skin, "default");
        btnJump.setColor(0,0,1,0.8f);
        btnShoot = new Touchpad(3, skin, "default");
        btnShoot.setColor(1,0,0,0.8f);


        // Add click listeners for buttons
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
        btnMenu.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MenuEvent.invoke(null);
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
                stopLeftEvent.invoke(null);
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
                stopRightEvent.invoke(null);
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
                stopShootEvent.invoke(null);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        // Add score and lives to HUD table
        hudTable.add(scoreLabel).expandX().align(Align.left).padLeft(20).padTop(10);
        hudTable.add(btnMenu).expandX().align(Align.center).padTop(20).height(50).width(150);
        hudTable.add(lifeLabel).expandX().align(Align.right).padRight(20).padTop(10);
        hudTable.row();
        hudTable.add(scoreCount).align(Align.left).padLeft(30);
        hudTable.add(new Label("", labelStyle));
        hudTable.add(lifeCount).align(Align.right).padRight(40);
        hudTable.row();
        hudTable.add(weapon).width(100).height(50).align(Align.left);
        hudTable.add(new Label("", labelStyle));
        hudTable.add(btnSound).align(Align.right).padRight(20).padTop(20).width(60).height(60);


        // Add move buttons to movebtnTable
        moveBtnTable.add(btnLeft).width(120).height(120).padLeft(40).padBottom(15);
        moveBtnTable.add(btnRight).width(120).height(120).padBottom(40).padLeft(20);

        // Add action buttons to actionBtnTable
        actionBtnTable.add(btnJump).width(120).height(120).padBottom(40).padRight(20);
        actionBtnTable.add(btnShoot).width(120).height(120).padRight(30).padBottom(15);

        // Add tables to Stage
        stage.addActor(hudTable);
        stage.addActor(moveBtnTable);
        stage.addActor(actionBtnTable);
    }

    private void initFonts()
    {
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/freeagentbold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 24;
        params.color = Color.WHITE;
        this.font24 = generator.generateFont(params);
    }

    public void setSound(boolean on)
    {
        if(on)
        {
            btnSound.setChecked(true);
            this.soundOn = true;
        }
        else
        {
            btnSound.setChecked(false);
            this.soundOn = false;
        }
    }

    public void setLives(int lives)
    {
        lifeCounter = lives;
    }

    public void setScore(int score)
    {
        scoreCounter = score;
    }

    @Override
    public void show() {

    }

    public Stage getStage(){
        return this.stage;
    }

    public Event<EventArgs> getShootEvent() { return shootEvent; }

    public Event<EventArgs> getStopShootEvent() { return stopShootEvent; }

    public Event<EventArgs> getJumpEvent() {
        return jumpEvent;
    }

    public Event<EventArgs> getLeftEvent() {
        return leftEvent;
    }

    public Event<EventArgs> getStopLeftEvent() {
        return stopLeftEvent;
    }

    public Event<EventArgs> getRightEvent() {
        return rightEvent;
    }

    public Event<EventArgs> getStopRightEvent() {
        return stopRightEvent;
    }

    public Event<EventArgs> getSoundEvent(){ return soundEvent; }

    public Event<EventArgs> getMenuEvent(){ return MenuEvent; }


    @Override
    public void render(float delta)
    {
        super.render(delta);
        stage.act(delta);
        stage.draw();

        // Update lives and score
        lifeCount.setText(lifeCounter);
        scoreCount.setText(scoreCounter);
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
        font24.dispose();
        skin.dispose();
    }
}