package com.thependulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;

/**
 * Settings UI
 */
public class SettingsScreen extends BaseScreen {

    private Button btnSound;
    private TextButton btnMenu;
    private Label headLine;
    private BitmapFont font24;
    private Skin skin;
    private Table settingsTable;
    private Table headlineTable;
    private Table backTable;

    private Event<EventArgs> soundEvent = new Event<>();
    private Event<EventArgs> menuEvent = new Event<>();


    public SettingsScreen()
    {
        // Set font and labelstyle
        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this.font24;
        labelStyle.fontColor = Color.WHITE;

        this.skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        // Create Table
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
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                soundEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        btnMenu = new TextButton("Main Menu", skin);
        btnMenu.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        headlineTable.add(headLine).center().padTop(60);
        settingsTable.add(btnSound).center();
        backTable.add(btnMenu).center().size(300,60).padBottom(20);
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
        }
        else
        {
            btnSound.setChecked(false);
        }
    }

    public Stage getStage(){
        return this.stage;
    }


    @Override
    public void show() {

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
