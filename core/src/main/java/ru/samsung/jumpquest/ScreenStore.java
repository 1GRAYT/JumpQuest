package ru.samsung.jumpquest;

import static ru.samsung.jumpquest.Main.SCR_HEIGHT;
import static ru.samsung.jumpquest.Main.SCR_WIDTH;
import static ru.samsung.jumpquest.Main.extraLifePrice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenStore implements Screen {
    private Main main;

    private SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font;
    public BitmapFont fontGray;

    Texture imgBG;

    QuestButton btnBuyExtraLife;
    QuestButton btnBuyMultiplier;
    QuestButton btnBack;

    public ScreenStore(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font;
        fontGray = main.fontGray;

        imgBG = new Texture("bgstore.png");

        btnBuyExtraLife = new QuestButton(fontBuy(main.screenGame.extraLife), extraLifeBought(main.screenGame.extraLife, extraLifePrice), 1200);
        btnBuyMultiplier = new QuestButton(font, multiplierText(main.screenGame.multiplier, main.screenGame.multiplierPrice), 1050);
        btnBack = new QuestButton(font, "Back", 158);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBuyExtraLife.hit(touch.x, touch.y) && !main.screenGame.extraLife && main.allScore>=extraLifePrice) {
                main.allScore-=extraLifePrice;
                main.screenGame.extraLife = true;
                saveStore();
                main.screenMenu.saveAllScore();
            } else if(btnBuyMultiplier.hit(touch.x, touch.y) && main.allScore>=main.screenGame.multiplierPrice) {
                main.allScore-=main.screenGame.multiplierPrice;
                main.screenGame.multiplier+=1;
                main.screenGame.multiplierPrice*=2;
                saveStore();
                main.screenMenu.saveAllScore();
            } else if(btnBack.hit(touch.x, touch.y)) {
                main.setScreen(main.screenMenu);
            }
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, "Store", 0, 1400, SCR_WIDTH, Align.center, true);
        font.draw(batch, "Score: " + Integer.toString(main.allScore), 100, 1500);
        if(main.screenGame.extraLife) font.draw(batch, extraLifeBought(main.screenGame.extraLife, extraLifePrice), btnBuyExtraLife.x, btnBuyExtraLife.y);
        else fontGray.draw(batch, extraLifeBought(main.screenGame.extraLife, extraLifePrice), btnBuyExtraLife.x, btnBuyExtraLife.y);
        btnBuyMultiplier.font.draw(batch, multiplierText(main.screenGame.multiplier, main.screenGame.multiplierPrice), btnBuyMultiplier.x, btnBuyMultiplier.y);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        batch.end();

    }

    private String multiplierText(int multiplier, int price) {
        return "Multiplier X"+multiplier+": "+price;
    }

    private String extraLifeBought(boolean isBought, int price) {
        return "Extra Life: " + (isBought?"Bought":price);
    }

    private BitmapFont fontBuy(boolean isBought) {
        return isBought ? font : fontGray;
    }

    public void loadStore() {
        Preferences prefs = Gdx.app.getPreferences("JumpQuestStore");
        main.screenGame.extraLife = prefs.getBoolean("extralife", false);
        main.screenGame.multiplier = prefs.getInteger("multiplier", 1);
        main.screenGame.multiplierPrice = prefs.getInteger("multiplierPrice", 2000);
    }

    public void saveStore() {
        Preferences prefs = Gdx.app.getPreferences("JumpQuestStore");
        prefs.putBoolean("extralife", main.screenGame.extraLife);
        prefs.putInteger("multiplier", main.screenGame.multiplier);
        prefs.putInteger("multiplierPrice", main.screenGame.multiplierPrice);
        prefs.flush();
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
