package ru.samsung.jumpquest;

public class Player {
    String name = "Noname";
    int score;

    public void clone(Player p) {
        name = p.name;
        score = p.score;
    }

}
