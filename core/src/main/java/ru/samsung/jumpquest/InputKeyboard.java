package ru.samsung.jumpquest;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class InputKeyboard {
    String keysFileName = "keys.png";
    private final BitmapFont font;

    private final float x, y;
    private final float keyboardWidth, keyboardHeight;
    private final float keyWidth, keyHeight;
    private final float padding = 0;
    private final int enterTextLength;

    boolean isKeyboardShow;
    private boolean endOfEdit;

    private String text = "";

    private static final String LETTERS_EN_CAPS = "1234567890-~QWERTYUIOP+?^ASDFGHJKL;'`ZXCVBNM<> |";
    private static final String LETTERS_EN_LOW  = "!@#$%:&*()_~qwertyuiop[]^asdfghjkl:'`zxcvbnm,. |";
    private static final String LETTERS_RU_CAPS = "1234567890-~ЙЦУКЕНГШЩЗХЪ^ФЫВАПРОЛДЖЭ`ЯЧСМИТЬБЮЁ|";
    private static final String LETTERS_RU_LOW  = "!@#$%:&*()_~йцукенгшщзхъ^фывапролджэ`ячсмитьбюё|";
    private String letters = LETTERS_EN_CAPS;

    private final Texture imgAtlasKeys;
    private final TextureRegion imgEditText;
    private final TextureRegion imgKeyUP, imgKeyDown;
    private final TextureRegion imgKeyBS, imgKeyEnter, imgKeyCL, imgKeySW, imgBackground;

    private long timeStartPressKey, timeDurationPressKey = 150;
    private int keyPressed = -1;
    private final Array<Key> keys = new Array<>();

    public InputKeyboard(BitmapFont font, float scrWidth, float scrHeight, int enterTextLength){
        this.font = font;
        this.enterTextLength = enterTextLength;

        imgAtlasKeys = new Texture(keysFileName);
        imgKeyUP = new TextureRegion(imgAtlasKeys, 0, 0, 256, 256);
        imgKeyDown = new TextureRegion(imgAtlasKeys, 256, 0, 256, 256);
        imgEditText = new TextureRegion(imgAtlasKeys, 256*2, 0, 256, 256);
        imgKeyBS = new TextureRegion(imgAtlasKeys, 256*3, 0, 256, 256);
        imgKeyEnter = new TextureRegion(imgAtlasKeys, 256*4, 0, 256, 256);
        imgKeyCL = new TextureRegion(imgAtlasKeys, 256*5, 0, 256, 256);
        imgKeySW = new TextureRegion(imgAtlasKeys, 256*6, 0, 256, 256);
        imgBackground = new TextureRegion(createSolidColorTexture(new Color(0.15f, 0.1f, 0.08f, 1)));

        keyboardWidth = scrWidth/21f*20;
        keyboardHeight = scrHeight/5f*3;
        x = (scrWidth- keyboardWidth)/2;
        y = keyboardHeight +scrHeight/30f;
        keyWidth = keyboardWidth/13;
        keyHeight = keyboardHeight/5;
        createKBD();
    }

    private Texture createSolidColorTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void createKBD(){
        int j = 0;
        for (int i = 0; i < 12; i++, j++)
            keys.add(new Key(i*keyWidth+x+keyWidth/2, y-keyHeight*2, keyWidth-padding, keyHeight-padding, letters.charAt(j)));

        for (int i = 0; i < 13; i++, j++)
            keys.add(new Key(i*keyWidth+x, y-keyHeight*3, keyWidth-padding, keyHeight-padding, letters.charAt(j)));

        for (int i = 0; i < 12; i++, j++)
            keys.add(new Key(i*keyWidth+x+keyWidth/2, y-keyHeight*4, keyWidth-padding, keyHeight-padding, letters.charAt(j)));

        for (int i = 0; i < 11; i++, j++)
            keys.add(new Key(i*keyWidth+x+keyWidth, y-keyHeight*5, keyWidth-padding, keyHeight-padding, letters.charAt(j)));
    }

    private void setCharsKBD() {
        int j = 0;
        for (int i = 0; i < 12; i++, j++)
            keys.get(j).letter = letters.charAt(j);

        for (int i = 0; i < 13; i++, j++)
            keys.get(j).letter = letters.charAt(j);

        for (int i = 0; i < 12; i++, j++)
            keys.get(j).letter = letters.charAt(j);

        for (int i = 0; i < 11; i++, j++)
            keys.get(j).letter = letters.charAt(j);
    }

    public void draw(SpriteBatch batch){
        if(isKeyboardShow) {
            float pad = 30;
            batch.draw(imgBackground,
                x - pad,
                y - keyboardHeight - pad,
                keyboardWidth + pad*2,
                keyboardHeight + keyHeight);
            for (int i = 0; i < keys.size; i++) {
                drawImgKey(batch, i, keys.get(i).x, keys.get(i).y, keys.get(i).width, keys.get(i).height);
            }

            batch.draw(imgEditText, 2 * keyWidth + x + keyWidth / 2, y - keyHeight, keyboardWidth - 5 * keyWidth - padding, keyHeight);
            font.draw(batch, text, 2 * keyWidth + x + keyWidth / 2, keys.get(0).letterY + keyHeight, keyboardWidth - 5 * keyWidth - padding, Align.center, false);
        }
    }

    private void drawImgKey(SpriteBatch batch, int i, float x, float y, float width, float height){
        float dx, dy;
        if(keyPressed == i){
            batch.draw(imgKeyDown, x, y, width, height);
            dx = 2;
            dy = -2;
            if(TimeUtils.millis() - timeStartPressKey > timeDurationPressKey){
                keyPressed = -1;
            }
        } else {
            dx = 0;
            dy = 0;
            batch.draw(imgKeyUP, x, y, width, height);
        }


        switch (letters.charAt(i)) {
            case '~': batch.draw(imgKeyBS, x+dx, y+dy, width, height); break;
            case '^': batch.draw(imgKeyEnter, x+dx, y+dy, width, height); break;
            case '`': batch.draw(imgKeyCL, x+dx, y+dy, width, height); break;
            case '|': batch.draw(imgKeySW, x+dx, y+dy, width, height); break;
            default:
                font.draw(batch, ""+keys.get(i).letter, keys.get(i).letterX+dx, keys.get(i).letterY+dy);
        }
    }


    public boolean touch(float tx, float ty){
        if(isKeyboardShow) {
            for (int i = 0; i < keys.size; i++) {
                if (!keys.get(i).hit(tx, ty).isEmpty()) {
                    keyPressed = i;
                    setText(i);
                    timeStartPressKey = TimeUtils.millis();
                }
            }

            if (endOfEdit) {
                endOfEdit = false;
                isKeyboardShow = false;
                return true;
            }
        }
        return false;
    }

    public boolean touch(Vector3 t){
        if(isKeyboardShow) {
            for (int i = 0; i < keys.size; i++) {
                if (!keys.get(i).hit(t.x, t.y).isEmpty()) {
                    keyPressed = i;
                    setText(i);
                    timeStartPressKey = TimeUtils.millis();
                }
            }
            if (endOfEdit) {
                endOfEdit = false;
                isKeyboardShow = false;
                return true;
            }
        }
        return false;
    }

    private void setText(int i){
        switch (letters.charAt(i)) {
            case '~':
                if(!text.isEmpty()) text = text.substring(0, text.length() - 1);
                break;
            case '^':
                if(text.isEmpty()) break;
                endOfEdit = true;
                break;
            case '`':
                if(letters.charAt(12) == 'Q') letters = LETTERS_EN_LOW;
                else if(letters.charAt(12) == 'q') letters = LETTERS_EN_CAPS;
                else if(letters.charAt(12) == 'Й') letters = LETTERS_RU_LOW;
                else if(letters.charAt(12) == 'й') letters = LETTERS_RU_CAPS;
                setCharsKBD();
                break;
            case '|':
                if(letters.charAt(12) == 'й') letters = LETTERS_EN_LOW;
                else if(letters.charAt(12) == 'Й') letters = LETTERS_EN_CAPS;
                else if(letters.charAt(12) == 'q') letters = LETTERS_RU_LOW;
                else if(letters.charAt(12) == 'Q') letters = LETTERS_RU_CAPS;
                setCharsKBD();
                break;
            default:
                if(text.length() < enterTextLength) text += letters.charAt(i);
                setCharsKBD();
        }
    }

    public String getText() {
        return text;
    }

    private class Key {
        float x, y;
        float width, height;
        char letter;
        float letterX, letterY;

        private Key (float x, float y, float width, float height, char letter) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.letter = letter;
            letterX = x + width/3;
            letterY = y + height - (height - font.getCapHeight())/2;
        }

        private String hit(float tx, float ty){
            if (x<tx && tx<x+width && y<ty && ty<y+height) {
                return "" + letter;
            }
            return "";
        }
    }

    public void start(){
        isKeyboardShow = true;
    }

    public void dispose(){
        imgAtlasKeys.dispose();
        imgBackground.getTexture().dispose();
    }
}
