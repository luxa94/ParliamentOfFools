package com.bdzjn.xml.controller;

import com.bdzjn.xml.controller.dto.VoteDTO;
import com.bdzjn.xml.controller.exception.NotFoundException;
import com.bdzjn.xml.controller.exception.UnprocessableEntityException;
import com.bdzjn.xml.model.act.Amendment;
import com.bdzjn.xml.service.ActService;
import com.bdzjn.xml.service.AmendmentService;
import com.bdzjn.xml.service.ExportService;
import com.marklogic.client.io.Format;
import com.marklogic.client.semantics.RDFMimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/amendments")
public class AmendmentController {

    private final ActService actService;
    private final ExportService exportService;
    private final AmendmentService amendmentService;

    @Autowired
    public AmendmentController(ActService actService, ExportService exportService, AmendmentService amendmentService) {
        this.actService = actService;
        this.exportService = exportService;
        this.amendmentService = amendmentService;
    }

    @GetMapping(value = "{id}", produces = "application/xml")
    public ResponseEntity findOne(@PathVariable String id) {
        final Amendment amendment = amendmentService.findById(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(amendment, HttpStatus.OK);
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

    @GetMapping(value = "/export/rdf")
    public ResponseEntity exportMetadataAsRdf() throws TransformerException, FileNotFoundException {
        final String metadata = exportService.exportMetadataAs(RDFMimeTypes.RDFXML, Format.XML, "pof/amendments/metadata");
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

    @GetMapping(value = "/export/json")
    public ResponseEntity exportMetadataAsJson() throws TransformerException, FileNotFoundException {
        final String metadata = exportService.exportMetadataAs(RDFMimeTypes.RDFJSON, Format.JSON, "pof/amendments/metadata");
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }
}
