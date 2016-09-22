package com.grebtsew.app.ultimategamecounter.Structures;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Westberg on 2016-08-10.
 */
public class Player {
    public String name;
    public int score = 0;
    public List<Integer> scores = new ArrayList<Integer>();

    public Player(String name) {
        this.name = name;

    }

    public void SetScore(int score) {
        this.score = score;
        scores.add(score);
    }

    public int getTotScore() {
        int res = 0;
        for (int i : scores) {
            res += i;
        }
        return res;
    }

}
