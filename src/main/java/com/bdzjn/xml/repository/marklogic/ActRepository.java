package com.bdzjn.xml.repository.marklogic;

import com.bdzjn.xml.model.act.Act;
import com.bdzjn.xml.properties.MarkLogicConfiguration;
import com.bdzjn.xml.util.RdfTripleUpdate;
import com.fasterxml.jackson.databind.JsonNode;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.DocumentPatchBuilder;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.eval.EvalResultIterator;
import com.marklogic.client.eval.ServerEvaluationCall;
import com.marklogic.client.io.*;
import com.marklogic.client.io.marker.DocumentPatchHandle;
import com.marklogic.client.query.*;
import com.marklogic.client.semantics.GraphManager;
import com.marklogic.client.semantics.RDFMimeTypes;
import com.marklogic.client.util.EditableNamespaceContext;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.semantics.SPARQLMimeTypes;
import com.marklogic.client.semantics.SPARQLQueryDefinition;
import com.marklogic.client.semantics.SPARQLQueryManager;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
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

    public static final String ACTS_COLLECTION = "fools/acts/collection";

    public Optional<Act> save(Act act, ByteArrayOutputStream metadataResult) {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);
        final XMLDocumentManager documentManager = client.newXMLDocumentManager();

        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Act.class);
            final JAXBHandle<Act> handle = new JAXBHandle<>(jaxbContext);
            handle.set(act);

            final DocumentMetadataHandle documentMetadataHandle = new DocumentMetadataHandle();
            documentMetadataHandle.getCollections().add(ACTS_COLLECTION);

            documentManager.write("/acts/" + act.getId(), documentMetadataHandle, handle);

            saveMetadata(metadataResult, client);

            client.release();
            return Optional.of(act);
        } catch (Exception e) {
            e.printStackTrace();
            client.release();
            return Optional.empty();
        }
    }

    private void saveMetadata(ByteArrayOutputStream metadataResult, DatabaseClient client) {
        final GraphManager graphManager = client.newGraphManager();
        final String content = metadataResult.toString();

        final StringHandle stringHandle = new StringHandle(content).withMimetype(RDFMimeTypes.RDFXML);
        graphManager.merge("pof/act/metadata", stringHandle);
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

    public List<Act> findByTerm(String term) {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);

        SPARQLQueryManager manager = client.newSPARQLQueryManager();

        final SPARQLQueryDefinition query = manager.newQueryDefinition("SELECT * FROM <pof/act/metadata> WHERE { ?s ?p \"" + term + "\"}");

        JacksonHandle jacksonHandle = new JacksonHandle();
        jacksonHandle.setMimetype(SPARQLMimeTypes.SPARQL_JSON);

        jacksonHandle = manager.executeSelect(query, jacksonHandle);

        final JsonNode tuples = jacksonHandle.get().path("results").path("bindings");

        final List<Act> acts = new ArrayList<>();
        tuples.forEach(node -> {
            final String uri = node.path("s").path("value").asText();
            final String[] tokens = uri.split("/");
            final String id = tokens[tokens.length - 1];
            findById(id).ifPresent(acts::add);
        });

        return acts;

    }

    public List<Act> findByText(String text) throws TransformerException {
        String[] tokens = text.split("\\s+");
        String criteria = tokens[0];

        for (int i = 1; i < tokens.length; i++) {
            criteria += " OR " + tokens[i];
        }

        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);

        QueryManager queryManager = client.newQueryManager();

        StructuredQueryBuilder qb = queryManager.newStructuredQueryBuilder();

//        StructuredQueryDefinition queryDefinition = qb

        //queryDefinition.setCriteria(criteria);

        //SearchHandle results = queryManager.search(queryDefinition, new SearchHandle());

//        MatchDocumentSummary matches[] = results.getMatchResults();
//        MatchDocumentSummary result;
//
//        final List<Act> acts = new ArrayList<>();
//        for (int i = 0; i < matches.length; i++) {
//            result = matches[i];
//
//            String uriOfAct = result.getUri().substring(6);
//            findById(uriOfAct).ifPresent(acts::add);
//        }

        client.release();

        return null;
    }


    public void updateActStatus(String actId, String newStatus) {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);

        XMLDocumentManager xmlManager = client.newXMLDocumentManager();

        EditableNamespaceContext namespaces = new EditableNamespaceContext();
        namespaces.put("a", "http://www.fools.gov.rs/acts");
        namespaces.put("fn", "http://www.w3.org/2005/xpath-functions");

        DocumentPatchBuilder patchBuilder = xmlManager.newPatchBuilder();
        patchBuilder.setNamespaces(namespaces);

        final String actStatusContextPath = "/a:act/@status";
        patchBuilder.replaceValue(actStatusContextPath, newStatus);

        DocumentPatchHandle patchHandle = patchBuilder.build();

        xmlManager.patch("/acts/" + actId, patchHandle);
        SPARQLQueryManager sparqlQueryManager = client.newSPARQLQueryManager();

        String subject = "http://www.fools.gov.rs/acts/" + actId;
        String predicate = "http://www.fools.gov.rs/acts/status";
        RdfTripleUpdate.updateTriple(sparqlQueryManager, "pof/act/metadata", subject, predicate, newStatus);

        client.release();
    }

    public static void updateRDFStringObject(String id, String resource, String predicate, String value, SPARQLQueryManager sparqlQueryManager) {

    }
}
