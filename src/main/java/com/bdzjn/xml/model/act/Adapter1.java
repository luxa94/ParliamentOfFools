//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.20 at 12:34:54 PM CET 
//


package com.bdzjn.xml.model.act;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (com.bdzjn.xml.util.DateConverter.parseDate(value));
    }

    public String marshal(Date value) {
        return (com.bdzjn.xml.util.DateConverter.printDate(value));
    }

}
