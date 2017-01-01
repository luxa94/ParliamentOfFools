package com.bdzjn.xml.controller.dto;


import java.util.Date;

public class SessionDTO {

    private String name;
    private Date submissionStartsOn;
    private Date startsOn;

    @Override
    public String toString() {
        return "SessionDTO{" +
                "name='" + name + '\'' +
                ", submissionStartsOn=" + submissionStartsOn +
                ", startsOn=" + startsOn +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getSubmissionStartsOn() {
        return submissionStartsOn;
    }

    public void setSubmissionStartsOn(Date submissionStartsOn) {
        this.submissionStartsOn = submissionStartsOn;
    }

    public Date getStartsOn() {
        return startsOn;
    }

    public void setStartsOn(Date startsOn) {
        this.startsOn = startsOn;
    }
}
