package com.pendulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class GameOverScreen extends Screen{

    private Label headLine;
    TextButton btnNewGame;
    TextButton btnToHighscores;
    TextButton btnToMenu;

    private Skin skin;

    public void create(){
        super.create();

        BitmapFont font = new BitmapFont();
        font.getData().scale(0.2f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.setDebug(true);

        Table headLineTable = new Table();
        headLineTable.top();
        headLineTable.setFillParent(true);

        headLine = new Label("GAME OVER!", labelStyle);

        btnNewGame = new TextButton("New Game", skin);
        btnNewGame.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnNewGamePressed();
            }
        });
        btnToHighscores = new TextButton("Highscores", skin);
        btnToHighscores.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnToHighscoresPressed();
            }
        });
        btnToMenu = new TextButton("Main Menu", skin);
        btnToMenu.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnToMenuPressed();
            }
        });

        headLineTable.add(headLine).center().padTop(60);
        table.add(btnNewGame).center().size(300,60);
        table.row();
        table.add(btnToMenu).center().size(300,60).padTop(20);
        table.row();
        table.add(btnToHighscores).center().size(300,60).padTop(20);

        stage.addActor(table);
        stage.addActor(headLineTable);

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

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
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
    public void dispose() {

    }
}
