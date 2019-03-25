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
import com.badlogic.gdx.utils.Align;
import com.pendulumparadox.presenter.GamePresenter;




public class HighScoreScreen extends Screen
{
    private String[] names;
    private Integer[] score;

    private Skin skin;

    private TextButton btnBack;

    private Label scoreLabel;
    private Label nameLabel;
    private Label placeLabel;
    private Label headLine;
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

    private Table highScoreTable;
    private Table headLineTable;
    private Table buttonTable;

    private BitmapFont font24;

    @Override
    public void create()
    {
        super.create();

        this.names = new String[10];
        this.score = new Integer[10];

        //populate highscore table upon opening screen
        populateHighscoreList();

        //create font and set labelstyle and labelstyle color
        initFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        labelStyle.fontColor = Color.WHITE;

        //set input processor
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        //create table for headline
        headLineTable = new Table();
        headLineTable.top().center();
        headLineTable.setFillParent(true);

        //create table for high score
        highScoreTable = new Table();
        highScoreTable.center();
        highScoreTable.setFillParent(true);

        //create button Table
        buttonTable = new Table();
        buttonTable.center().bottom();
        buttonTable.setFillParent(true);

        //add labels to highscoreTable and headLineTable
        headLine = new Label("HIGHSCORES", labelStyle);
        placeLabel = new Label("Place", labelStyle);
        nameLabel = new Label("Name", labelStyle);
        scoreLabel = new Label("Score", labelStyle);
        first = new Label("1",labelStyle);
        firstName = new Label(names[0], labelStyle);
        firstScore = new Label(String.format("%06d",score[0]), labelStyle);
        second = new Label("2",labelStyle);
        secondName = new Label(names[1], labelStyle);
        secondScore = new Label(String.format("%06d",score[1]), labelStyle);
        third = new Label("3",labelStyle);
        thirdName = new Label(names[2], labelStyle);
        thirdScore = new Label(String.format("%06d",score[2]), labelStyle);
        fourth = new Label("4",labelStyle);
        fourthName = new Label(names[3], labelStyle);
        fourthScore = new Label(String.format("%06d",score[3]), labelStyle);
        fifth = new Label("5",labelStyle);
        fifthName = new Label(names[4], labelStyle);
        fifthScore = new Label(String.format("%06d",score[4]), labelStyle);
        sixth = new Label("6",labelStyle);
        sixthName = new Label(names[5], labelStyle);
        sixthScore = new Label(String.format("%06d",score[5]), labelStyle);
        seventh = new Label("7",labelStyle);
        seventhName = new Label(names[6], labelStyle);
        seventhScore = new Label(String.format("%06d",score[6]), labelStyle);
        eighth = new Label("8",labelStyle);
        eighthName = new Label(names[7], labelStyle);
        eighthScore = new Label(String.format("%06d",score[7]), labelStyle);
        ninth = new Label("9",labelStyle);
        ninthName = new Label(names[8], labelStyle);
        ninthScore = new Label(String.format("%06d",score[8]), labelStyle);
        tenth = new Label("10",labelStyle);
        tenthName = new Label(names[9], labelStyle);
        tenthScore = new Label(String.format("%06d",score[9]), labelStyle);


        //put headline label in headline table
        headLineTable.add(headLine).expandX().padTop(20).padBottom(20);

        //put button in button table
        buttonTable.add(btnBack).expandX().padBottom(20).size(300,60);

        //pubt high score labels in high score table
        highScoreTable.add(placeLabel).expandX();
        highScoreTable.add(nameLabel).expandX();
        highScoreTable.add(scoreLabel).expandX();
        highScoreTable.row();
        highScoreTable.add(first).expandX();
        highScoreTable.add(firstName).expandX();
        highScoreTable.add(firstScore).expandX();
        highScoreTable.row();
        highScoreTable.add(second).expandX();
        highScoreTable.add(secondName).expandX();
        highScoreTable.add(secondScore).expandX();
        highScoreTable.row();
        highScoreTable.add(third).expandX();
        highScoreTable.add(thirdName).expandX();
        highScoreTable.add(thirdScore).expandX();
        highScoreTable.row();
        highScoreTable.add(fourth).expandX();
        highScoreTable.add(fourthName).expandX();
        highScoreTable.add(fourthScore).expandX();
        highScoreTable.row();
        highScoreTable.add(fifth).expandX();
        highScoreTable.add(fifthName).expandX();
        highScoreTable.add(fifthScore).expandX();
        highScoreTable.row();
        highScoreTable.add(sixth).expandX();
        highScoreTable.add(sixthName).expandX();
        highScoreTable.add(sixthScore).expandX();
        highScoreTable.row();
        highScoreTable.add(seventh).expandX();
        highScoreTable.add(seventhName).expandX();
        highScoreTable.add(seventhScore).expandX();
        highScoreTable.row();
        highScoreTable.add(eighth).expandX();
        highScoreTable.add(eighthName).expandX();
        highScoreTable.add(eighthScore).expandX();
        highScoreTable.row();
        highScoreTable.add(ninth).expandX();
        highScoreTable.add(ninthName).expandX();
        highScoreTable.add(ninthScore).expandX();
        highScoreTable.row();
        highScoreTable.add(tenth).expandX();
        highScoreTable.add(tenthName).expandX();
        highScoreTable.add(tenthScore).expandX();
        highScoreTable.row();

        stage.addActor(highScoreTable);
        stage.addActor(headLineTable);
        stage.addActor(btnBack);

    }

    //initialize TrueTypeFont
    private void initFont(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/freeagentbold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 24;
        params.color = Color.WHITE;
        font24 = generator.generateFont(params);
    }

    //method used to populate highscorelist with correct scores and names
    private void populateHighscoreList(){
        for(int i = 0; i<10; i++){
            score[i] = 0;
            names[i] = "------";
        }
    }

    public void btnBackClicked(){
        btnBack.setText("yay!");
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
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
