<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:a="http://www.fools.gov.rs/acts"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="renderIdent">
        <xsl:for-each select="a:ident">
            <fo:block>-
                <xsl:copy-of select="a:contentItem"/>
            </fo:block>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="renderSubitem">
        <xsl:for-each select="a:subItem">
            <fo:block>(<xsl:value-of select="position()"/>)
                <xsl:for-each select="a:text/a:contentItem">
                    <fo:block>
                        <xsl:copy-of select="."/>
                    </fo:block>
                </xsl:for-each>
            </fo:block>
            <xsl:call-template name="renderIdent"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="renderItem">
        <xsl:for-each select="a:item">
            <fo:block><xsl:value-of select="position()"/>)
                <xsl:for-each select="a:text/a:contentItem">
                    <fo:block>
                        <xsl:copy-of select="."/>
                    </fo:block>
                </xsl:for-each>
            </fo:block>
            <xsl:call-template name="renderSubitem"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="renderParagraph">
        <xsl:for-each select="a:paragraph">
            <xsl:for-each select="a:text/a:contentItem">
                <fo:block>
                    <xsl:copy-of select="."/>
                </fo:block>
            </xsl:for-each>
            <xsl:call-template name="renderItem"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="renderArticle">
        <xsl:for-each select="a:article">
            <fo:block>Član
                <xsl:value-of select="@id"/>
            </fo:block>
            <xsl:call-template name="renderParagraph"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="/a:amendment">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="amendment-page">
                    <fo:region-body margin="0.75in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="amendment-page">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>Date:
                        <xsl:value-of select="@date"/>
                    </fo:block>
                    <fo:block>Amandman na akt:
                        <xsl:value-of select="@actName"/>
                    </fo:block>
                    <fo:block>
                        <xsl:value-of select="@name"/>
                    </fo:block>

                    <xsl:for-each select="a:amendmentItem">
                        <fo:block>Promena
                            <xsl:value-of select="position()"/>
                        </fo:block>

                        <xsl:choose>
                            <xsl:when test="@elementName = 'article'">
                                <xsl:choose>
                                    <xsl:when test="@type = 'INSERT'">
                                        <xsl:choose>
                                            <xsl:when test="@placement = 'BEFORE'">
                                                <fo:block>Pre člana
                                                    <xsl:value-of select="@elementId"/> dolazi novi član koji glasi
                                                </fo:block>
                                            </xsl:when>
                                            <xsl:when test="@placement = 'AFTER'">
                                                <fo:block>Pre člana
                                                    <xsl:value-of select="@elementId"/> dolazi novi član koji glasi
                                                </fo:block>
                                            </xsl:when>
                                        </xsl:choose>

                                        <fo:block font-style="italic">
                                            <xsl:call-template name="renderArticle"/>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:when test="@type = 'UPDATE'">
                                        <fo:block>Član
                                            <xsl:value-of select="@elementId"/> se menja i glasi
                                        </fo:block>
                                        <fo:block font-style="italic">
                                            <xsl:call-template name="renderArticle"/>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:when test="@type = 'DELETE'">
                                        <fo:block>Član
                                            <xsl:value-of select="@elementId"/> se briše
                                        </fo:block>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:when>

                            <xsl:when test="@elementName = 'paragraph'">
                                <xsl:choose>
                                    <xsl:when test="@type = 'INSERT'">
                                        <xsl:choose>
                                            <xsl:when test="@placement = 'BEFORE'">
                                                <fo:block>Pre stava sa id-em
                                                    <xsl:value-of select="@elementId"/> dolazi novi stav koji glasi
                                                </fo:block>
                                            </xsl:when>
                                            <xsl:when test="@placement = 'AFTER'">
                                                <fo:block>Pre stava sa id-em
                                                    <xsl:value-of select="@elementId"/> dolazi novi stav koji glasi
                                                </fo:block>
                                            </xsl:when>
                                        </xsl:choose>

                                        <fo:block font-style="italic">
                                            <xsl:call-template name="renderParagraph"/>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:when test="@type = 'UPDATE'">
                                        <fo:block>Stav sa id-em
                                            <xsl:value-of select="@elementId"/> se menja i glasi
                                        </fo:block>
                                        <fo:block font-style="italic">
                                            <xsl:call-template name="renderParagraph"/>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:when test="@type = 'DELETE'">
                                        <fo:block>Stav sa id-em
                                            <xsl:value-of select="@elementId"/> se briše
                                        </fo:block>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:when>

                            <xsl:when test="@elementName = 'item'">
                                <xsl:choose>
                                    <xsl:when test="@type = 'INSERT'">
                                        <xsl:choose>
                                            <xsl:when test="@placement = 'BEFORE'">
                                                <fo:block>Pre tačke sa id-em
                                                    <xsl:value-of select="@elementId"/> dolazi nova tačka koja glasi
                                                </fo:block>
                                            </xsl:when>
                                            <xsl:when test="@placement = 'AFTER'">
                                                <fo:block>Pre tačke sa id-em
                                                    <xsl:value-of select="@elementId"/> dolazi nova tačka koja glasi
                                                </fo:block>
                                            </xsl:when>
                                        </xsl:choose>

                                        <fo:block font-style="italic">
                                            <xsl:call-template name="renderItem"/>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:when test="@type = 'UPDATE'">
                                        <fo:block>Tačka sa id-em
                                            <xsl:value-of select="@elementId"/> se menja i glasi
                                        </fo:block>
                                        <fo:block font-style="italic">
                                            <xsl:call-template name="renderItem"/>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:when test="@type = 'DELETE'">
                                        <fo:block>Tačka sa id-em
                                            <xsl:value-of select="@elementId"/> se briše
                                        </fo:block>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:when>

                            <xsl:when test="@elementName = 'subItem'">
                                <xsl:choose>
                                    <xsl:when test="@type = 'INSERT'">
                                        <xsl:choose>
                                            <xsl:when test="@placement = 'BEFORE'">
                                                <fo:block>Pre podtačke sa id-em
                                                    <xsl:value-of select="@elementId"/> dolazi nova podtačka koja glasi
                                                </fo:block>
                                            </xsl:when>
                                            <xsl:when test="@placement = 'AFTER'">
                                                <fo:block>Pre podtačke sa id-em
                                                    <xsl:value-of select="@elementId"/> dolazi nova podtačka koja glasi
                                                </fo:block>
                                            </xsl:when>
                                        </xsl:choose>

                                        <fo:block font-style="italic">
                                            <xsl:call-template name="renderSubitem"/>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:when test="@type = 'UPDATE'">
                                        <fo:block>Podtačka sa id-em
                                            <xsl:value-of select="@elementId"/> se menja i glasi
                                        </fo:block>
                                        <fo:block font-style="italic">
                                            <xsl:call-template name="renderSubitem"/>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:when test="@type = 'DELETE'">
                                        <fo:block>Podtačka sa id-em
                                            <xsl:value-of select="@elementId"/> se briše
                                        </fo:block>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:when>

                            <xsl:when test="@elementName = 'ident'">
                                <xsl:choose>
                                    <xsl:when test="@type = 'INSERT'">
                                        <xsl:choose>
                                            <xsl:when test="@placement = 'BEFORE'">
                                                <fo:block>Pre tačke sa id-em
                                                    <xsl:value-of select="@elementId"/> dolazi nova tačka koja glasi
                                                </fo:block>
                                            </xsl:when>
                                            <xsl:when test="@placement = 'AFTER'">
                                                <fo:block>Pre tačke sa id-em
                                                    <xsl:value-of select="@elementId"/> dolazi nova tačka koja glasi
                                                </fo:block>
                                            </xsl:when>
                                        </xsl:choose>

                                        <fo:block font-style="italic">
                                            <xsl:call-template name="renderIdent"/>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:when test="@type = 'UPDATE'">
                                        <fo:block>Tačka sa id-em
                                            <xsl:value-of select="@elementId"/> se menja i glasi
                                        </fo:block>
                                        <fo:block font-style="italic">
                                            <xsl:call-template name="renderIdent"/>
                                        </fo:block>
                                    </xsl:when>
                                    <xsl:when test="@type = 'DELETE'">
                                        <fo:block>Tačka sa id-em
                                            <xsl:value-of select="@elementId"/> se briše
                                        </fo:block>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:for-each>

                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

</xsl:stylesheet>