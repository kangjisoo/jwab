package org.techtown.laddergame;

public class LadderGameThis {

    boolean[][] points;

    LadderGameThis(int lineCount, int rows){
        points = new boolean[rows][lineCount];
    }

    void drawLine(int row, int order){

        points[row][order] = true;
        points[row][order+1]=true;

        //points[row][order] =true;

    }

    int run(int source){
        return source;
    }
}
