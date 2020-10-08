package org.techtown.vote;

public class VoteFinishData {
    private boolean voteRadioButton;
    private String voteFinishItems;

    public VoteFinishData(boolean voteRadioButton, String voteFinishItems) {
        this.voteRadioButton = voteRadioButton;
        this.voteFinishItems = voteFinishItems;
    }

    public boolean isVoteRadioButton() {
        return voteRadioButton;
    }

    public void setVoteRadioButton(boolean voteRadioButton) {
        this.voteRadioButton = voteRadioButton;
    }

    public String getVoteFinishItems() {
        return voteFinishItems;
    }

    public void setVoteFinishItems(String voteFinishItems) {
        this.voteFinishItems = voteFinishItems;
    }

}
