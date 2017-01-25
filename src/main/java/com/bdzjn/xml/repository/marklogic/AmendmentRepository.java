package com.bdzjn.xml.repository.marklogic;

import com.bdzjn.xml.model.act.Act;
import com.bdzjn.xml.model.act.Amendment;
import com.bdzjn.xml.properties.MarkLogicConfiguration;
import com.bdzjn.xml.util.RdfTripleUpdate;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.DocumentPatchBuilder;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.eval.EvalResultIterator;
import com.marklogic.client.eval.ServerEvaluationCall;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.io.marker.DocumentPatchHandle;
import com.marklogic.client.semantics.GraphManager;
import com.marklogic.client.semantics.RDFMimeTypes;
import com.marklogic.client.semantics.SPARQLQueryDefinition;
import com.marklogic.client.semantics.SPARQLQueryManager;
import com.marklogic.client.util.EditableNamespaceContext;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AmendmentRepository {

    private static final String AMENDMENTS_COLLECTION = "fools/amendments/collection";

    public Optional<Amendment> save(Amendment amendment, ByteArrayOutputStream metadataStream) {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);
        final XMLDocumentManager documentManager = client.newXMLDocumentManager();

        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Amendment.class);
            final JAXBHandle<Amendment> handle = new JAXBHandle<>(jaxbContext);
            handle.set(amendment);

            final DocumentMetadataHandle documentMetadataHandle = new DocumentMetadataHandle();
            documentMetadataHandle.getCollections().add(AMENDMENTS_COLLECTION);

            documentManager.write("/amendments/" + amendment.getId(), documentMetadataHandle, handle);

            final GraphManager graphManager = client.newGraphManager();
            final String content = metadataStream.toString();

            final StringHandle stringHandle = new StringHandle(content).withMimetype(RDFMimeTypes.RDFXML);
            graphManager.merge("pof/amendments/metadata", stringHandle);

            client.release();
            return Optional.of(amendment);
        } catch (JAXBException e) {
            e.printStackTrace();
            client.release();
            return Optional.empty();
        }
    }

    public Optional<Amendment> findById(String id) {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);
        final XMLDocumentManager documentManager = client.newXMLDocumentManager();

        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Amendment.class);
            final JAXBHandle<Amendment> handle = new JAXBHandle<>(jaxbContext);
            documentManager.read("/amendments/" + id, handle);
            final Amendment act = handle.get();

            client.release();
            return Optional.of(act);
        } catch (Exception e) {
            e.printStackTrace();
            client.release();
            return Optional.empty();
        }
    }

    public List<Amendment> findForAct(String actId) {
        return new ArrayList<>();
    }

    public void updateAmendmentStatus(String amendmentId, String newStatus) {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);

        XMLDocumentManager xmlManager = client.newXMLDocumentManager();

        EditableNamespaceContext namespaces = new EditableNamespaceContext();
        namespaces.put("a", "http://www.fools.gov.rs/acts");
        namespaces.put("fn", "http://www.w3.org/2005/xpath-functions");

        DocumentPatchBuilder patchBuilder = xmlManager.newPatchBuilder();
        patchBuilder.setNamespaces(namespaces);

        final String actStatusContextPath = "/a:amendment/@status";
        patchBuilder.replaceValue(actStatusContextPath, newStatus);

        DocumentPatchHandle patchHandle = patchBuilder.build();

        xmlManager.patch("/amendments/" + amendmentId, patchHandle);
        SPARQLQueryManager sqm = client.newSPARQLQueryManager();

        SPARQLQueryManager sparqlQueryManager = client.newSPARQLQueryManager();

        String subject = "http://www.fools.gov.rs/amendments/" + amendmentId;
        String predicate = "http://www.fools.gov.rs/amendments/status";
        RdfTripleUpdate.updateTriple(sparqlQueryManager, "pof/amendments/metadata", subject, predicate, newStatus);


        client.release();
    }

    public List<Amendment> findByActId(String actId) {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);
        final ServerEvaluationCall call = client.newServerEval();

        call.xquery("declare namespace a = \"http://www.fools.gov.rs/acts\";\n" +
                "/a:amendment[@actId='" + actId + "']");

        final List<Amendment> amendments = new ArrayList<>();
        final EvalResultIterator eval = call.eval();

        eval.forEach(evalResult -> {
            final String s = evalResult.getAs(String.class);
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(s.getBytes(Charset.defaultCharset()));
            final Amendment act = JAXB.unmarshal(byteArrayInputStream, Amendment.class);

            amendments.add(act);
        });

        return amendments;
    }
}
