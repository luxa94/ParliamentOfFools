package com.bdzjn.xml.controller;

import com.bdzjn.xml.controller.dto.VoteDTO;
import com.bdzjn.xml.controller.exception.NotFoundException;
import com.bdzjn.xml.controller.exception.UnprocessableEntityException;
import com.bdzjn.xml.model.act.Amendment;
import com.bdzjn.xml.service.ActService;
import com.bdzjn.xml.service.AmendmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/amendments")
public class AmendmentController {

    private final ActService actService;
    private final AmendmentService amendmentService;

    @Autowired
    public AmendmentController(ActService actService, AmendmentService amendmentService) {
        this.actService = actService;
        this.amendmentService = amendmentService;
    }

    @GetMapping(value = "{id}", produces = "application/xml")
    public ResponseEntity findOne(@PathVariable String id) {
        return null;
    }

    @PostMapping(value = "/act/{actId}")
    public ResponseEntity create(@RequestBody String rawAmendment, @PathVariable String actId) {
        actService.findById(actId).orElseThrow(NotFoundException::new);
        final Amendment newAmendment = amendmentService.create(rawAmendment, actId).orElseThrow(UnprocessableEntityException::new);
        return new ResponseEntity<>(newAmendment, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PRESIDENT')")
    @PutMapping("/{id}/vote")
    public ResponseEntity vote(@RequestBody VoteDTO voteDTO,
                               @PathVariable String id) {
        amendmentService.vote(id, voteDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
