package com.bdzjn.xml.controller;

import com.bdzjn.xml.controller.exception.UnprocessableEntityException;
import com.bdzjn.xml.model.act.Amendment;
import com.bdzjn.xml.service.AmendmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/amendments")
public class AmendmentController {

    private final AmendmentService amendmentService;

    @Autowired
    public AmendmentController(AmendmentService amendmentService) {
        this.amendmentService = amendmentService;
    }

    @GetMapping(value = "{id}", produces = "application/xml")
    public ResponseEntity findOne(@PathVariable String id) {
        return null;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody String rawAmendment) {
        final Amendment newAmendment = amendmentService.create(rawAmendment).orElseThrow(UnprocessableEntityException::new);
        return new ResponseEntity<>(newAmendment, HttpStatus.OK);
    }
}
