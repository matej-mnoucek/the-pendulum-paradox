package com.pendulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pendulumparadox.presenter.GamePresenter;

public class SettingsScreen extends BaseScreen{

    private TextButton btnSound;
    private Label headLine;
    private BitmapFont font24;
    private Skin skin;
    private Table table;
    private Table headlineTable;

    boolean soundOn;


    public SettingsScreen()
    {
        super();

        //set font and labelstyle
        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this.font24;
        labelStyle.fontColor = Color.WHITE;

        //set input processor
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        //create Table
        headlineTable = new Table();
        headlineTable.top();
        headlineTable.setFillParent(true);

        table = new Table();
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

    private void initFonts(){
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/freeagentbold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 24;
        params.color = Color.WHITE;
        this.font24 = generator.generateFont(params);
    }

    private void btnSoundClicked(){
            btnSound.setText("Sound: OFF");
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
