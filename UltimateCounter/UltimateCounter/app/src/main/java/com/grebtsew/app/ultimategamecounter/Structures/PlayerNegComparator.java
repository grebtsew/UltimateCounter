package com.grebtsew.app.ultimategamecounter.Structures;

import java.util.Comparator;

/**
 * Created by Daniel Westberg on 2016-08-30.
 */
public class PlayerNegComparator implements Comparator<Player> {

    @Override
    public int compare(Player player1, Player player2) {
        return player1.score - player2.score; // ascending
    }
}
