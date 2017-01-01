package com.bdzjn.xml.repository;


import com.bdzjn.xml.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long>, SessionRepositoryCustom {

    Optional<Session> findById(long id);

}
