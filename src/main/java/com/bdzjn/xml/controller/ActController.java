package com.bdzjn.xml.controller;

import com.bdzjn.xml.controller.dto.VoteDTO;
import com.bdzjn.xml.controller.exception.NotFoundException;
import com.bdzjn.xml.controller.exception.UnprocessableEntityException;
import com.bdzjn.xml.model.User;
import com.bdzjn.xml.model.act.Act;
import com.bdzjn.xml.model.act.wrapper.ActWrapper;
import com.bdzjn.xml.service.ActService;
import com.bdzjn.xml.service.ExportService;
import com.bdzjn.xml.service.PdfService;
import com.marklogic.client.io.Format;
import com.marklogic.client.semantics.RDFMimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.List;

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
                                  @RequestParam(required = false, defaultValue = "") String text) throws TransformerException {
        final List<Act> acts = actService.findAll(term, text);
        final ActWrapper actWrapper = new ActWrapper(acts);
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
                               @PathVariable long id) {
        return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
    }

    @GetMapping(value = "/pdf/{actId}", produces = "application/pdf")
    public ResponseEntity downloadPdf(@PathVariable String actId) throws Exception {

        final Act act = actService.findById(actId).orElseThrow(NotFoundException::new);

        String pathToXmlFile = "src/main/resources/pdf/" + actId + ".xml";

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
        final String metadata = exportService.exportMetadataAs(RDFMimeTypes.RDFXML, Format.XML,"pof/act/metadata", "src/main/resources/export/act_metadata.rdf");
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

    @GetMapping(value="/export/json")
    public ResponseEntity exportMetadataAsJson() throws TransformerException, FileNotFoundException {
        final String metadata = exportService.exportMetadataAs(RDFMimeTypes.RDFJSON, Format.JSON, "pof/act/metadata", "src/main/resources/export/act_metadata.json");
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

}
