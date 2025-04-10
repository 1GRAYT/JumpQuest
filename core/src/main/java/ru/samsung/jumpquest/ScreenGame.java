package ru.samsung.jumpquest;

import static ru.samsung.jumpquest.Main.SCR_HEIGHT;
import static ru.samsung.jumpquest.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenGame implements Screen {
    private Main main;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font;

    John john;

    Texture imgBG;
    Texture imgBG2;
    Texture whitePixel;

    Texture[] imgJohn = new Texture[2];
    Texture[] imgGrounds = new Texture[4];
    Texture[] imgStars = new Texture[8];

    SpaceButton btnBack;

    private Music defaultMusic;
    private Music gameOverMusic;
    public Sound jumpSound;
    public Sound starSound;

    List<Ground> grounds = new ArrayList<>();
    List<Star> stars = new ArrayList<>();
    Sky[] sky = new Sky[2];

    public int score = 0;

    private long timeLastSpawnGround, timeIntervalSpawnGround = 1000;
    public static boolean isGameOver = false;
    private boolean hasGameOverMusicPlayed = false;
    private long timeLastScore, timeIntervalScore = 200;
    private long timeLastSpawnStar, timeIntervalSpawnStar = 5000;
    private long timeLastSpeedUp, timeIntervalSpeedUp = 100;
    private long timeLastPhaseStar, timePhaseIntervalStar = 50;
    public float speedMultiply;


    public ScreenGame(Main main) {
        this.main = main;
        shapeRenderer = new ShapeRenderer();
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        whitePixel = new Texture(pixmap);

        imgBG = new Texture("bggame.png");
        imgBG2 = new Texture("bggame2.png");
        for (int i = 0; i < imgJohn.length; i++) {
            imgJohn[i] = new Texture(i==0?"run3.png":"run2.png");
        }
        for (int i = 1; i < imgGrounds.length; i++) {
            imgGrounds[i] = new Texture(groundFile(i));
        }
        for (int i = 0; i < imgStars.length; i++) {
            imgStars[i] = new Texture(starFile(i+1));
            System.out.println(starFile(i+1));
        }


        btnBack = new SpaceButton(font, "X", 850, 1580);

        defaultMusic = Gdx.audio.newMusic(Gdx.files.internal("defaultmusic.mp3"));
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("overmusic.mp3"));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jumpsound.mp3"));
        starSound = Gdx.audio.newSound(Gdx.files.internal("soundstar.mp3"));

        sky[0] = new Sky(0, 0);
        sky[1] = new Sky(SCR_WIDTH, 0);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new JumpQuestInputProcessor());
        gameStart();
    }

    @Override
    public void render(float delta) {
        //касания
        if(Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBack.hit(touch.x, touch.y)) {
                main.setScreen(main.screenMenu);
                defaultMusic.setLooping(true);
                defaultMusic.stop();
            }
        }

        //события
        //
        for(Sky s:sky) s.move();
        spawnGround();
        spawnStar();
        score();
        for(Ground g:grounds) g.move();
        for(Star s:stars) s.move();
        for(int i = stars.size()-1; i >= 0; i--) {
            if(stars.get(i).outOfScreen()) {
                stars.remove(i);
                break;
            }
            if(stars.get(i).overlap(john)) {
                score += stars.get(i).price;
                starSound.play();
                stars.remove(i);
            }
        }
        john.move();

        //отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for(int i = 0; i < sky.length; i++) {
            batch.draw(i==0?imgBG:imgBG2, sky[i].x, sky[i].y, sky[i].width, sky[i].height);
        }
        font.draw(batch, Integer.toString(score), 100, 1500);
        for (Ground g:grounds) {
            batch.draw(imgGrounds[g.type], g.scrX(), g.scrY(), g.width, g.height);
        }
        for (Star s:stars) {
            batch.draw(imgStars[s.type], s.scrX(), s.scrY(), s.width, s.height);
        }

        for (Ground g:grounds) {
            switch(g.type) {
                case 1:
                    g.realHeight = g.height-50;
                    break;
                case 2:
                    g.realHeight = g.height-200;
                    break;
                case 3:
                    g.realHeight = g.height-350;
            }
            if(john.x+john.width-30 >= g.x && john.y < g.realHeight-100) {
                if (john.y < g.realHeight) {
                    gameOver();
                }
            }
            else if(john.x+john.width >= g.x && john.x<g.x+g.width && !isGameOver) {
                if(john.y<=g.realHeight && john.y>0) {
                    john.y = g.realHeight;
                }
                if(john.y == g.realHeight && !isGameOver) john.isOnGround = true;
            }
        }
        for (int i = grounds.size()-1; i>=0; i--) {
            if(grounds.get(i).outOfScreen()) {
                grounds.remove(i);
            }
        }
        batch.draw(imgJohn[john.phase], john.scrX(), john.scrY(), john.width, john.height);

        //полоса начало

        batch.setColor(Color.BLACK);
        batch.draw(whitePixel, 280, 1280, 340, 90);
        batch.setColor(Color.WHITE);
        batch.draw(whitePixel, 290, 1290, 320, 70);
        if(john.jumpForce < 10) {
            batch.setColor(Color.GREEN);
        } else if(john.jumpForce >= 10 && john.jumpForce < 20) {
            batch.setColor(Color.YELLOW);
        } else {
            batch.setColor(Color.RED);
        }
        batch.draw(whitePixel, 300, 1300, john.jumpForce*10, 50);
        batch.setColor(Color.BLACK);
        batch.draw(whitePixel, 400-10/2, 1290, 10, 70);
        batch.draw(whitePixel, 500-10/2, 1290, 10, 70);
        batch.setColor(Color.WHITE);

        //полоса конец

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
        for (int i = 0; i < imgJohn.length; i++) {
            imgJohn[i].dispose();
        }
        defaultMusic.dispose();
    }

    private void gameStart() {
        john = new John(300, 500);
        grounds.clear();
        stars.clear();
        isGameOver = false;
        grounds.add(new Ground(300, 0));
        grounds.add(new Ground(900, 0));
        defaultMusic.play();
        score = 0;
        hasGameOverMusicPlayed = false;
    }

    private void gameOver() {
        isGameOver = true;
        john.isOnGround = false;
        defaultMusic.stop();
        if(!hasGameOverMusicPlayed) {
            gameOverMusic.play();
            main.allScore += score;
            main.screenMenu.saveAllScore();
            hasGameOverMusicPlayed = true;
        }

    font.draw(batch, "Game Over!", 0, SCR_HEIGHT/2, SCR_WIDTH, Align.center, true);
    }

    private void spawnGround(){
        if(TimeUtils.millis()>timeLastSpawnGround+timeIntervalSpawnGround && !isGameOver) {
            grounds.add(new Ground(1600, 0));
            timeLastSpawnGround = TimeUtils.millis();
        }
    }

    private void changePhaseOfStar(Star s){
        if(TimeUtils.millis()>timeLastPhaseStar+timePhaseIntervalStar && !isGameOver) {
            if(s.phase!=s.nPhases-1) s.phase++;
            else {
                s.isDead = true;
                s.phase = s.nPhases-1;
            }
            timeLastPhaseStar = TimeUtils.millis();
        }
    }

    private void spawnStar(){
        if(TimeUtils.millis()>timeLastSpawnStar+timeIntervalSpawnStar && !isGameOver) {
            stars.add(new Star(1600, 1000));
            timeLastSpawnStar = TimeUtils.millis();
        }
    }

    private void speedUp(){
        if(TimeUtils.millis()>timeLastSpeedUp+timeIntervalSpeedUp && !isGameOver) {
            speedMultiply += 0.01;
            timeLastSpeedUp = TimeUtils.millis();
        } else if(isGameOver) {
            speedMultiply = 0;
        }
    }

    private void score(){
        if(TimeUtils.millis()>timeLastScore+timeIntervalScore && !isGameOver) {
            score++;
            timeLastScore = TimeUtils.millis();
        }
    }


    private String groundFile(int number) {
        return "ground"+Integer.toString(number)+".png";
    }

    private String starFile(int number) {
        return "star"+Integer.toString(number)+".png";
    }

    class JumpQuestInputProcessor implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            john.startCharging();
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if(john.isCharging) jumpSound.play();
            john.endJump();
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
