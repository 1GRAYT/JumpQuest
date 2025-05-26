package ru.samsung.jumpquest;

import static ru.samsung.jumpquest.Main.SCR_HEIGHT;
import static ru.samsung.jumpquest.Main.SCR_WIDTH;
import static ru.samsung.jumpquest.Main.englishLanguage;
import static ru.samsung.jumpquest.Main.russianLanguage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenSettings implements Screen {
    private Main main;

    private SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font;
    public BitmapFont fontGray;

    private String SettingsText;
    private String LanguageText;
    public int language;

    private boolean isEnteringName;
    private String cheatCode;

    private InputKeyboard keyboard;

    Texture imgBG;

    QuestButton btnPlayerName;
    QuestButton btnEnglish;
    QuestButton btnRussian;
    QuestButton btnCheat;
    QuestButton btnBack;

    public ScreenSettings(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font;
        fontGray = main.fontGray;

        keyboard = new InputKeyboard(font, SCR_WIDTH, SCR_HEIGHT/2, 6);

        imgBG = new Texture("bgsettings.png");

        loadSettings();
        btnPlayerName = new QuestButton(font, "Name: "+main.player.name, 100, 1250);
        btnEnglish = new QuestButton(getFont(language), "English", 150, 950);
        btnRussian = new QuestButton(getFont(language), "Русский", 150, 800);
        btnCheat = new QuestButton(font, "*", 0, 350);
        btnBack = new QuestButton(font, "Back", 200);
    }

    @Override
    public void show() {
        selectLanguage();
        loadLanguageText();
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(keyboard.isKeyboardShow) {
                if(keyboard.touch(touch)) {
                    if(isEnteringName) {
                        main.player.name = keyboard.getText();
                        btnPlayerName.setText("Name: " + main.player.name);
                        loadLanguageText();
                        isEnteringName = false;
                    } else {
                        cheatCode = keyboard.getText();
                    }
                }
            } else {
                if (btnPlayerName.hit(touch)) {
                    isEnteringName = true;
                    keyboard.start();
                }

                if(btnEnglish.hit(touch)) {
                    language = englishLanguage;
                    selectLanguage();
                    loadLanguageText();
                    saveSettings();
                }

                if(btnRussian.hit(touch)) {
                    language = russianLanguage;
                    selectLanguage();
                    loadLanguageText();
                    saveSettings();
                }

                if (btnCheat.hit(touch)) {
                    isEnteringName = false;
                    keyboard.start();
                }

                if (btnBack.hit(touch)) {
                    saveSettings();
                    main.setScreen(main.screenMenu);
                }
            }
        }

        if("gumar".equals(cheatCode)) {
            main.screenGame.multiplier = 1000;
        } else if("liven".equals(cheatCode)) {
            main.player.name = "Изгой";
            btnPlayerName.setText("Name: " + main.player.name);
            loadLanguageText();
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, SettingsText, 0, 1400, SCR_WIDTH, Align.center, true);
        font.draw(batch, LanguageText, 100, 1100, SCR_WIDTH, Align.left, true);
        btnPlayerName.font.draw(batch, btnPlayerName.text, btnPlayerName.x, btnPlayerName.y);
        btnEnglish.font.draw(batch, btnEnglish.text, btnEnglish.x, btnEnglish.y);
        btnRussian.font.draw(batch, btnRussian.text, btnRussian.x, btnRussian.y);
        btnCheat.font.draw(batch, btnCheat.text, btnCheat.x, btnCheat.y);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        keyboard.draw(batch);
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

    private void selectLanguage() {
        btnEnglish.setFont(fontGray);
        btnRussian.setFont(fontGray);
        switch (language) {
            case englishLanguage: btnEnglish.setFont(font); break;
            case russianLanguage: btnRussian.setFont(font);
        }
    }

    private void loadLanguageText() {
        switch (language) {
            case englishLanguage:
                SettingsText = "Settings";
                LanguageText = "Language";
                btnPlayerName.setText("Name: " + main.player.name);
                btnBack = new QuestButton(font, "Back", 200);break;
            case russianLanguage:
                SettingsText = "Настройки";
                LanguageText = "Язык";
                btnPlayerName.setText("Имя: " + main.player.name);
                btnBack = new QuestButton(font, "Назад", 200);
        }
    }

    public BitmapFont getFont(int type) {
        return (language == type)? font : fontGray;
    }

    public void loadSettings() {
        Preferences prefs = Gdx.app.getPreferences("JumpQuestSettings");
        main.player.name = prefs.getString("name", "Noname");
        language = prefs.getInteger("language", englishLanguage);
    }

    public void saveSettings() {
        Preferences prefs = Gdx.app.getPreferences("JumpQuestSettings");
        prefs.putString("name", main.player.name);
        prefs.putInteger("language", language);
        prefs.flush();
    }
}
