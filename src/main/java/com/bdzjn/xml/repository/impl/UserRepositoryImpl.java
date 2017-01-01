package com.bdzjn.xml.repository.impl;


import com.bdzjn.xml.model.User;
import com.bdzjn.xml.repository.UserRepositoryCustom;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

public class UserRepositoryImpl extends QueryDslRepositorySupport implements UserRepositoryCustom {

    public UserRepositoryImpl() {
        super(User.class);
    }

}
