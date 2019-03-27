package com.pendulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pendulumparadox.observer.Event;
import com.pendulumparadox.observer.EventArgs;

public class SettingsScreen extends BaseScreen{

    private Button btnSound;
    private TextButton btnMenu;
    private Label headLine;
    private BitmapFont font24;
    private Skin skin;
    private Table settingsTable;
    private Table headlineTable;
    private Table backTable;

    private boolean soundOn;

    private Event<EventArgs> soundEvent = new Event<>();
    private Event<EventArgs> menuEvent = new Event<>();


    public SettingsScreen()
    {

        //set font and labelstyle
        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this.font24;
        labelStyle.fontColor = Color.WHITE;


        this.skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        //create Table
        headlineTable = new Table();
        headlineTable.top();
        headlineTable.setFillParent(true);

        settingsTable = new Table();
        settingsTable.center();
        settingsTable.setFillParent(true);

        backTable = new Table();
        backTable.bottom();
        backTable.setFillParent(true);


        this.headLine = new Label("SETTINGS", labelStyle);
        this.btnSound = new Button(skin, "sound");
        this.btnSound.setChecked(true);
        btnSound.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                if(btnSound.isChecked()){
                    btnSound.setChecked(false);
                } else {
                    btnSound.setChecked(true);
                }
                soundEvent.invoke(null);
            }
        });
        btnMenu = new TextButton("Menu", skin);
        btnMenu.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                menuEvent.invoke(null);
            }
        });

        headlineTable.add(headLine).center().padTop(60);
        settingsTable.add(btnSound).center();
        backTable.add(btnMenu).center().size(300,60);
        stage.addActor(settingsTable);
        stage.addActor(headlineTable);
        stage.addActor(backTable);
    }

    public Event<EventArgs> getSoundEvent() {
        return soundEvent;
    }

    public Event<EventArgs> getMenuEvent(){
        return menuEvent;
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

    public Stage getStage(){
        return this.stage;
    }


    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

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
        font24.dispose();
        skin.dispose();
    }
}
