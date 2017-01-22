package com.bdzjn.xml.repository.marklogic;

import com.bdzjn.xml.model.act.Act;
import com.bdzjn.xml.properties.MarkLogicConfiguration;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.eval.EvalResultIterator;
import com.marklogic.client.eval.ServerEvaluationCall;
import com.marklogic.client.io.*;
import com.marklogic.client.semantics.GraphManager;
import com.marklogic.client.semantics.RDFMimeTypes;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ActRepository {

    public Optional<Act> save(Act act, ByteArrayOutputStream metadataResult) {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);
        final XMLDocumentManager documentManager = client.newXMLDocumentManager();

        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Act.class);
            final JAXBHandle<Act> handle = new JAXBHandle<>(jaxbContext);
            handle.set(act);

            documentManager.write("/acts/" + act.getId(), handle);


            final GraphManager graphManager = client.newGraphManager();
            graphManager.setDefaultMimetype(RDFMimeTypes.RDFXML);
            final String content = metadataResult.toString();

            final StringHandle stringHandle = new StringHandle(content).withMimetype(RDFMimeTypes.RDFXML);
            graphManager.merge("pof/act/metadata", stringHandle);

            client.release();
            return Optional.of(act);
        } catch (Exception e) {
            e.printStackTrace();
            client.release();
            return Optional.empty();
        }
    }

    public Optional<Act> findById(String id) {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);
        final XMLDocumentManager documentManager = client.newXMLDocumentManager();

        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Act.class);
            final JAXBHandle<Act> handle = new JAXBHandle<>(jaxbContext);
            documentManager.read("/acts/" + id, handle);
            final Act act = handle.get();

            client.release();
            return Optional.of(act);
        } catch (Exception e) {
            e.printStackTrace();
            client.release();
            return Optional.empty();
        }
    }

    public List<Act> findAll() {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);
        final XMLDocumentManager documentManager = client.newXMLDocumentManager();

        ServerEvaluationCall call = client.newServerEval();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        call.xquery("declare namespace a = \"http://www.fools.gov.rs/acts\";\n//a:act");

        final List<Act> acts = new ArrayList<>();
        final EvalResultIterator eval = call.eval();

        eval.forEach(evalResult -> {
            final String s = evalResult.getAs(String.class);
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(s.getBytes(Charset.defaultCharset()));
            final Act act = JAXB.unmarshal(byteArrayInputStream, Act.class);

            acts.add(act);
        });

        return acts;
    }

}
