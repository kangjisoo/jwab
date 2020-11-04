package org.techtown.vote;

public class VoteDPData {
    private String voteTitleShow;
    private String voteConfirmShow;
    private int voteProjectKey;

    public VoteDPData(String voteTitleShow, String voteConfirmShow, int voteProjectKey){
        this.voteTitleShow=voteTitleShow;
       this.voteConfirmShow=voteConfirmShow;
        this.voteProjectKey = voteProjectKey;

    }


    public String getVoteTitleShow() {
        return voteTitleShow;
    }

    public void setVoteTitleShow(String voteTitleShow) {
        this.voteTitleShow = voteTitleShow;
    }

    public String getVoteConfirmShow() {return voteConfirmShow;}

    public void setVoteConfirmShow(String voteConfirmShow) {
        this.voteConfirmShow = voteConfirmShow;
    }
    public int getVoteProjectKey() {
        return voteProjectKey;
    }

    public void setVoteProjectKey(int voteProjectKey) {
        this.voteProjectKey = voteProjectKey;
    }
}
