package org.techtown.vote;

public class VoteDPData {
    private String voteTitleShow;
    private String voteInfoShow;
    private String voteConfirmShow;

    public VoteDPData(String voteTitleShow, String voteInfoShow, String voteConfirmShow){
        this.voteTitleShow=voteTitleShow;
        this.voteInfoShow=voteInfoShow;
        this.voteConfirmShow=voteConfirmShow;

    }

    public String getVoteTitleShow() {
        return voteTitleShow;
    }

    public void setVoteTitleShow(String voteTitleShow) {
        this.voteTitleShow = voteTitleShow;
    }

    public String getVoteInfoShow() {
        return voteInfoShow;
    }

    public void setVoteInfoShow(String voteInfoShow) {
        this.voteInfoShow = voteInfoShow;
    }

    public String getVoteConfirmShow() {
        return voteConfirmShow;
    }

    public void setVoteConfirmShow(String voteConfirmShow) {
        this.voteConfirmShow = voteConfirmShow;
    }
}
