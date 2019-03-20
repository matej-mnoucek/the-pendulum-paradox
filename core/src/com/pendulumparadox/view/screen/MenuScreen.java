package com.pendulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pendulumparadox.presenter.GamePresenter;

public class MenuScreen extends Screen
{

    private TextButton btnNewGame;
    private TextButton btnTutorial;
    private TextButton btnHighScore;
    private TextButton btnSettings;
    private TextButton btnGoogleLogin;


    // Setup the whole layout here
    public void create()
    {
        super.create();


        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = GamePresenter.font24;

        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        //TODO: Fit buttons with final size of screen!

        TextButton btnNewGame = new TextButton("New Game", skin);
        btnNewGame.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnNewGameClicked();
            }
        });

        TextButton btnTutorial = new TextButton("Tutorial", skin);
        btnTutorial.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnTutorialClicked();
            }
        });

        TextButton btnHighScore = new TextButton("HighScore", skin);
        btnHighScore.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnHighScoreClicked();
            }
        });

        TextButton btnSettings = new TextButton("Settings", skin);
        btnSettings.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnSettingsClicked();
            }
        });

        TextButton btnGoogleLogin = new TextButton("Log In to Google", skin);
        btnGoogleLogin.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnGoogleLoginClicked();
            }
        });

        table.add(btnNewGame).center().size(300,60).padTop(GamePresenter.V_HEIGHT / 5);
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

    public void btnTutorialClicked(){
        btnTutorial.setText("YAY!");
    }

    public void btnNewGameClicked(){
        btnNewGame.setText("YAY!");
    }

    public void btnHighScoreClicked(){
        btnHighScore.setText("YAY!");
    }

    public void btnSettingsClicked(){
        btnSettings.setText("YAY!");
    }
    public void btnGoogleLoginClicked(){
        btnGoogleLogin.setText("YAY!");
    }


    private void initFonts(){
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/freeagentboldi.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 24;
        params.color = Color.WHITE;
        this.font24 = generator.generateFont(params);

    }
    @Override
    public void resize(int width, int height) {

    }

    public void render ()
    {
        float delta = Gdx.graphics.getDeltaTime();
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        font24.dispose();

    }
}
