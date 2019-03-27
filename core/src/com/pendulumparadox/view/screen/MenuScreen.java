package com.pendulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pendulumparadox.observer.Event;
import com.pendulumparadox.observer.EventArgs;
import com.pendulumparadox.presenter.GamePresenter;

public class MenuScreen extends BaseScreen
{

    private TextButton btnNewGame;
    private TextButton btnTutorial;
    private TextButton btnHighScore;
    private TextButton btnSettings;
    private TextButton btnGoogleLogin;
    private Skin skin;

    private BitmapFont font24;

    Event<EventArgs> newGameEvent = new Event<>();
    Event<EventArgs> settingsEvent = new Event<>();
    Event<EventArgs> highScoreEvent = new Event<>();
    Event<EventArgs> tutorialEvent = new Event<>();

    // Setup the whole layout here
    public MenuScreen()
    {
        super();

        this.skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;


        Table table = new Table();
        table.center();
        table.setFillParent(true);

        //TODO: Fit buttons with final size of screen!

        this.btnNewGame = new TextButton("New Game", skin);
        btnNewGame.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                newGameEvent.invoke(null);
            }
        });

        this.btnTutorial = new TextButton("Tutorial", skin);
        btnTutorial.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                tutorialEvent.invoke(null);
            }
        });

        this.btnHighScore = new TextButton("HighScore", skin);
        btnHighScore.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                highScoreEvent.invoke(null);
            }
        });

        this.btnSettings = new TextButton("Settings", skin);
        btnSettings.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                settingsEvent.invoke(null);
            }
        });

        this.btnGoogleLogin = new TextButton("Log In to Google", skin);
        btnGoogleLogin.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnGoogleLoginClicked();
            }
        });

        table.add(btnNewGame).center().size(300,60).padTop(GamePresenter.V_HEIGHT / 5f);
        table.row();
        table.add(btnHighScore).center().size(300,60).padTop(20);
        table.row();
        table.add(btnSettings).center().size(300,60).padTop(20);
        table.row();
        table.add(btnTutorial).center().size(300,60).padTop(20);
        table.row();
        table.add(btnGoogleLogin).center().size(300,60).padTop(20);

        stage.addActor(table);
    }

    public Stage getStage(){
        return this.stage;
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

    public Event<EventArgs> getNewGameEvent() {
        return newGameEvent;
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

    public void btnGoogleLoginClicked(){
        btnGoogleLogin.setText("YAY!");
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

    }
}
