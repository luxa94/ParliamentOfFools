package com.bdzjn.xml.service;

import com.bdzjn.xml.properties.MarkLogicConfiguration;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.semantics.GraphManager;
import com.marklogic.client.semantics.RDFMimeTypes;
import org.springframework.stereotype.Service;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

@Service
public class ExportService {

    public String exportMetadataAs(String mimeType, Format format, String namedGraphUri, String outputFilePath) throws TransformerException, FileNotFoundException {
        final DatabaseClient client = DatabaseClientFactory.newClient(MarkLogicConfiguration.host,
                MarkLogicConfiguration.port, MarkLogicConfiguration.database, MarkLogicConfiguration.user,
                MarkLogicConfiguration.password, DatabaseClientFactory.Authentication.DIGEST);

        GraphManager graphManager = client.newGraphManager();
        String content = graphManager.read(namedGraphUri,
                new StringHandle().withMimetype(mimeType)).withFormat(format).get();
        client.release();

        return content;
    }
}
