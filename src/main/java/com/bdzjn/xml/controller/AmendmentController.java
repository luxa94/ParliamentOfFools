package com.bdzjn.xml.controller;

import com.bdzjn.xml.controller.dto.VoteDTO;
import com.bdzjn.xml.controller.exception.NotFoundException;
import com.bdzjn.xml.controller.exception.UnprocessableEntityException;
import com.bdzjn.xml.model.act.Amendment;
import com.bdzjn.xml.service.ActService;
import com.bdzjn.xml.service.AmendmentService;
import com.bdzjn.xml.service.ExportService;
import com.bdzjn.xml.service.PdfService;
import com.marklogic.client.io.Format;
import com.marklogic.client.semantics.RDFMimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXB;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;

@RestController
@RequestMapping("/api/amendments")
public class AmendmentController {

    private final PdfService pdfService;
    private final ActService actService;
    private final ExportService exportService;
    private final AmendmentService amendmentService;

    @Autowired
    public AmendmentController(PdfService pdfService, ActService actService, ExportService exportService, AmendmentService amendmentService) {
        this.pdfService = pdfService;
        this.actService = actService;
        this.exportService = exportService;
        this.amendmentService = amendmentService;
    }

    @GetMapping(value = "{id}", produces = "application/xml")
    public ResponseEntity findOne(@PathVariable String id) {
        final Amendment amendment = amendmentService.findById(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(amendment, HttpStatus.OK);
    }

    @GetMapping(value = "/pdf/{id}", produces = "application/pdf")
    public ResponseEntity downloadPdf(@PathVariable String id) throws Exception {
        final Amendment amendment = amendmentService.findById(id).orElseThrow(NotFoundException::new);
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(amendment, stringWriter);

        final ByteArrayOutputStream byteArrayOutputStream = pdfService.generateAmendmentPDF(stringWriter);
        final byte[] bytes = byteArrayOutputStream.toByteArray();

        return ResponseEntity
                .ok()
                .header("Content-Disposition", "attachment; filename=\"quot.pdf\";")
                .contentLength(bytes.length)
                .contentType(
                        MediaType.parseMediaType("application/pdf"))
                .body(bytes);
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
