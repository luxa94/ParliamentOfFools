package com.bdzjn.xml.service;

import com.bdzjn.xml.model.User;
import com.bdzjn.xml.model.act.Act;
import com.bdzjn.xml.model.act.DocumentStatus;
import com.bdzjn.xml.repository.marklogic.ActRepository;
import com.bdzjn.xml.util.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActService {

    public static final String RDF_XSL = "src/main/resources/rdf/act.xsl";

    private final ActRepository actRepository;

    @Autowired
    public ActService(ActRepository actRepository) {
        this.actRepository = actRepository;
    }

    public Optional<Act> create(String rawAct, User user) {
        try {
            final DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawAct.getBytes(Charset.defaultCharset()));
            final Document doc = documentBuilder.parse(byteArrayInputStream);

            populateIdFor("article", doc);
            populateIdFor("paragraph", doc);
            populateIdFor("item", doc);
            populateIdFor("subItem", doc);
            populateIdFor("ident", doc);

            doc.getDocumentElement().setAttribute("id", UUID.randomUUID().toString());
            doc.getDocumentElement().setAttribute("status", DocumentStatus.PENDING.name());
            doc.getDocumentElement().setAttribute("authorId", String.valueOf(user.getId()));
            doc.getDocumentElement().setAttribute("date", DateConverter.printDate(new Date()));

            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
            final StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            final String newXml = writer.getBuffer().toString();

            final ByteArrayOutputStream metadataResult = generateMetadata(newXml);

            final Act act = JAXB.unmarshal(new StringReader(newXml), Act.class);

            return actRepository.save(act, metadataResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private ByteArrayOutputStream generateMetadata(String newXml) throws Exception {
        StreamSource transformSource = new StreamSource(RDF_XSL);
        StreamSource source = new StreamSource(new ByteArrayInputStream(newXml.getBytes()));
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer(transformSource);

        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        final StreamResult outputTarget = new StreamResult(byteArrayOutputStream);

        transformer.transform(source, outputTarget);

        return byteArrayOutputStream;
    }

    private void populateIdFor(String tagName, Document doc) {
        final NodeList list = doc.getDocumentElement().getElementsByTagName(tagName);
        for(int i=0; i<list.getLength(); i++) {
            ((Element)list.item(i)).setAttribute("id", Integer.toString(i));
        }
    }

    public Optional<Act> findById(String id) {
        return actRepository.findById(id);
    }

    public List<Act> findAll(String term) {
        if (term.isEmpty())
            return actRepository.findAll();
        else {
            return actRepository.findByTerm(term);
        }
    }

    public Optional<Act> update(Act act) {
        return actRepository.save(act, new ByteArrayOutputStream());
    }
}
