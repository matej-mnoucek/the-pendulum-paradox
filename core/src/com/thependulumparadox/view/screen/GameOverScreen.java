package com.thependulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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
import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;
import com.thependulumparadox.presenter.GamePresenter;

import javax.management.StandardEmitterMBean;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Game over UI
 */
public class GameOverScreen extends BaseScreen{

    private Label headLine;
    private Label score;

    TextButton btnNewGame;
    TextButton btnToHighscores;
    TextButton btnToMenu;

    public Event<EventArgs> btnNewGamePressed;
    private Skin skin;
    private BitmapFont font24;
    private Table headLineTable;
    private Table buttonTable;

    private Event<EventArgs> newGameEvent = new Event<>();
    private Event<EventArgs> highScoreEvent = new Event<>();
    private Event<EventArgs> menuEvent = new Event<>();

    public GameOverScreen()
    {
        // Set font and labelStyle
        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;

        // Declare skin
        this.skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        // Create table for buttons, place in center of stage, size root of table to stage
        buttonTable = new Table();
        buttonTable.center();
        buttonTable.setFillParent(true);
        buttonTable.setDebug(true);

        // Create table for headline
        headLineTable = new Table();
        headLineTable.top();
        headLineTable.setFillParent(true);

        // Create label for headline
        headLine = new Label("GAME OVER!", labelStyle);
        score = new Label("Score: 0", labelStyle);
        score.setFontScale(0.8f);

        btnNewGamePressed = new Event<EventArgs>();

        // Create button for starting new game
        btnNewGame = new TextButton("New Game", skin);
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
        // Create button for going to highscore screen
        btnToHighscores = new TextButton("High Score", skin);
        btnToHighscores.addListener(new ClickListener(){
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

        // Create button for going back to menu
        btnToMenu = new TextButton("Main Menu", skin);
        btnToMenu.addListener(new ClickListener(){
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

        // Add headline to headlineTable
        headLineTable.add(headLine).center().padTop(80);
        headLineTable.row();
        headLineTable.add(score).padTop(20);

        // Add buttons to buttonTable
        buttonTable.add(btnNewGame).center().size(300,60);
        buttonTable.row();
        buttonTable.add(btnToMenu).center().size(300,60).padTop(20);
        buttonTable.row();
        buttonTable.add(btnToHighscores).center().size(300,60).padTop(20);

        // Add tables to stage
        stage.addActor(buttonTable);
        stage.addActor(headLineTable);
    }

    public void setScore(int value)
    {
        score.setText("Score: " + value);
    }

    public Event<EventArgs> getNewGameEvent() {
        return newGameEvent;
    }

    public Event<EventArgs> getHighScoreEvent() {
        return highScoreEvent;
    }

    public Event<EventArgs> getMenuEvent() {
        return menuEvent;
    }

    public Stage getStage(){
        return this.stage;
    }


    // Initialize TrueTypeFont
    private void initFonts()
    {
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/freeagentbold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 40;
        params.color = Color.WHITE;
        this.font24 = generator.generateFont(params);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
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
        skin.dispose();
        font24.dispose();
    }
}
