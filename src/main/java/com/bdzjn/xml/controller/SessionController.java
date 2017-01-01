package com.bdzjn.xml.controller;

import com.bdzjn.xml.controller.dto.SessionDTO;
import com.bdzjn.xml.controller.exception.NotFoundException;
import com.bdzjn.xml.model.Session;
import com.bdzjn.xml.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PreAuthorize("hasAnyAuthority('PRESIDENT')")
    @Transactional
    @PostMapping
    public ResponseEntity create(@RequestBody SessionDTO sessionDTO) {
        final Session savedSession = sessionService.create(sessionDTO);
        return new ResponseEntity<>(savedSession, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable long id) {
        final Session session = sessionService.findById(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @GetMapping("/active")
    public ResponseEntity findActive() {
        final Session session = sessionService.findOpenSession().orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PRESIDENT')")
    @Transactional
    @PutMapping
    public ResponseEntity edit(@RequestBody SessionDTO sessionDTO) {
        sessionService.edit(sessionDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PRESIDENT')")
    @Transactional
    @DeleteMapping
    public ResponseEntity delete() {
        sessionService.delete();
        return new ResponseEntity(HttpStatus.OK);
    }

}
