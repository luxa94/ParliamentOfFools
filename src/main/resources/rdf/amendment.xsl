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

    <xsl:template match="/a:amendment">
        <rdf:RDF>
            <rdf:Description rdf:about="http://www.fools.gov.rs/acts/{@id}">
                <predicate:date><xsl:value-of select="@date"/></predicate:date>
                <predicate:name><xsl:value-of select="@name"/></predicate:name>
                <predicate:actId><xsl:value-of select="@actId"/></predicate:actId>
                <predicate:status><xsl:value-of select="@status"/></predicate:status>
            </rdf:Description>
        </rdf:RDF>
    </xsl:template>

</xsl:stylesheet>