<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <h2>Akti</h2>
        <table border="1">
            <tr bgcolor="#9acd32">
                <th style="text-align:left">Title</th>
                <th style="text-align:left">Date</th>
                <th style="text-align:left">Get as</th>
            </tr>
            <xsl:for-each select="wrapper/act">
                <tr>
                    <td>
                        <a href="#!/actVoting/{@id}" alt="More information">
                            <xsl:value-of select="@title"/>
                        </a>
                    </td>
                    <td>
                        <xsl:value-of select="@date"/>
                    </td>
                    <td>
                        <button ng-click="vm.getPDF('{@id}')" style="cursor: pointer;">PDF</button>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
</xsl:stylesheet>
