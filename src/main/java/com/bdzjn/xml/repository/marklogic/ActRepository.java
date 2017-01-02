package com.bdzjn.xml.repository.marklogic;

import com.bdzjn.xml.model.act.Act;
import com.bdzjn.xml.properties.MarkLogicConfiguration;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.eval.ServerEvaluationCall;
import com.marklogic.client.io.JAXBHandle;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ActRepository {

    public Optional<Act> save(Act act) {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);
        final XMLDocumentManager documentManager = client.newXMLDocumentManager();

        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Act.class);
            final JAXBHandle<Act> handle = new JAXBHandle<>(jaxbContext);
            handle.set(act);

            documentManager.write("/acts/" + act.getId(), handle);

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

        call.xquery("declare namespace u = \"http://www.fools.gov.rs/users\";\n//u:user");

        final List<Act> acts = new ArrayList<>();
        call.eval().forEach(evalResult -> {
            final Act act = evalResult.getAs(Act.class);
            acts.add(act);
        });

        return acts;
    }

}
