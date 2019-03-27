package com.pendulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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



public class InGameScreen extends BaseScreen
{
    private Label lifeLabel;
    private Label lifeCount;
    private Label ammoLabel;
    private Label ammoCount;
    private Texture weaponTexture;
    private Image weapon;
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

    private boolean shooting;
    private float shootTimer;

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

        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;

        this.skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

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

        //HUD Texture for weapon
        weaponTexture = new Texture("ak47.png");

        //image for weapon button, move button action button
        weapon = new Image(weaponTexture);
        btnLeft = new Button(skin, "left");
        btnRight = new Button(skin, "right");
        btnJump = new Touchpad(3, skin, "default");
        btnJump.setColor(1, 0,0,1);
        btnShoot = new Touchpad(3, skin, "default");
        btnShoot.setColor(0,0,1,1);


        //add clicklistneres for buttons
        btnLeft.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // This is just invoking the already defined event
                // We are not passing any data == null for EventArgs argument
                leftEvent.invoke(null);
            }
        });
        btnRight.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightEvent.invoke(null);
            }
        });
        btnJump.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jumpEvent.invoke(null);
            }
        });
        btnShoot.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int Button) {
                shooting = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                shooting = false;
                shootTimer = 0;
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

    @Override
    public void render(float delta) {
        if(shooting) {
            shootTimer += delta;
            if(shootTimer > 0.1f){
                decrementAmmo();
                shootTimer = 0;
            }
        }
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