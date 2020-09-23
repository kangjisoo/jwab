package org.techtown.randomgame;

import java.io.Serializable;

public class RandomGameData implements Serializable {

    private String randomGameListItem;
    private int initNum=0;

    public RandomGameData(String randomGameListItem){
        this.randomGameListItem = randomGameListItem;
    }

    public String getRandomGameListItem() {
        return randomGameListItem;
    }

    public void setRandomGameListItem(String randomGameListItem) {
        this.randomGameListItem = randomGameListItem;
    }

    public int getInitNum(){ return initNum;}
    public void setInitNum(int num){ this.initNum=num;}


}
