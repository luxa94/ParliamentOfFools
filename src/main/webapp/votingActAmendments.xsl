<xsl:stylesheet version="1.0"
                xmlns:a="http://www.fools.gov.rs/acts"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <xsl:for-each select="//amendment">
            <p><a href="#!/amendmentVoting/{@id}" alt="More information">
                <xsl:value-of select="@name"/>
            </a></p><br/>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>