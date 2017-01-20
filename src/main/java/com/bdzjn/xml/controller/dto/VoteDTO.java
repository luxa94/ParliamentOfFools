package com.bdzjn.xml.controller.dto;


public class VoteDTO {

    private int numberOfFor;
    private int numberAgainst;
    private int numberAbstained;

    public boolean isAccepted() {
        return numberOfFor >= numberAgainst;
    }

    public int getNumberOfFor() {
        return numberOfFor;
    }

    public void setNumberOfFor(int numberOfFor) {
        this.numberOfFor = numberOfFor;
    }

    public int getNumberAgainst() {
        return numberAgainst;
    }

    public void setNumberAgainst(int numberAgainst) {
        this.numberAgainst = numberAgainst;
    }

    public int getNumberAbstained() {
        return numberAbstained;
    }

    public void setNumberAbstained(int numberAbstained) {
        this.numberAbstained = numberAbstained;
    }
}
