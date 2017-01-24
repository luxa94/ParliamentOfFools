package com.bdzjn.xml.util;

import com.marklogic.client.semantics.SPARQLQueryDefinition;
import com.marklogic.client.semantics.SPARQLQueryManager;

public class RdfTripleUpdate {

    public static void  updateTriple(SPARQLQueryManager queryManager, String metadataCollection, String subject, String predicate, String object) {
        String queryDefinition =
                "PREFIX xs: <http://www.w3.org/2001/XMLSchema#> \n" +
                        " WITH <" + metadataCollection + ">" +
                        " DELETE { <" + subject + "> <" + predicate + ">  ?o} " +
                        " INSERT { <" + subject + "> <" + predicate + "> \"" + object + "\" }" +
                        " WHERE  { <" + subject + "> <" + predicate + ">  ?o}";
        SPARQLQueryDefinition query = queryManager
                .newQueryDefinition(queryDefinition);

        queryManager.executeUpdate(query);
    }
}
