package com.bdzjn.xml.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Attendance {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Session session;

}
