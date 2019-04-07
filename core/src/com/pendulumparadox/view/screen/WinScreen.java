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

public class WinScreen extends BaseScreen {

    private Label headLine;
    TextButton btnNewGame;
    TextButton btnToHighscores;
    TextButton btnToMenu;

    private Skin skin;
    private BitmapFont font60;
    private Table headLineTable;
    private Table buttonTable;


    private Event<EventArgs> newGameEvent = new Event<>();
    private Event<EventArgs> highScoreEvent = new Event<>();
    private Event<EventArgs> menuEvent = new Event<>();


    // Constructor
    public WinScreen()
    {
        //super(camera);

        //set font and labelStyle
        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font60;
        labelStyle.fontColor = Color.WHITE;

        // declare skin
        this.skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        //create table for buttons. place in center of stage. size root of table to stage
        buttonTable = new Table();
        buttonTable.center();
        buttonTable.setFillParent(true);
        buttonTable.setDebug(true);

        //create table for headline.
        headLineTable = new Table();
        headLineTable.top();
        headLineTable.setFillParent(true);

        //create label for headline
        headLine = new Label("YOU WIN!", labelStyle);

        newGameEvent = new Event<EventArgs>();
        //create button for starting new game
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
        //create button for going to highscore screen
        btnToHighscores = new TextButton("Highscores", skin);
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

        //create button for going back to menu
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

        //add headline to headlineTable
        headLineTable.add(headLine).center().padTop(60);

        //add buttons to buttonTable
        buttonTable.add(btnNewGame).center().size(300,60);
        buttonTable.row();
        buttonTable.add(btnToMenu).center().size(300,60).padTop(20);
        buttonTable.row();
        buttonTable.add(btnToHighscores).center().size(300,60).padTop(20);

        //add tables to stage
        stage.addActor(buttonTable);
        stage.addActor(headLineTable);

    }

    //initialize TrueTypeFont
    private void initFonts(){
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/freeagentbold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 60;
        params.color = Color.WHITE;
        this.font60 = generator.generateFont(params);
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


    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

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

    }
}
