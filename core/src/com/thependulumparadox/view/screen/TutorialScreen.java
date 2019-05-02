package com.thependulumparadox.view.screen;

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
import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;

/**
 * Tutorial UI
 */
public class TutorialScreen extends BaseScreen {

    private BitmapFont font24;
    private Table headLineTable;
    private Table btnTable;
    private Label headLine;
    private Label tutorialText;
    private TextButton btnBack;
    private Skin skin;
    private Event<EventArgs> menuEvent = new Event<>();

    public TutorialScreen()
    {
        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;

        this.skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        headLineTable = new Table();
        headLineTable.center().top();
        headLineTable.setFillParent(true);
        headLineTable.setDebug(false);

        btnTable = new Table();
        btnTable.bottom();
        btnTable.setFillParent(true);
        btnTable.setDebug(false);

        headLine = new Label("Tutorial", labelStyle);
        headLine.setFontScale(1f);

        String tutorial = "Player is controlled by UI buttons. The left part of the screen\n" +
                            "contains buttons for moving left and right and the right part\n" +
                            "has buttons for shooting and jumping.\n\n" +
                        "The basic goal is to stay alive as long as possible and get as high\n" +
                            "score as possible. One game round runs as long as all involved\n" +
                            "players are alive. Each player has three lives.\n\n" +
                        "Players get points for killing enemies (5 points) and collecting\n" +
                            "time machine parts (10 or 20 points).\n\n" +
                        "In the levels you can also find three different kinds of enhancements\n" +
                            "(lives, defense, damage).\n\n" +
                        "The game contains three different kinds of enemies\n" +
                            "(knight, ninja, ninja boy)";

        tutorialText = new Label(tutorial, labelStyle);
        tutorialText.setFontScale(0.6f);

        btnBack = new TextButton("Back", skin);
        btnBack.addListener(new ClickListener(){
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
        headLineTable.add(headLine).padTop(20);
        headLineTable.row();
        headLineTable.add(tutorialText).padTop(20);
        btnTable.add(btnBack).size(300, 60).padBottom(20);

        stage.addActor(headLineTable);
        stage.addActor(btnTable);
    }

    public Event<EventArgs> getMenuEvent() {
        return menuEvent;
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
    public void show() {

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
