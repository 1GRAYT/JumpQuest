package ru.samsung.jumpquest;

import static ru.samsung.jumpquest.Main.SCR_HEIGHT;
import static ru.samsung.jumpquest.Main.SCR_WIDTH;
import static ru.samsung.jumpquest.Main.englishLanguage;
import static ru.samsung.jumpquest.Main.russianLanguage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenAbout implements Screen {
    private Main main;

    private SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font;

    Texture imgBG;

    private String AboutText;
    private String MadeText;

    QuestButton btnBack;

    public ScreenAbout(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font;

        imgBG = new Texture("bgabout.png");

        btnBack = new QuestButton(font, "Back", 158);
    }

    @Override
    public void show() {
        loadLanguageText();
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBack.hit(touch.x, touch.y)) {
                main.setScreen(main.screenMenu);
            }
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, AboutText, 0, 1400, SCR_WIDTH, Align.center, true);
        font.draw(batch, MadeText, 0, 1300, SCR_WIDTH, Align.center, true);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
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

    private void loadLanguageText() {
        switch (main.screenSettings.language) {
            case englishLanguage:
                AboutText = "About";
                MadeText = "Made by Ilia Gumarov";
                btnBack.text = "Back"; break;
            case russianLanguage:
                AboutText = "Об Игре";
                MadeText = "Сделано Ильей Гумаровым";
                btnBack.text = "Назад";
        }
    }
}
