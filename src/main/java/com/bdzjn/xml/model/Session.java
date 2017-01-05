package com.bdzjn.xml.model;

import com.bdzjn.xml.model.enumeration.SessionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Session {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private Date submissionStartsOn;

    @NotNull
    private Date startsOn;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", submissionStartsOn=" + submissionStartsOn +
                ", startsOn=" + startsOn +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (!submissionStartsOn.equals(session.submissionStartsOn)) return false;
        if (!startsOn.equals(session.startsOn)) return false;
        if (!name.equals(session.name)) return false;
        return status == session.status;
    }

    @Override
    public int hashCode() {
        int result = submissionStartsOn.hashCode();
        result = 31 * result + startsOn.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

}
