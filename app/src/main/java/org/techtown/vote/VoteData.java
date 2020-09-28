package org.techtown.vote;


public class VoteData {
    private boolean checkBoxCh;
    private String voteListValue;

    public VoteData(boolean checkBoxCh, String voteListValue){
        this.checkBoxCh = checkBoxCh;
        this.voteListValue = voteListValue;
    }

    public boolean isCheckBoxCh() {
        return checkBoxCh;
    }

    public void setCheckBoxCh(boolean checkBoxCh) {
        this.checkBoxCh = checkBoxCh;
    }

    public String getVoteListValue() {
        return voteListValue;
    }

    public void setVoteListValue(String voteListValue) {
        this.voteListValue = voteListValue;
    }


}
