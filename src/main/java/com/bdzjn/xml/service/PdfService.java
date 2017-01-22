package com.bdzjn.xml.service;

import org.springframework.stereotype.Service;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

    public static final String XSL_FILE = "src/main/resources/pdf/act_fo.xsl";

    public static final String OUTPUT_FILE = "src/main/resources/pdf/";


    public PdfService() throws SAXException, IOException {
        fopFactory = FopFactory.newInstance(new File("src/main/resources/fop.xconf"));
        transformerFactory = new TransformerFactoryImpl();
    }

    public String generatePDF(String inputFile) throws Exception {

        // Point to the XSL-FO file
        File xslFile = new File(XSL_FILE);

        // Create transformation source
        StreamSource transformSource = new StreamSource(XSL_FILE);

        // Initialize the transformation subject
        StreamSource source = new StreamSource(new File(inputFile));

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

        final String fullPathToPdfOutput = OUTPUT_FILE + "test.pdf";

        // Generate PDF file
        File pdfFile = new File(fullPathToPdfOutput);
        if (!pdfFile.getParentFile().exists()) {
            pdfFile.getParentFile().mkdir();
        }

        OutputStream out = new BufferedOutputStream(new FileOutputStream(pdfFile));
        out.write(outStream.toByteArray());

        out.close();

        return fullPathToPdfOutput;
    }
}



