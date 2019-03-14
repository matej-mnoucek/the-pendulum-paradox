package com.pendulumparadox.view.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/****HUD class. Displays overlaying labels with score, life count.****/

/**TODO:
 * In constructor: FitViewport need width and height coordinates = center of player carracter.
 * Add image to */



public class Hud {
    public Stage stage;
    private Viewport viewport;

    private Integer score;
    private Integer lifeCount;
    private Integer ammoCount;

    private Label scoreLabel;
    private Label scoreTextLabel;
    private Label lifeLabel;
    private Label lifeTextLabel;
    private Label ammoLabel;
    private Label ammoTextLabel;
    private Image weaponImg;

    public Hud(SpriteBatch sb){
        lifeCount = 3;
        score = 0;


        /***TODO: Set viewport coordinates: ***/
        viewport = new FitViewport(0,0, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        /***TODO: Cosmetic changes to make labels correct***/
        scoreLabel = new Label( String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        lifeLabel = new Label(String.format("%03d", lifeCount), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        ammoLabel = new Label(String.format("%03d", ammoCount), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        scoreTextLabel = new Label("SCORE: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        lifeTextLabel = new Label("LIFE: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        ammoTextLabel = new Label("AMMO: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        /***TODO: add correct weabon PNG***/
        weaponImg = new Image(new Texture("Weapon.png"));

        /***adding labels to table***/
        table.add(scoreTextLabel).expandX().padTop(10);
        table.add(lifeTextLabel).expandX().padTop(10);
        table.add(ammoTextLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(lifeLabel).expandX();
        table.add(ammoLabel).expandX();
        table.row();

        /***TODO:scale image correctly***/
        table.add(weaponImg).width(50).height(10);

        /***Add table to stage***/
        stage.addActor(table);
    }

}
