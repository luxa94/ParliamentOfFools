package com.bdzjn.xml.service;

import com.bdzjn.xml.model.act.Act;
import com.bdzjn.xml.repository.marklogic.ActRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActService {

    private final ActRepository actRepository;

    @Autowired
    public ActService(ActRepository actRepository) {
        this.actRepository = actRepository;
    }

    public Optional<Act> create(Act act) {
        act.setId(UUID.randomUUID().toString());
        return actRepository.save(act);
    }

    public Optional<Act> findById(String id) {
        return actRepository.findById(id);
    }

    public List<Act> findAll() {
        return actRepository.findAll();
    }

    public Optional<Act> update(Act act) {
        return actRepository.save(act);
    }

}
