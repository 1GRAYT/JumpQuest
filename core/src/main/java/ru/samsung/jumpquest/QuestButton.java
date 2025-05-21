package ru.samsung.jumpquest;

import static ru.samsung.jumpquest.Main.SCR_WIDTH;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;

public class QuestButton {
    BitmapFont font;
    String text;
    float x, y;
    float width, height;

    public QuestButton(BitmapFont font, String text, float x, float y, boolean isRight) {
        this.font = font;
        this.text = text;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
        this.x = isRight ? (SCR_WIDTH - width - x) : x;
        this.y = y;
    }

    public QuestButton(BitmapFont font, String text, float x, float y) {
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }


    public QuestButton(BitmapFont font, String text, float y) {
        this.font = font;
        this.text = text;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
        this.x = SCR_WIDTH/2 - width/2;
    }

    public void setText(String text) {
        this.text = text;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    boolean hit(float tx, float ty) {
        return x<tx && tx<x+width && y>ty && ty>y-height;
    }
    boolean hit(Vector3 t) {
        return x<t.x && t.x<x+width && y>t.y && t.y>y-height;
    }
}
