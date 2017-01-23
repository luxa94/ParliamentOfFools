package com.bdzjn.xml.util;


import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MetadataExtractor {

    public static ByteArrayOutputStream extractMetadata(String xml, String xslPath) throws Exception {
        StreamSource transformSource = new StreamSource(xslPath);
        StreamSource source = new StreamSource(new ByteArrayInputStream(xml.getBytes()));
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer(transformSource);

        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        final StreamResult outputTarget = new StreamResult(byteArrayOutputStream);

        transformer.transform(source, outputTarget);

        return byteArrayOutputStream;
    }

}
