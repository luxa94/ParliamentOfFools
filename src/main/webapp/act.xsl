<xsl:stylesheet version="1.0"
                xmlns:a="http://www.fools.gov.rs/acts"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="renderArticle">
        <xsl:for-each select="a:article">
            <h6>Član <xsl:value-of select="@id"/></h6>
            <xsl:for-each select="a:paragraph">
                <xsl:for-each select="a:text/a:contentItem">
                    <p style="padding-left: 8em; padding-right: 8em; margin: 0"><xsl:copy-of select="."/></p>
                </xsl:for-each>
                <xsl:for-each select="a:item">
                    <p style="text-align: start; padding-left: 15em; padding-right: 15em; margin: 0" ><xsl:value-of select="position()"/>)
                        <xsl:for-each select="a:text/a:contentItem">
                            <span><xsl:copy-of select="."/></span>
                        </xsl:for-each>
                    </p>
                    <xsl:for-each select="a:subItem">
                        <p style="text-align: start; padding-left: 18em; padding-right: 18em; margin: 0">(<xsl:value-of select="position()"/>)
                            <xsl:for-each select="a:text/a:contentItem">
                                <span><xsl:copy-of select="."/></span>
                            </xsl:for-each>
                        </p>
                        <xsl:for-each select="a:ident">
                        <p style="text-align: start; padding-left: 20em; padding-right: 20em; margin: 0">-<xsl:copy-of select="a:contentItem"/></p>
                        </xsl:for-each>
                    </xsl:for-each>
                </xsl:for-each>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name = "renderHead">
        <xsl:for-each select="a:head">
            <h3>Glava <xsl:value-of select="position()"/></h3>
            <xsl:call-template name="renderArticle" />
            <xsl:for-each select="a:section">
                <h4>Odeljak <xsl:value-of select="position()"/></h4>
                <xsl:call-template name="renderArticle" />
                <xsl:for-each select="a:subsection">
                    <h5>Pododeljak <xsl:value-of select="position()"/></h5>
                    <xsl:call-template name="renderArticle" />
                </xsl:for-each>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="/a:act">

        <xsl:for-each select="a:preamble/a:contentItem">
            <p style="text-align: start;"><xsl:copy-of select="."/></p>
        </xsl:for-each>

        <h1 style="font-size: 3em;"><xsl:value-of select="@title"/></h1>

        <xsl:call-template name="renderArticle" />
        <xsl:call-template name="renderHead" />
        <xsl:for-each select="a:part">
            <h2>Deo <xsl:value-of select="position()"/></h2>
            <xsl:call-template name="renderArticle" />
            <xsl:call-template name="renderHead" />
        </xsl:for-each>

    </xsl:template>

</xsl:stylesheet>