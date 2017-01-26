package com.bdzjn.xml.service;


import com.bdzjn.xml.controller.dto.VoteDTO;
import com.bdzjn.xml.controller.exception.NotFoundException;
import com.bdzjn.xml.model.act.Act;
import com.bdzjn.xml.model.act.Amendment;
import com.bdzjn.xml.repository.marklogic.ActRepository;
import com.bdzjn.xml.repository.marklogic.AmendmentRepository;
import com.bdzjn.xml.util.MetadataExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.UUID;

@Service
public class AmendmentService {

    private static final String RDF_XSL = "src/main/resources/rdf/amendment.xsl";

    private final ActRepository actRepository;
    private final AmendmentRepository amendmentRepository;

    @Autowired
    public AmendmentService(ActRepository actRepository, AmendmentRepository amendmentRepository) {
        this.actRepository = actRepository;
        this.amendmentRepository = amendmentRepository;
    }

    public Optional<Amendment> create(String rawAmendment, String actId) {
        try {
            final Act act = actRepository.findById(actId).orElseThrow(NotFoundException::new);
            final DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawAmendment.getBytes(Charset.defaultCharset()));
            final Document doc = documentBuilder.parse(byteArrayInputStream);

            doc.getDocumentElement().setAttribute("id", UUID.randomUUID().toString());
            doc.getDocumentElement().setAttribute("actId", actId);
            doc.getDocumentElement().setAttribute("actName", act.getTitle());

            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
            final StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            final String newXml = writer.getBuffer().toString();

            final ByteArrayOutputStream metadataStream = MetadataExtractor.extractMetadata(newXml, RDF_XSL);
            final Amendment amendment = JAXB.unmarshal(new StringReader(newXml), Amendment.class);

            return amendmentRepository.save(amendment, metadataStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void vote(String id, VoteDTO voteDTO) {
        String actStatus;
        if (voteDTO.getNumberOfFor() > voteDTO.getNumberAgainst()) {
            actStatus = "ACCEPTED";
        } else {
            actStatus = "REJECTED";
        }

        amendmentRepository.updateAmendmentStatus(id, actStatus);
    }

    public Optional<Amendment> findById(String amendmentId) {
        return amendmentRepository.findById(amendmentId);
    }
}
