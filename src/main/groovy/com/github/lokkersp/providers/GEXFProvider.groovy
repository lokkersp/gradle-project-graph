package com.github.lokkersp.providers

import com.github.lokkersp.graph.Graph
import com.github.lokkersp.graph.GraphEdge
import com.github.lokkersp.graph.GraphNode
import sun.reflect.generics.reflectiveObjects.NotImplementedException

class GEXFProvider implements Provider{
    Graph graph
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

    GEXFProvider(Graph graph) {
        this.graph = graph
        creator = "Built by ${System.getenv("USERNAME")} on the ${System.getenv("COMPUTERNAME")}"
        description = boilerplateDescription
        keywords = "auto-generated, gradle, mixed"
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

    @Override
    String serialize() {
        def edgesString = graph.edges.each {GraphEdge<String>e->"\t<edge id=\"${e.id}\" source=\"${e.source}\" target=\"${e.destination}\"/>"}.join('\n')
        def nodesString =  graph.nodes.each {GraphNode<String>  node->"\t<node id=\"${graph.nodesToID.get(node.id) as String}\" label=\"${node.id}\"/>"}.join('\n')
        String data =
                  "$GEXF_HEADER \n${metaBuild()}" +
                  "<graph defaultedgetype=\"${DEFAULT_EDGE_TYPE}\">\n"+
                     "<nodes>\n"+
                        "\t${nodesString}"
                     "\n</nodes>\n"
                     "<edges>\n"
                        "\t${edgesString}"
                     "\n</edges>\n"
                      "\n</graph>\n"
                  "${GEXF_END}"
        return data

    }
}



