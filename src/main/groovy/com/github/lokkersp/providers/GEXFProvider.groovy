package com.github.lokkersp.providers

import com.github.lokkersp.graph.Graph
import sun.reflect.generics.reflectiveObjects.NotImplementedException

class GEXFProvider implements Provider{
    static final String GEXF_HEADER = """<?xml version="1.0" encoding="UTF-8"?>
                                         <gexf xmlns="http://www.gexf.net/1.2draft" 
                                               xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance" 
                                               xsi:schemaLocation="http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd 
                                               version="1.2">                                     
                                         """
    static final String GEXF_END = "</gexf>"
    // meta
    String creator
    String description
    String keywords
    String DATE_FORMAT = "dd/MM/yyyy"
    //metaend

    static final String boilerplateDescription = "Auto-generated GEXF from Gradle project tree"
    String DEFAULT_EDGE_TYPE = "mixed"
    String content
    GEXFProvider(){

    }
    GEXFProvider(String dateFormat){
        DATE_FORMAT = dateFormat || dateFormat?.empty ?: DATE_FORMAT
        creator = "Built by ${System.getenv("USERNAME")} on the ${System.getenv("COMPUTERNAME")}"
        description = boilerplateDescription
        keywords = "auto-generated, gradle, mixed"
    }
    String metaBuild() {
        return  " <meta lastmodifieddate=\"${new Date().format(DATE_FORMAT)}\">\n" +
                "   <creator>$creator</creator>\n" +
                "   <description>$description</description>\n" +
                "   <keywords>$description</keywords>\n" +
                " </meta>\n"
    }
    def init(){

    }
    @Override
    String serialize() {
        return """${GEXF_HEADER}
                  ${metaBuild()}
                  <graph defaultedgetype="${DEFAULT_EDGE_TYPE}">
                    
                  </graph>
                  ${GEXF_END}
               """
    }
}
