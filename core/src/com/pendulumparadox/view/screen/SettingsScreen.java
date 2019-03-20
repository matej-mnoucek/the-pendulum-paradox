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
import com.pendulumparadox.presenter.GamePresenter;

public class SettingsScreen extends Screen{

    private TextButton btnSound;
    private Label headLine;
    private BitmapFont font;
    private Skin skin;

    boolean soundOn;


    public void create(){
        super.create();

        this.soundOn = true;

        this.font = new BitmapFont();
        font.getData().scale(0.2f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this.font;
        labelStyle.fontColor = Color.WHITE;

        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table headlineTable = new Table();
        headlineTable.top();
        headlineTable.setFillParent(true);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        this.headLine = new Label("SETTINGS", labelStyle);
        this.btnSound = new TextButton("Sound: ON", skin);
        btnSound.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnSoundClicked();
            }
        });

        headlineTable.add(headLine).center().padTop(60);
        table.add(btnSound).center().size(300, 60);

        stage.addActor(table);
        stage.addActor(headlineTable);

    }

    private void btnSoundClicked(){
            btnSound.setText("Sound: OFF");
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
