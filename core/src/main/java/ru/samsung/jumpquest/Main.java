package ru.samsung.jumpquest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public static final float SCR_WIDTH = 900;
    public static final float SCR_HEIGHT = 1600;
    public static final int englishLanguage = 0, russianLanguage = 1;
    public static final int summerType = 0, winterType = 1;
    public static final int extraLifePrice = 1000;
    public static final int summerPrice = 1000;
    public static final int winterPrice = 2000;

    SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font;
    public BitmapFont fontGray;

    public int allScore = 0;

    public ScreenMenu screenMenu;
    public ScreenGame screenGame;
    public ScreenSettings screenSettings;
    public ScreenLeaderBoard screenLeaderBoard;
    public ScreenAbout screenAbout;
    public ScreenStore screenStore;

    public Player player;


    @Override
    public void create() {
        Gdx.graphics.setForegroundFPS(220);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        touch = new Vector3();
        font = new BitmapFont(Gdx.files.internal("Fonts/whiteFont.fnt"));
        fontGray = new BitmapFont(Gdx.files.internal("Fonts/grayFont.fnt"));

        player = new Player();

        screenMenu = new ScreenMenu(this);
        screenGame = new ScreenGame(this);
        screenSettings = new ScreenSettings(this);
        screenLeaderBoard = new ScreenLeaderBoard(this);
        screenAbout = new ScreenAbout(this);
        screenStore = new ScreenStore(this);
        setScreen(screenMenu);

        screenSettings.loadSettings();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
