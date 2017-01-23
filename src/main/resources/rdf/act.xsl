<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:a="http://www.fools.gov.rs/acts"
                xmlns:predicate="http://www.fools.gov.rs/acts/"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">

    <!--<xsl:template match="/">-->
        <!--<rdf:RDF>-->
            <!--<rdf:Description rdf:about="http://www.example.com/xml">-->
                <!--<xsl:apply-templates/>-->
            <!--</rdf:Description>-->
        <!--</rdf:RDF>-->
    <!--</xsl:template>-->

    <xsl:template match="/a:act">
        <rdf:RDF>
            <rdf:Description rdf:about="http://www.fools.gov.rs/acts/{@id}">
                <predicate:authorId><xsl:value-of select="@authorId"/></predicate:authorId>
                <predicate:title><xsl:value-of select="@title"/></predicate:title>
                <predicate:country><xsl:value-of select="@country"/></predicate:country>
                <predicate:region><xsl:value-of select="@region"/></predicate:region>
                <predicate:establishment><xsl:value-of select="@establishment"/></predicate:establishment>
                <predicate:serial><xsl:value-of select="@serial"/></predicate:serial>
                <predicate:date><xsl:value-of select="@date"/></predicate:date>
                <predicate:city><xsl:value-of select="@city"/></predicate:city>
                <predicate:status><xsl:value-of select="@status"/></predicate:status>
            </rdf:Description>
        </rdf:RDF>
    </xsl:template>

</xsl:stylesheet>