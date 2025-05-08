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

public class ScreenLeaderBoard implements Screen {
    private Main main;

    private SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font;

    Texture imgBG;

    private String LeaderBoardText;
    private String NameOfBoardText;
    private String ScoreOfBoardText;

    QuestButton btnClear;
    QuestButton btnBack;
    Player[] players;

    public ScreenLeaderBoard(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font;

        imgBG = new Texture("bglead.png");

        btnClear = new QuestButton(font, "Clear",300);
        btnBack = new QuestButton(font, "Back", 150);
        players = main.screenGame.players;
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

            if(btnClear.hit(touch)) {
                main.screenGame.clearTableOfRecords();
                main.screenGame.saveTableOfRecords();
            }
            if(btnBack.hit(touch.x, touch.y)) {
                main.setScreen(main.screenMenu);
            }
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, LeaderBoardText, 0, 1400, SCR_WIDTH, Align.center, true);
        font.draw(batch, NameOfBoardText, 100, 1180, 200, Align.right, true);
        font.draw(batch, ScoreOfBoardText, 500, 1180, 200, Align.right, true);
        for (int i = 0; i < players.length-1; i++) {
            font.draw(batch, players[i].name, 100, 1100 - 70 * i);
            font.draw(batch, "" + players[i].score, 500, 1100 - 70 * i, 200, Align.right, true);
        }
        btnClear.font.draw(batch, btnClear.text, btnClear.x, btnClear.y);
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
                LeaderBoardText = "Leader Board";
                NameOfBoardText = "Name";
                ScoreOfBoardText = "Score";
                btnClear.text = "Clear";
                btnBack.text = "Back"; break;
            case russianLanguage:
                LeaderBoardText = "Рекорды";
                NameOfBoardText = "Имя";
                ScoreOfBoardText = "Очки";
                btnClear.text = "Чистка";
                btnBack.text = "Назад";
        }
    }
}
