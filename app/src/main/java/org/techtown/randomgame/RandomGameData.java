package org.techtown.randomgame;

import java.io.Serializable;

public class RandomGameData implements Serializable {

    private String randomGameListItem;

    public RandomGameData(String randomGameListItem){
        this.randomGameListItem = randomGameListItem;
    }

    public String getRandomGameListItem() {
        return randomGameListItem;
    }

    public void setRandomGameListItem(String randomGameListItem) {
        this.randomGameListItem = randomGameListItem;
    }


}
