package org.techtown.randomgame;

public class RandomGameData extends RandomGameMyItem{

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
