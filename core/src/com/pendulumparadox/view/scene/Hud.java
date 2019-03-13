package com.pendulumparadox.view.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/****HUD class. Displays overlaying labels with score, life count.****/

/**TODO:
 * In constructor: FitViewport need width and height coordinates = center of player carracter*/


public class Hud {
    public Stage stage;
    private Viewport viewport;

    private Integer score;
    private Integer lifeCount;

    private Label scoreLabel;
    private Label lifeLabel;

    public Hud(SpriteBatch sb){
        lifeCount = 3;
        score = 0;

        viewport = new FitViewport(0,0, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
    }

}
