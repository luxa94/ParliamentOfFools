<xsl:stylesheet version="1.0"
                xmlns:a="http://www.fools.gov.rs/acts"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">

        <table border="1">
            <tr bgcolor="#9acd32">
                <th style="text-align:left">Title</th>
                <th style="text-align:left">Date</th>
            </tr>

            <xsl:for-each select="//amendment">
                <tr>
                    <td>
                        <a href="#!/amendmentVoting/{@id}" alt="More information">
                            <xsl:value-of select="@name"/>
                        </a>
                    </td>
                    <td>
                        <button ng-click="vm.getPDF('{@id}')" style="cursor: pointer;"> PDF </button>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>

</xsl:stylesheet>