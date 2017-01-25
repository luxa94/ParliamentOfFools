<xsl:stylesheet version="1.0"
                xmlns:a="http://www.fools.gov.rs/acts"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <xsl:for-each select="//amendment">
            <a href="#!/amendment/{@id}" alt="More information">
                <xsl:value-of select="@name"/>
            </a>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>