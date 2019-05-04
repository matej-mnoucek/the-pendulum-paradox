package com.thependulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.thependulumparadox.presenter.GamePresenter;
import com.thependulumparadox.multiplayer.ISynchronization;
import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;
import com.thependulumparadox.presenter.GamePresenter;

/**
 * Menu UI
 */
public class MenuScreen extends BaseScreen
{
    private TextButton btnNewGame;
    private TextButton btnMultiPlayerGame;
    private TextButton btnTutorial;
    private TextButton btnHighScore;
    private TextButton btnSettings;
    private TextButton btnGoogleLogin;
    private Skin skin;
    private Label loggedIn;

    private BitmapFont font24;

    Event<EventArgs> newMultiPlayerEvent = new Event<>();
    Event<EventArgs> newGameEvent = new Event<>();
    Event<EventArgs> settingsEvent = new Event<>();
    Event<EventArgs> highScoreEvent = new Event<>();
    Event<EventArgs> tutorialEvent = new Event<>();
    Event<EventArgs> googleLoginEvent = new Event<>();


    // Setup the whole layout here
    public MenuScreen()
    {
        this.skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;

        loggedIn = new Label("", labelStyle);
        loggedIn.setAlignment(Align.center);
        setIsLoggedIn(false);

        Table table = new Table();
        table.center();
        table.setFillParent(true);


        this.btnNewGame = new TextButton("New Game", skin);
        btnNewGame.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                newGameEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        this.btnMultiPlayerGame = new TextButton("New Multiplayer Game", skin);
        btnMultiPlayerGame.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                newMultiPlayerEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        this.btnTutorial = new TextButton("Tutorial", skin);
        btnTutorial.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                tutorialEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        this.btnHighScore = new TextButton("High Score", skin);
        btnHighScore.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                highScoreEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        this.btnSettings = new TextButton("Settings", skin);
        btnSettings.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                settingsEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        this.btnGoogleLogin = new TextButton("Log In to Google", skin);
        btnGoogleLogin.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                googleLoginEvent.invoke(null);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Label gameName = new Label("THE PENDULUM PARADOX", labelStyle);
        gameName.setFontScale(1.4f);

        table.add(gameName);
        table.row();
        table.add(btnNewGame).center().size(300,55).padTop(30);
        table.row();
        table.add(btnMultiPlayerGame).center().size(300,55).padTop(20);
        table.row();
        table.add(btnHighScore).center().size(300,55).padTop(20);
        table.row();
        table.add(btnSettings).center().size(300,55).padTop(20);
        table.row();
        table.add(btnTutorial).center().size(300,55).padTop(20);
        table.row();
        table.add(btnGoogleLogin).center().size(300,55).padTop(20);
        table.row();
        table.add(loggedIn).center().padTop(30);

        stage.addActor(table);
    }

    public Stage getStage(){
        return this.stage;
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

    public Event<EventArgs> getNewGameEvent() {
        return newGameEvent;
    }

    public Event<EventArgs> getMultiPlayerEvent() {
        return newMultiPlayerEvent;
    }

    public Event<EventArgs> getSettingsEvent() {
        return settingsEvent;
    }

    public Event<EventArgs> getHighScoreEvent() {
        return highScoreEvent;
    }

    public Event<EventArgs> getTutorialEvent() {
        return tutorialEvent;
    }

    public Event<EventArgs> getGoogleLoginEvent(){ return googleLoginEvent; }


    public void setIsLoggedIn(boolean isLoggedIn)
    {
        if (isLoggedIn)
        {
            loggedIn.setText("Ready to play multiplayer");
        }
        else
        {
            loggedIn.setText("To play multiplayer\nlog in first");
        }
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
