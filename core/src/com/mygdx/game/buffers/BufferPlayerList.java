package com.mygdx.game.buffers;

import com.mygdx.game.Player;

import java.util.ArrayList;

/**
 * Get players list
 */

public class BufferPlayerList {
    private ArrayList<Player> playersList;

    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(ArrayList<Player> playersList) {
        this.playersList = playersList;
    }
}
