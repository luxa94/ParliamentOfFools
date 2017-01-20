package com.bdzjn.xml.repository.marklogic;

import com.bdzjn.xml.model.act.Amendment;
import com.bdzjn.xml.properties.MarkLogicConfiguration;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.JAXBHandle;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AmendmentRepository {

    public Optional<Amendment> save(Amendment amendment) {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);
        final XMLDocumentManager documentManager = client.newXMLDocumentManager();

        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Amendment.class);
            final JAXBHandle<Amendment> handle = new JAXBHandle<>(jaxbContext);
            handle.set(amendment);

            documentManager.write("/amendments/" + amendment.getId(), handle);

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

}
