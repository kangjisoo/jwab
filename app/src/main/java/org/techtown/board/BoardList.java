package org.techtown.board;

public class BoardList {
    String title;
    String date;
    String writer;

    public BoardList(String title, String writer, String date){
        this.title = title;
        this.writer = writer;
        this.date = date;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }







}
