package com.bdzjn.xml.service;

import org.springframework.stereotype.Service;

import java.io.*;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;

import net.sf.saxon.TransformerFactoryImpl;

@Service
public class PdfService {

    private FopFactory fopFactory;

    private TransformerFactory transformerFactory;

    private static final String XSL_FILE = "src/main/resources/pdf/act_fo.xsl";

    public PdfService() throws SAXException, IOException {
        fopFactory = FopFactory.newInstance(new File("src/main/resources/fop.xconf"));
        transformerFactory = new TransformerFactoryImpl();
    }

    public ByteArrayOutputStream generatePDF(StringWriter inputFile) throws Exception {
        StreamSource transformSource = new StreamSource(XSL_FILE);

        // Initialize the transformation subject
        StreamSource source = new StreamSource(new ByteArrayInputStream(inputFile.getBuffer().toString().getBytes()));

        // Initialize user agent needed for the transformation
        FOUserAgent userAgent = fopFactory.newFOUserAgent();

        // Create the output stream to store the results
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        // Initialize the XSL-FO transformer object
        Transformer xslFoTransformer = transformerFactory.newTransformer(transformSource);

        // Construct FOP instance with desired output format
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, outStream);

        // Resulting SAX events
        Result res = new SAXResult(fop.getDefaultHandler());

        // Start XSLT transformation and FOP processing
        xslFoTransformer.transform(source, res);

        return outStream;
    }
}



