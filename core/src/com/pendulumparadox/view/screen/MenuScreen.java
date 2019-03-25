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

public class MenuScreen extends BaseScreen
{

    private TextButton btnNewGame;
    private TextButton btnTutorial;
    private TextButton btnHighScore;
    private TextButton btnSettings;
    private TextButton btnGoogleLogin;
    private Skin skin;

    private BitmapFont font24;

    // Setup the whole layout here
    public MenuScreen()
    {
        super();

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        //TODO: Fit buttons with final size of screen!

        this.btnNewGame = new TextButton("New Game", skin);
        btnNewGame.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnNewGameClicked();
            }
        });

        this.btnTutorial = new TextButton("Tutorial", skin);
        btnTutorial.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnTutorialClicked();
            }
        });

        this.btnHighScore = new TextButton("HighScore", skin);
        btnHighScore.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnHighScoreClicked();
            }
        });

        this.btnSettings = new TextButton("Settings", skin);
        btnSettings.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                btnSettingsClicked();
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


    private void initFonts(){
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/freeagentbold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 24;
        params.color = Color.WHITE;
        this.font24 = generator.generateFont(params);
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
