//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.23 at 09:34:14 PM CET 
//


package com.bdzjn.xml.model.act;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

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
