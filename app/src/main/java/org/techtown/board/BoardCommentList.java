package org.techtown.board;

public class BoardCommentList {

    String comment, writer, date;

    public BoardCommentList(String comment, String writer, String date){
        this.comment = comment;
        this.writer = writer;
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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


}
