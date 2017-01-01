package com.bdzjn.xml.repository;


import com.bdzjn.xml.model.Session;

import java.util.Optional;

public interface SessionRepositoryCustom {

    Optional<Session> findOpenSession();

}
