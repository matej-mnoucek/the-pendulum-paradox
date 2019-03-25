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

import javax.management.StandardEmitterMBean;


public class GameOverScreen extends BaseScreen{

    private Label headLine;
    TextButton btnNewGame;
    TextButton btnToHighscores;
    TextButton btnToMenu;
    public Event<EventArgs> btnNewGamePressed;
    private Skin skin;
    private BitmapFont font24;
    private Table headLineTable;
    private Table buttonTable;

    public GameOverScreen()
    {
        super();

        //set font and labelStyle
        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;

        // declare skin
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

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
        headLine = new Label("GAME OVER!", labelStyle);

        btnNewGamePressed = new Event<EventArgs>();
        //create button for starting new game
        btnNewGame = new TextButton("New Game", skin);
        btnNewGame.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnNewGamePressed.invoke(null);
            }
        });
        //create button for going to highscore screen
        btnToHighscores = new TextButton("Highscores", skin);
        btnToHighscores.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnToHighscoresPressed();
            }
        });

        //create button for going back to menu
        btnToMenu = new TextButton("Main Menu", skin);
        btnToMenu.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnToMenuPressed();
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

    public Stage getStage(){
        return this.stage;
    }

    public void btnNewGamePressed(){
        btnNewGame.setText("Yay!");
    }
    public void btnToHighscoresPressed(){
        btnToHighscores.setText("YAY!");
    }
    public void btnToMenuPressed(){
        btnToMenu.setText("YAY!");
    }


    //initialize TrueTypeFont
    private void initFonts(){
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
    public void resize(int width, int height)
    {

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
