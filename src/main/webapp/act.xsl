<xsl:stylesheet version="1.0"  xmlns:a="http://www.fools.gov.rs/acts"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/a:act">
        <h3><xsl:value-of select="@title"/></h3>
    </xsl:template>

</xsl:stylesheet>