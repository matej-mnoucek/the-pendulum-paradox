package com.thependulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class GameOverScreen extends Screen{

    TextButton btnNewGame;
    TextButton btnToHighscores;
    TextButton btnToMenu;

    public void create(){
        super.create();

        BitmapFont font = new BitmapFont();
        font.getData().scale(0.2f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.setDebug(true);

        TextButton btnNewGame = new TextButton("New Game", skin);
        btnNewGame.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnNewGamePressed();
            }
        });
        TextButton btnToHighscores = new TextButton("Highscores", skin);
        btnNewGame.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnToHighscoresPressed();
            }
        });
        TextButton btnToMenu = new TextButton("Main Menu", skin);
        btnNewGame.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnToMenuPressed();
            }
        });

        table.row().expandY().align(Align.center);
        table.add(btnNewGame).expandY().align(Align.center);
        table.row();
        table.add(btnToMenu).expandY().align(Align.center);
        table.row();
        table.add(btnToHighscores).expandY().align(Align.center);
        table.row().expandY().align(Align.center);

        stage.addActor(table);

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
