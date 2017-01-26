<xsl:stylesheet version="1.0"
                xmlns:a="http://www.fools.gov.rs/acts"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="renderIdent">
        <xsl:for-each select="a:ident">
            <p style="text-align: start; padding-left: 20em; padding-right: 20em; margin: 0">-<xsl:copy-of select="a:contentItem"/></p>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="renderSubitem">
        <xsl:for-each select="a:subItem">
            <p style="text-align: start; padding-left: 18em; padding-right: 18em; margin: 0">(<xsl:value-of select="position()"/>)
                <xsl:for-each select="a:text/a:contentItem">
                    <span><xsl:copy-of select="."/></span>
                </xsl:for-each>
            </p>
            <xsl:call-template name="renderIdent"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="renderItem">
        <xsl:for-each select="a:item">
            <p style="text-align: start; padding-left: 15em; padding-right: 15em; margin: 0" ><xsl:value-of select="position()"/>)
                <xsl:for-each select="a:text/a:contentItem">
                    <span><xsl:copy-of select="."/></span>
                </xsl:for-each>
            </p>
            <xsl:call-template name="renderSubitem"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="renderParagraph">
        <xsl:for-each select="a:paragraph">
            <xsl:for-each select="a:text/a:contentItem">
                <p style="padding-left: 8em; padding-right: 8em; margin: 0"><xsl:copy-of select="."/></p>
            </xsl:for-each>
            <xsl:call-template name="renderItem"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="renderArticle">
        <xsl:for-each select="a:article">
            <h6>Član <xsl:value-of select="@id"/></h6>
            <xsl:call-template name="renderParagraph"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="/a:amendment">
        <p>Date: <xsl:value-of select="@date"/></p>
        <p>Amandman na akt: <a href="#!/actVoting/{@actId}"><xsl:value-of select="@actName"/></a></p>
        <h1 style="font-size: 3em;"><xsl:value-of select="@name"/></h1>

        <xsl:for-each select="a:amendmentItem">
            <h2>Promena <xsl:value-of select="position()"/></h2>

            <xsl:choose>
                <xsl:when test="@elementName = 'article'">
                    <xsl:choose>
                        <xsl:when test="@type = 'INSERT'">
                            <xsl:choose>
                                <xsl:when test="@placement = 'BEFORE'">
                                    <p>Pre člana <xsl:value-of select="@elementId"/> dolazi novi član koji glasi</p>
                                </xsl:when>
                                <xsl:when test="@placement = 'AFTER'">
                                    <p>Pre člana <xsl:value-of select="@elementId"/> dolazi novi član koji glasi</p>
                                </xsl:when>
                            </xsl:choose>

                            <em>
                                <xsl:call-template name="renderArticle" />
                            </em>
                        </xsl:when>
                        <xsl:when test="@type = 'UPDATE'">
                            <p>Član <xsl:value-of select="@elementId"/> se menja i glasi</p>
                            <em>
                                <xsl:call-template name="renderArticle" />
                            </em>
                        </xsl:when>
                        <xsl:when test="@type = 'DELETE'">
                            <p>Član <xsl:value-of select="@elementId"/> se briše</p>
                        </xsl:when>
                    </xsl:choose>
                </xsl:when>

                <xsl:when test="@elementName = 'paragraph'">
                    <xsl:choose>
                        <xsl:when test="@type = 'INSERT'">
                            <xsl:choose>
                                <xsl:when test="@placement = 'BEFORE'">
                                    <p>Pre stava sa id-em <xsl:value-of select="@elementId"/> dolazi novi stav koji glasi</p>
                                </xsl:when>
                                <xsl:when test="@placement = 'AFTER'">
                                    <p>Pre stava sa id-em <xsl:value-of select="@elementId"/> dolazi novi stav koji glasi</p>
                                </xsl:when>
                            </xsl:choose>

                            <em>
                                <xsl:call-template name="renderParagraph" />
                            </em>
                        </xsl:when>
                        <xsl:when test="@type = 'UPDATE'">
                            <p>Stav sa id-em <xsl:value-of select="@elementId"/> se menja i glasi</p>
                            <em>
                                <xsl:call-template name="renderParagraph" />
                            </em>
                        </xsl:when>
                        <xsl:when test="@type = 'DELETE'">
                            <p>Stav sa id-em <xsl:value-of select="@elementId"/> se briše</p>
                        </xsl:when>
                    </xsl:choose>
                </xsl:when>

            <xsl:when test="@elementName = 'item'">
                <xsl:choose>
                    <xsl:when test="@type = 'INSERT'">
                        <xsl:choose>
                            <xsl:when test="@placement = 'BEFORE'">
                                <p>Pre tačke sa id-em <xsl:value-of select="@elementId"/> dolazi nova tačka koja glasi</p>
                            </xsl:when>
                            <xsl:when test="@placement = 'AFTER'">
                                <p>Pre tačke sa id-em <xsl:value-of select="@elementId"/> dolazi nova tačka koja glasi</p>
                            </xsl:when>
                        </xsl:choose>

                        <em>
                            <xsl:call-template name="renderItem" />
                        </em>
                    </xsl:when>
                    <xsl:when test="@type = 'UPDATE'">
                        <p>Tačka sa id-em <xsl:value-of select="@elementId"/> se menja i glasi</p>
                        <em>
                            <xsl:call-template name="renderItem" />
                        </em>
                    </xsl:when>
                    <xsl:when test="@type = 'DELETE'">
                        <p>Tačka sa id-em <xsl:value-of select="@elementId"/> se briše</p>
                    </xsl:when>
                </xsl:choose>
            </xsl:when>

            <xsl:when test="@elementName = 'subItem'">
                <xsl:choose>
                    <xsl:when test="@type = 'INSERT'">
                        <xsl:choose>
                            <xsl:when test="@placement = 'BEFORE'">
                                <p>Pre podtačke sa id-em <xsl:value-of select="@elementId"/> dolazi nova podtačka koja glasi</p>
                            </xsl:when>
                            <xsl:when test="@placement = 'AFTER'">
                                <p>Pre podtačke sa id-em <xsl:value-of select="@elementId"/> dolazi nova podtačka koja glasi</p>
                            </xsl:when>
                        </xsl:choose>

                        <em>
                            <xsl:call-template name="renderSubitem" />
                        </em>
                    </xsl:when>
                    <xsl:when test="@type = 'UPDATE'">
                        <p>Podtačka sa id-em <xsl:value-of select="@elementId"/> se menja i glasi</p>
                        <em>
                            <xsl:call-template name="renderSubitem" />
                        </em>
                    </xsl:when>
                    <xsl:when test="@type = 'DELETE'">
                        <p>Podtačka sa id-em <xsl:value-of select="@elementId"/> se briše</p>
                    </xsl:when>
                </xsl:choose>
            </xsl:when>

            <xsl:when test="@elementName = 'ident'">
                <xsl:choose>
                    <xsl:when test="@type = 'INSERT'">
                        <xsl:choose>
                            <xsl:when test="@placement = 'BEFORE'">
                                <p>Pre tačke sa id-em <xsl:value-of select="@elementId"/> dolazi nova tačka koja glasi</p>
                            </xsl:when>
                            <xsl:when test="@placement = 'AFTER'">
                                <p>Pre tačke sa id-em <xsl:value-of select="@elementId"/> dolazi nova tačka koja glasi</p>
                            </xsl:when>
                        </xsl:choose>

                        <em>
                            <xsl:call-template name="renderIdent" />
                        </em>
                    </xsl:when>
                    <xsl:when test="@type = 'UPDATE'">
                        <p>Tačka sa id-em <xsl:value-of select="@elementId"/> se menja i glasi</p>
                        <em>
                            <xsl:call-template name="renderIdent" />
                        </em>
                    </xsl:when>
                    <xsl:when test="@type = 'DELETE'">
                        <p>Tačka sa id-em <xsl:value-of select="@elementId"/> se briše</p>
                    </xsl:when>
                </xsl:choose>
            </xsl:when>
        </xsl:choose>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>