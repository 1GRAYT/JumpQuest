package ru.samsung.jumpquest;

import static ru.samsung.jumpquest.Main.SCR_HEIGHT;
import static ru.samsung.jumpquest.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenMenu implements Screen {
    private Main main;

    private SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font;

    Texture imgBG;

    SpaceButton btnGame;
    SpaceButton btnSettings;
    SpaceButton btnLeaderBoard;
    SpaceButton btnStore;
    SpaceButton btnAbout;
    SpaceButton btnExit;

    public ScreenMenu(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font;

        imgBG = new Texture("bgmenu.png");

        btnGame = new SpaceButton(font, "Play", 250, 1100);
        btnSettings = new SpaceButton(font, "Settings", 250, 950);
        btnLeaderBoard = new SpaceButton(font, "LeaderBoard", 250, 800);
        btnStore = new SpaceButton(font, "Store", 250, 650);
        btnAbout = new SpaceButton(font, "About", 250, 500);
        btnExit = new SpaceButton(font, "Exit", 250, 350);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnGame.hit(touch.x, touch.y)) {
                main.setScreen(main.screenGame);
            }
            if(btnSettings.hit(touch.x, touch.y)) {
                main.setScreen(main.screenSettings);
            }
            if(btnLeaderBoard.hit(touch.x, touch.y)) {
                main.setScreen(main.screenLeaderBoard);
            }
            if(btnStore.hit(touch.x, touch.y)) {
                main.setScreen(main.screenStore);
            }
            if(btnAbout.hit(touch.x, touch.y)) {
                main.setScreen(main.screenAbout);
            }
            if(btnExit.hit(touch.x, touch.y)) {
                Gdx.app.exit();
            }
        }


        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, "Jump Quest", 0, 1400, SCR_WIDTH, Align.center, true);
        btnGame.font.draw(batch, btnGame.text, btnGame.x, btnGame.y);
        btnSettings.font.draw(batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnLeaderBoard.font.draw(batch, btnLeaderBoard.text, btnLeaderBoard.x, btnLeaderBoard.y);
        btnStore.font.draw(batch, btnStore.text, btnStore.x, btnStore.y);
        btnAbout.font.draw(batch, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(batch, btnExit.text, btnExit.x, btnExit.y);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

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
        imgBG.dispose();

    }
}
