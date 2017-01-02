package com.bdzjn.xml.controller;

import com.bdzjn.xml.controller.exception.NotFoundException;
import com.bdzjn.xml.controller.exception.UnprocessableEntityException;
import com.bdzjn.xml.model.User;
import com.bdzjn.xml.model.act.Act;
import com.bdzjn.xml.service.ActService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/acts")
public class ActController {

    private final ActService actService;

    @Autowired
    public ActController(ActService actService) {
        this.actService = actService;
    }

    @PreAuthorize("hasAnyAuthority('ALDERMAN', 'PRESIDENT')")
    @PostMapping
    public ResponseEntity create(@RequestBody Act act,
                                 @AuthenticationPrincipal User user) {
        act.setAuthorId(user.getId());
        final Act savedAct = actService.create(act).orElseThrow(UnprocessableEntityException::new);
        return new ResponseEntity<>(savedAct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity findAll() {
        final List<Act> acts = actService.findAll();
        return new ResponseEntity<>(acts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable String id) {
        final Act act = actService.findById(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(act, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PRESIDENT')")
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody Act act,
                                 @PathVariable String id) {
        act.setId(id);
        final Optional<Act> updatedAct = actService.update(act);
        return new ResponseEntity<>(updatedAct, HttpStatus.OK);
    }

}
