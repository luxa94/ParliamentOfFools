package com.bdzjn.xml.service;

import com.bdzjn.xml.model.User;
import com.bdzjn.xml.model.act.Act;
import com.bdzjn.xml.repository.marklogic.ActRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActService {

    private final ActRepository actRepository;

    @Autowired
    public ActService(ActRepository actRepository) {
        this.actRepository = actRepository;
    }

    public Optional<Act> create(Act act) {
        act.setId(UUID.randomUUID().toString());
        return actRepository.save(act);
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

            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
            final StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            final Act act = JAXB.unmarshal(new StringReader(writer.getBuffer().toString()), Act.class);
            act.setId(UUID.randomUUID().toString());
            act.setAuthorId(user.getId());
            act.setDate(new Date());

            return actRepository.save(act);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void populateIdFor(String tagName, Document doc) {
        System.out.println(doc.getNamespaceURI());
        final Element documentElement = doc.getDocumentElement();
        System.out.println(documentElement.getNamespaceURI());
        System.out.println(documentElement.getTagName());
        final NodeList list = documentElement.getElementsByTagName(tagName);
        System.out.println(list.getLength());
        for(int i=0; i<list.getLength(); i++) {
            ((Element)list.item(i)).setAttribute("id", Integer.toString(i));
        }
    }

    public Optional<Act> findById(String id) {
        return actRepository.findById(id);
    }

    public List<Act> findAll() {
        return actRepository.findAll();
    }

    public Optional<Act> update(Act act) {
        return actRepository.save(act);
    }
}
