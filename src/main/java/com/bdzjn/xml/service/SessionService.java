package com.bdzjn.xml.service;

import com.bdzjn.xml.controller.dto.SessionDTO;
import com.bdzjn.xml.controller.exception.NotFoundException;
import com.bdzjn.xml.controller.exception.UnprocessableEntityException;
import com.bdzjn.xml.model.Session;
import com.bdzjn.xml.model.enumeration.SessionStatus;
import com.bdzjn.xml.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session create(SessionDTO sessionDTO) {
        if (sessionRepository.findOpenSession().isPresent()) {
            throw new UnprocessableEntityException("Another open session already exists.");
        }
        final Session session = new Session();
        session.setName(sessionDTO.getName());
        session.setStartsOn(sessionDTO.getStartsOn());
        session.setStatus(SessionStatus.ANNOUNCED);

        return sessionRepository.save(session);
    }

    public Optional<Session> findById(long id) {
        return sessionRepository.findById(id);
    }

    public Optional<Session> findOpenSession() {
        return sessionRepository.findOpenSession();
    }

    public Session activateSession() {
        final Session session = findOpenSession().orElseThrow(NotFoundException::new);
        session.setStatus(SessionStatus.ACTIVE);
        return sessionRepository.save(session);
    }

    public Session finishSession() {
        final Session session = findOpenSession().orElseThrow(NotFoundException::new);
        session.setStatus(SessionStatus.CLOSED);
        return sessionRepository.save(session);
    }

    public void edit(SessionDTO sessionDTO) {
        final Session session = sessionRepository.findOpenSession().orElseThrow(NotFoundException::new);

        if (sessionDTO.getStartsOn().before(new Date()) || session.getStatus() != SessionStatus.ANNOUNCED) {
            throw new UnprocessableEntityException("Can not change a session after it started.");
        }
        session.setName(sessionDTO.getName());
        session.setStartsOn(sessionDTO.getStartsOn());

        sessionRepository.save(session);
    }

    public void delete() {
        final Session session = sessionRepository.findOpenSession().orElseThrow(NotFoundException::new);
        if (session.getStatus() != SessionStatus.ANNOUNCED) {
            throw new UnprocessableEntityException("Can not delete a session after it started.");
        }
        sessionRepository.delete(session);
    }
}
