package com.bdzjn.xml.repository.impl;

import com.bdzjn.xml.model.QSession;
import com.bdzjn.xml.model.Session;
import com.bdzjn.xml.model.enumeration.SessionStatus;
import com.bdzjn.xml.repository.SessionRepositoryCustom;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import java.util.Optional;

public class SessionRepositoryImpl extends QueryDslRepositorySupport implements SessionRepositoryCustom {

    public SessionRepositoryImpl() {
        super(Session.class);
    }

    @Override
    public Optional<Session> findOpenSession() {
        final QSession session = QSession.session;
        final Session result = from(session)
                .where(session.status.ne(SessionStatus.CLOSED))
                .fetchFirst();

        return Optional.ofNullable(result);
    }
}
