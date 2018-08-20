package com.brox.livewallpaper.alien_blood;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class LWSpaceScreen implements Screen {

    final LWSpace main;
    Texture image;
    ShaderProgram shaderProgram;

    float time = 0;
    float timeAttr = 1;

    float[] touches;
    int count = 0;

    Vector3 colors;

    TextureAtlas atlas;
    Skin skin;
    Stage stage;
    public static Table container;
    Dialog popup;

    public LWSpaceScreen(final LWSpace main) {
        this.main = main;
        touches = new float[9];
        colors = new Vector3(0.6f, 0.0f, 0.6f);

        // Customize
        atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
        stage = new Stage(new FillViewport(main.WIDTH, main.HEIGHT));
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touches[count] = x;
                touches[count+1] = y;
                touches[count+2] = 0.01f;
                if (count < 6) {
                    count += 3;
                } else {
                    count = 0;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        container = new Table();
        stage.addActor(container);
        container.padBottom(120);
        container.setFillParent(true);
        container.align(Align.bottom);
        Table table = new Table();
        container.add(table);
        container.row();
        final TextButton customize = new TextButton("@ Customize", skin);
        container.add(customize);
        customize.setTransform(true);
        customize.setOrigin(Align.center);
        customize.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                customize.clearActions();
                customize.setScale(0.5f);
                customize.addAction(Actions.scaleTo(1,1,1,Interpolation.elasticOut));
                super.clicked(event, x, y);
            }}
        );

        // Shader
        image = new Texture(Gdx.files.internal("background.jpg"));
        ShaderProgram.pedantic = false;
        shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/v.vsh"), Gdx.files.internal("shaders/f.fsh"));
        if (!shaderProgram.isCompiled())
            Gdx.app.log("SHADER", "NO COMPILE!");
        main.batch.setShader(shaderProgram);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        if (time < 0) {
            timeAttr = 1;
        } else if (time > 3.33) {
            timeAttr = -1;
        }
        time += delta*0.13f*timeAttr;

        for (int c = 0; c < 9; c++) {
            if ((c+1) % 3 == 0) {
                if (touches[c] < 10 && touches[c] > 0) {
                    touches[c] += delta * 6.66f;
                }
            }
        }

        main.batch.setShader(shaderProgram);
        shaderProgram.begin();
        shaderProgram.setUniformf("time", time);
        shaderProgram.setUniform1fv("touches", touches, 0 , 9);
        shaderProgram.setUniformf("offset", main.offset);
        shaderProgram.setUniformf("resolution", main.WIDTH, main.HEIGHT);
        shaderProgram.setUniformf("colors", colors);
        shaderProgram.end();

        main.batch.begin();
        main.batch.draw(image,0,0, main.WIDTH, main.HEIGHT);
        main.batch.end();

        main.batch.setShader(null);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        image.dispose();
        atlas.dispose();
        skin.dispose();
        shaderProgram.dispose();
    }
}
