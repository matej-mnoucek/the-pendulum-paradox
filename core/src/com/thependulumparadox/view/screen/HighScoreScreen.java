package com.thependulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.thependulumparadox.multiplayer.ISynchronization;
import com.thependulumparadox.presenter.GamePresenter;

import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;
import com.thependulumparadox.presenter.GamePresenter;

import java.util.ArrayList;

/**
 * High score UI
 */
public class HighScoreScreen extends BaseScreen
{
    private String[] names;
    private Integer[] score;

    private Skin skin;

    private TextButton btnBack;

    private Label headLine;
    private Label scoreLabel;
    private Label nameLabel;
    private Label placeLabel;
    private Label first;
    private Label firstName;
    private Label firstScore;
    private Label second;
    private Label secondName;
    private Label secondScore;
    private Label third;
    private Label thirdName;
    private Label thirdScore;
    private Label fourth;
    private Label fourthName;
    private Label fourthScore;
    private Label fifth;
    private Label fifthName;
    private Label fifthScore;
    private Label sixth;
    private Label sixthName;
    private Label sixthScore;
    private Label seventh;
    private Label seventhName;
    private Label seventhScore;
    private Label eighth;
    private Label eighthName;
    private Label eighthScore;
    private Label ninth;
    private Label ninthName;
    private Label ninthScore;
    private Label tenth;
    private Label tenthName;
    private Label tenthScore;

    private BitmapFont font24;

    private Event<EventArgs> menuEvent = new Event<>();


    public HighScoreScreen()
    {
        this.names = new String[10];
        this.score = new Integer[10];

        initFonts();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;

        this.skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        Table headLineTable = new Table();
        headLineTable.top();
        headLineTable.setFillParent(true);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Table btnTable = new Table();
        btnTable.center().bottom();
        btnTable.setFillParent(true);

        headLine = new Label("HIGHSCORES", labelStyle);
        placeLabel = new Label("Place", labelStyle);
        nameLabel = new Label("Name", labelStyle);
        scoreLabel = new Label("Score", labelStyle);
        first = new Label("1",labelStyle);
        firstName = new Label(names[0], labelStyle);
        firstScore = new Label(String.format("%06d",score[0]), labelStyle);
        second = new Label("2",labelStyle);
        secondName = new Label(names[0], labelStyle);
        secondScore = new Label(String.format("%06d",score[0]), labelStyle);
        third = new Label("3",labelStyle);
        thirdName = new Label(names[0], labelStyle);
        thirdScore = new Label(String.format("%06d",score[0]), labelStyle);
        fourth = new Label("4",labelStyle);
        fourthName = new Label(names[0], labelStyle);
        fourthScore = new Label(String.format("%06d",score[0]), labelStyle);
        fifth = new Label("5",labelStyle);
        fifthName = new Label(names[0], labelStyle);
        fifthScore = new Label(String.format("%06d",score[0]), labelStyle);
        sixth = new Label("6",labelStyle);
        sixthName = new Label(names[0], labelStyle);
        sixthScore = new Label(String.format("%06d",score[0]), labelStyle);
        seventh = new Label("7",labelStyle);
        seventhName = new Label(names[0], labelStyle);
        seventhScore = new Label(String.format("%06d",score[0]), labelStyle);
        eighth = new Label("8",labelStyle);
        eighthName = new Label(names[0], labelStyle);
        eighthScore = new Label(String.format("%06d",score[0]), labelStyle);
        ninth = new Label("9",labelStyle);
        ninthName = new Label(names[0], labelStyle);
        ninthScore = new Label(String.format("%06d",score[0]), labelStyle);
        tenth = new Label("10",labelStyle);
        tenthName = new Label(names[0], labelStyle);
        tenthScore = new Label(String.format("%06d",score[0]), labelStyle);

        btnBack = new TextButton("Main Menu", skin);
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

        headLineTable.add(headLine).expandX().padTop(20).padBottom(20);
        btnTable.add(btnBack).expandX().padBottom(20).size(300,60);
        table.add(placeLabel);
        table.add(nameLabel);
        table.add(scoreLabel);
        table.row();
        table.add(first).expandX();
        table.add(firstName).expandX();
        table.add(firstScore).expandX();
        table.row();
        table.add(second).expandX();
        table.add(secondName).expandX();
        table.add(secondScore).expandX();
        table.row();
        table.add(third).expandX();
        table.add(thirdName).expandX();
        table.add(thirdScore).expandX();
        table.row();
        table.add(fourth).expandX();
        table.add(fourthName).expandX();
        table.add(fourthScore).expandX();
        table.row();
        table.add(fifth).expandX();
        table.add(fifthName).expandX();
        table.add(fifthScore).expandX();
        table.row();
        table.add(sixth).expandX();
        table.add(sixthName).expandX();
        table.add(sixthScore).expandX();
        table.row();
        table.add(seventh).expandX();
        table.add(seventhName).expandX();
        table.add(seventhScore).expandX();
        table.row();
        table.add(eighth).expandX();
        table.add(eighthName).expandX();
        table.add(eighthScore).expandX();
        table.row();
        table.add(ninth).expandX();
        table.add(ninthName).expandX();
        table.add(ninthScore).expandX();
        table.row();
        table.add(tenth).expandX();
        table.add(tenthName).expandX();
        table.add(tenthScore).expandX();
        table.row();

        stage.addActor(table);
        stage.addActor(headLineTable);
        stage.addActor(btnTable);

        // Populate scores
        populateHighScoreList("");
    }

    public Event<EventArgs> getMenuEvent() {
        return menuEvent;
    }

    private void initFonts()
    {
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/freeagentbold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 24;
        params.color = Color.WHITE;
        this.font24 = generator.generateFont(params);
    }

    public Stage getStage(){
        return this.stage;
    }

    public void populateHighScoreList(String highscores)
    {
        if (highscores == "")
        {
            for(int i = 0; i < 10; i++)
            {
                score[i] = 0;
                names[i] = "----";
            }
        }
        else
        {
            String[] scores = highscores.split("!");
            for (int i = 0; i < scores.length; i++)
            {
                String[] temp = scores[i].split(":");
                score[i] = Integer.parseInt(temp[1].replace(".","0")
                        .replace(",","0").replaceAll("\\s+",""));
                names[i] = temp[0];
            }
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        stage.act(delta);
        stage.draw();

        firstName.setText(names[0]);
        secondName.setText(names[1]);
        thirdName.setText(names[2]);
        fourthName.setText(names[3]);
        fifthName.setText(names[4]);
        sixthName.setText(names[5]);
        seventhName.setText(names[6]);
        eighthName.setText(names[7]);
        ninthName.setText(names[8]);
        tenthName.setText(names[9]);

        firstScore.setText(score[0]);
        secondScore.setText(score[1]);
        thirdScore.setText(score[2]);
        fourthScore.setText(score[3]);
        fifthScore.setText(score[4]);
        sixthScore.setText(score[5]);
        seventhScore.setText(score[6]);
        eighthScore.setText(score[7]);
        ninthScore.setText(score[8]);
        tenthScore.setText(score[9]);
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
        skin.dispose();
    }
}
