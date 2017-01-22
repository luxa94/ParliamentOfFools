<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:a="http://www.fools.gov.rs/acts"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">

    <xsl:output encoding="UTF-8" indent="yes" method="xml"/>
    <xsl:template name="renderArticle">
        <xsl:for-each select="a:article">

            <fo:block font-weight="bold" text-align="center" font-size="20px">
                Član <xsl:value-of select="position()"/>
            </fo:block>

            <xsl:for-each select="a:paragraph">
                <xsl:for-each select="a:text/a:contentItem">

                    <fo:block font-family="sans-serif" font-size="12px" padding="10px" text-align="start">
                        <xsl:value-of select="."/>
                    </fo:block>

                </xsl:for-each>

                <xsl:for-each select="a:item">

                    <fo:block>
                        <xsl:value-of select="position()"/>)
                        <xsl:for-each select="a:text/a:contentItem">

                            <fo-block><xsl:value-of select="."/></fo-block>

                        </xsl:for-each>
                    </fo:block>
                    <!--<xsl:for-each select="a:subItem">-->
                    <!--<p>&nbsp;(<xsl:value-of select="position()"/>) <xsl:value-of select="a:text/a:contentItem"/></p>-->
                    <!--<xsl:for-each select="a:ident">-->
                    <!--<p>&nbsp;&nbsp;-(<xsl:value-of select="position()"/>) <xsl:value-of select="a:text/a:contentItem"/></p>-->
                    <!--</xsl:for-each>-->
                    <!--</xsl:for-each>-->
                </xsl:for-each>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="/a:act">

        <fo:root>

            <fo:layout-master-set>
                <fo:simple-page-master master-name="act-page">
                    <fo:region-body margin="0.75in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="act-page">
                <fo:flow flow-name="xsl-region-body">
                    <xsl:for-each select="a:preamble/a:contentItem">

                        <fo:block font-family="sans-serif" font-size="12px" padding="10px" text-align="start">
                            <xsl:value-of select="."/>
                        </fo:block>

                    </xsl:for-each>

                    <fo:block font-family="sans-serif" font-size="24px" text-align="center">
                        <xsl:value-of select="@title"/>
                    </fo:block>

                    <xsl:for-each select="a:part">

                        <fo:block font-weight="bold" text-align="center" font-size="20px">
                            Deo <xsl:value-of select="position()"/>
                        </fo:block>

                        <xsl:call-template name="renderArticle" />

                        <xsl:for-each select="a:head">

                            <fo:block font-weight="bold" text-align="center" font-size="18px">
                                Glava <xsl:value-of select="position()"/>
                            </fo:block>


                            <xsl:call-template name="renderArticle" />

                            <xsl:for-each select="a:section">

                                <fo:block text-align="center" font-size="16px">
                                    Odeljak <xsl:value-of select="position()"/>
                                </fo:block>

                                <xsl:call-template name="renderArticle" />

                                <xsl:for-each select="a:subsection">

                                    <fo:block text-align="center" font-size="14px">
                                        Pododeljak <xsl:value-of select="position()"/>
                                    </fo:block>

                                    <xsl:call-template name="renderArticle" />

                                </xsl:for-each>
                            </xsl:for-each>
                        </xsl:for-each>
                    </xsl:for-each>

                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>