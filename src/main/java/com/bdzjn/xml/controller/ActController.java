package com.bdzjn.xml.controller;

import com.bdzjn.xml.controller.dto.VoteDTO;
import com.bdzjn.xml.controller.exception.NotFoundException;
import com.bdzjn.xml.controller.exception.UnprocessableEntityException;
import com.bdzjn.xml.model.User;
import com.bdzjn.xml.model.act.Act;
import com.bdzjn.xml.model.act.Amendment;
import com.bdzjn.xml.model.act.DocumentStatus;
import com.bdzjn.xml.model.act.wrapper.ActWrapper;
import com.bdzjn.xml.model.act.wrapper.AmendmentWrapper;
import com.bdzjn.xml.service.ActService;
import com.bdzjn.xml.service.ExportService;
import com.bdzjn.xml.service.PdfService;
import com.marklogic.client.io.Format;
import com.marklogic.client.semantics.RDFMimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/acts")
public class ActController {

    private final ActService actService;

    private final PdfService pdfService;

    private final ExportService exportService;

    @Autowired
    public ActController(ActService actService, PdfService pdfService, ExportService exportService) {
        this.actService = actService;
        this.pdfService = pdfService;
        this.exportService = exportService;
    }

    @PreAuthorize("hasAnyAuthority('ALDERMAN', 'PRESIDENT')")
    @PostMapping
    public ResponseEntity create(@RequestBody String rawAct,
                                 @AuthenticationPrincipal User user) {
        final Act savedAct = actService.create(rawAct, user).orElseThrow(UnprocessableEntityException::new);
        return new ResponseEntity<>(savedAct, HttpStatus.CREATED);
    }

    @GetMapping(produces = "application/xml")
    public ResponseEntity findAll(@RequestParam(required = false, defaultValue = "") String term,
                                  @RequestParam(required = false, defaultValue = "") String text) {
        final List<Act> acts = actService.findAll(term, text);
        final ActWrapper actWrapper = new ActWrapper(acts);
        return new ResponseEntity<>(actWrapper, HttpStatus.OK);
    }

    @GetMapping(path = "/pending", produces = "application/xml")
    public ResponseEntity findPending() {
        final List<Act> pendingActs = actService.findAll().stream().filter(act -> act.getStatus() == DocumentStatus.PENDING).collect(Collectors.toList());
        final ActWrapper actWrapper = new ActWrapper(pendingActs);
        return new ResponseEntity<>(actWrapper, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/xml")
    public ResponseEntity findOne(@PathVariable String id) {
        final Act act = actService.findById(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(act, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PRESIDENT')")
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody Act act,
                                 @PathVariable String id) {
        act.setId(id);
        final Act updatedAct = actService.update(act).orElseThrow(UnprocessableEntityException::new);
        return new ResponseEntity<>(updatedAct, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('PRESIDENT')")
    @PutMapping("/{id}/vote")
    public ResponseEntity vote(@RequestBody VoteDTO voteDTO,
                               @PathVariable String id) {
        String status = actService.vote(id, voteDTO);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping(value = "/pdf/{actId}", produces = "application/pdf")
    public ResponseEntity downloadPdf(@PathVariable String actId) throws Exception {

        final Act act = actService.findById(actId).orElseThrow(NotFoundException::new);

        StringWriter stringWriter = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(Act.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(act, stringWriter);

        final ByteArrayOutputStream byteArrayOutputStream = pdfService.generatePDF(stringWriter);
        final byte[] bytes = byteArrayOutputStream.toByteArray();

        return ResponseEntity
                .ok()
                .header("Content-Disposition", "attachment; filename=\"quot.pdf\";")
                .contentLength(bytes.length)
                .contentType(
                        MediaType.parseMediaType("application/pdf"))
                .body(bytes);
    }

    @GetMapping(value = "/export/rdf")
    public ResponseEntity exportMetadataAsRdf() throws TransformerException, FileNotFoundException {
        final String metadata = exportService.exportMetadataAs(RDFMimeTypes.RDFXML, Format.XML, "pof/act/metadata");
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

    @GetMapping(value = "/export/json")
    public ResponseEntity exportMetadataAsJson() throws TransformerException, FileNotFoundException {
        final String metadata = exportService.exportMetadataAs(RDFMimeTypes.RDFJSON, Format.JSON, "pof/act/metadata");
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/amendments", produces = "application/xml")
    public ResponseEntity findAmendments(@PathVariable String id) {
        final List<Amendment> amendments = actService.findAmendments(id);
        final AmendmentWrapper amendmentWrapper = new AmendmentWrapper(amendments);
        return new ResponseEntity<>(amendmentWrapper, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/amendments/pending", produces = "application/xml")
    public ResponseEntity findPendingAmendments(@PathVariable String id) {
        final List<Amendment> amendments = actService.findAmendments(id).stream().filter(amendment -> amendment.getStatus() == DocumentStatus.PENDING).collect(Collectors.toList());
        final AmendmentWrapper amendmentWrapper = new AmendmentWrapper(amendments);
        return new ResponseEntity<>(amendmentWrapper, HttpStatus.OK);
    }

}
