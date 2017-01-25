//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.25 at 09:12:01 PM CET 
//


package com.bdzjn.xml.model.act;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bdzjn.xml.model.act package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ContentContentItem_QNAME = new QName("http://www.fools.gov.rs/acts", "contentItem");
    private final static QName _SubItemText_QNAME = new QName("http://www.fools.gov.rs/acts", "text");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bdzjn.xml.model.act
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Content }
     * 
     */
    public Content createContent() {
        return new Content();
    }

    /**
     * Create an instance of {@link Act }
     * 
     */
    public Act createAct() {
        return new Act();
    }

    /**
     * Create an instance of {@link Part }
     * 
     */
    public Part createPart() {
        return new Part();
    }

    /**
     * Create an instance of {@link Head }
     * 
     */
    public Head createHead() {
        return new Head();
    }

    /**
     * Create an instance of {@link Article }
     * 
     */
    public Article createArticle() {
        return new Article();
    }

    /**
     * Create an instance of {@link Paragraph }
     * 
     */
    public Paragraph createParagraph() {
        return new Paragraph();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link SubItem }
     * 
     */
    public SubItem createSubItem() {
        return new SubItem();
    }

    /**
     * Create an instance of {@link Ident }
     * 
     */
    public Ident createIdent() {
        return new Ident();
    }

    /**
     * Create an instance of {@link Content.ContentItem }
     * 
     */
    public Content.ContentItem createContentContentItem() {
        return new Content.ContentItem();
    }

    /**
     * Create an instance of {@link Section }
     * 
     */
    public Section createSection() {
        return new Section();
    }

    /**
     * Create an instance of {@link Subsection }
     * 
     */
    public Subsection createSubsection() {
        return new Subsection();
    }

    /**
     * Create an instance of {@link AmendmentItem }
     * 
     */
    public AmendmentItem createAmendmentItem() {
        return new AmendmentItem();
    }

    /**
     * Create an instance of {@link Amendment }
     * 
     */
    public Amendment createAmendment() {
        return new Amendment();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Content.ContentItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.fools.gov.rs/acts", name = "contentItem", scope = Content.class)
    public JAXBElement<Content.ContentItem> createContentContentItem(Content.ContentItem value) {
        return new JAXBElement<Content.ContentItem>(_ContentContentItem_QNAME, Content.ContentItem.class, Content.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Content }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.fools.gov.rs/acts", name = "text", scope = SubItem.class)
    public JAXBElement<Content> createSubItemText(Content value) {
        return new JAXBElement<Content>(_SubItemText_QNAME, Content.class, SubItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Content }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.fools.gov.rs/acts", name = "text", scope = Item.class)
    public JAXBElement<Content> createItemText(Content value) {
        return new JAXBElement<Content>(_SubItemText_QNAME, Content.class, Item.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Content }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.fools.gov.rs/acts", name = "text", scope = Paragraph.class)
    public JAXBElement<Content> createParagraphText(Content value) {
        return new JAXBElement<Content>(_SubItemText_QNAME, Content.class, Paragraph.class, value);
    }

}
