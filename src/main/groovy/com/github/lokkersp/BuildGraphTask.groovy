package com.github.lokkersp

import com.github.lokkersp.graph.Graph
import com.github.lokkersp.graph.GraphEdge
import com.github.lokkersp.graph.GraphNode
import com.github.lokkersp.providers.GEXFProvider
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class BuildGraphTask extends DefaultTask {
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
    Graph g
    @Input
    @Optional
    String graphName = "${project.name}-graph"

    def getGEXFHeader() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<gexf xmlns=\"http://www.gexf.net/1.2draft\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd\" version=\"1.2\">\n"
    }
    def getGEXFMetaString() {
        return " <meta lastmodifieddate=\"${new Date().format("dd/MM/yyyy")}\">\n" +
                "\t\t<creator>yakovlev@viva64.com</creator>\n" +
                "\t\t<description>A Web network</description>\n" +
                " </meta>\n"
    }
    def getGEXFAttributes() {
        return
        "  <attributes class=\"node\">\n" +
                "\t\t<attribute id=\"0\" title=\"path\" type=\"string\"/>\n" +
                "\t\t<attribute id=\"1\" title=\"deps\" type=\"string\"/>\n" +
                "\t\t<attribute id=\"2\" title=\"external-deps\" type=\"string\"/>\n" +
                "\t\t<attribute id=\"2\" title=\"indegree\" type=\"float\"/>\n" +
                "  </attributes>\n"
    }
    String extDepToString(ExternalModuleDependency emd) {
        if((emd.version == "" || emd.version == null) || (emd.group == "" || emd.group == null)) {
            return (((emd.group == "" || emd.group == null)?"":"${emd.group}")+(emd.name) + (emd.version == "" || emd.version == null)?"":"${emd.version}")
        }
        return "${emd.group}:${emd.name}:${emd.version}"
    }
    String buildGEXF(nodes,edges){
        return getGEXFHeader() + getGEXFMetaString() +
                " <graph defaultedgetype=\"directed\">\n" + getGEXFAttributes() +
                nodes +
                edges +
                " </graph>\n" +
                "</gexf>"


    }
    List<String> edgeRecordsForProjects(int edgeId,Map<String,Integer> nodes,Project project) {
        def ps = project.configurations*.allDependencies*.withType(ProjectDependency).flatten().unique()
        if(ps.size() == 0) return []
        def edges = []
        println project.name
        ps.each{ProjectDependency p->
            println "\t${p.name} [${p.dependencyProject.projectDir.path}]"
            def edge = "\t<edge id=\"$edgeId\" source=\"${nodes.get(project.name)}\" target=\"${nodes.get(p.name)}\" />"
            edges.add(edge)
            edgeId++
        }
        return edges
    }

    def nodeRecordForProject(int id,Project p) {
        def deps = p.configurations*.allDependencies*.withType(ProjectDependency).flatten().unique().collect{it.dependencyProject.name}.join(";")
        def extdeps = p.configurations*.allDependencies*.withType(ExternalModuleDependency).flatten().unique().collect{extDepToString(it as ExternalModuleDependency)}.join(" ; ")
        //.collect { it.allDependencies.withType(ProjectDependency)}.flatten().unique().join(";")
        def nodeAttributes =
                " \t<attvalues>\n" +
                        " \t <attvalue for=\"0\" value=\"${p.getProjectDir().path}\" />\n"+
                        " \t <attvalue for=\"1\" value=\"${deps == ""?"__":deps}\" />\n"+
                        " \t <attvalue for=\"2\" value=\"${extdeps == ""?"__":extdeps}\" />\n"+
                " \t</attvalues>\n"

        return "\n\t<node id=\"${id}\" label=\"${p.name}\" >" +
                "$nodeAttributes"+
                "\t</node>\n"
    }

    BuildGraphTask() {
        group = 'project-graph'
        description = "Generate project graph"
        g = new Graph<String>();
    }
    String metaBuild() {
        return  " <meta lastmodifieddate=\"${new Date().format(DATE_FORMAT)}\">\n" +
                "   <creator>$creator</creator>\n" +
                "   <description>$description</description>\n" +
                "   <keywords>$description</keywords>\n" +
                " </meta>\n"
    }
    @TaskAction
    def buildGraph() {
        project // top project of the tree
/*        Project applied = project // project with applied plugin
        //go to the top of the build
        while (top?.subprojects?.size() == 0) {
            top = project.parent
        }
        g = new Graph<String>();
        project.subprojects.each { s->
            g.addNode(new GraphNode<String>(s.name))
        }
        project.subprojects.each {s->
            def ds = s.configurations*.dependencies*.withType(ProjectDependency).flatten().unique()
            ds.each {d ->
                g.addEdge(new GraphEdge<String>(s.name,d.dependencyProject.name))
            }
            //println g.nodes
            //def GEXFRepresentantion = new GEXFProvider(g)
            new File("S:/temp.gexf")?.delete()
            new File("S:/temp.gexf").createNewFile()
            def f = new File("S:/temp.gexf")
             f << serialize(g)
        }
        //project.afterEvaluate(this.&afterEvaluate())
*/

        def subprojects = project.subprojects
        def counter = 0
        def nodes = []
        def edges = []
        Map<String,Integer> nodesRefl = new HashMap<>();
        subprojects.each {Project p->
            nodesRefl.put(p.name,counter)
            nodes.add(nodeRecordForProject(counter,p))
            counter++
        }
        def ceed = 0
        subprojects.forEach{Project p ->
            int edgeID = edges.size() > 0 ? ((edges.flatten().last() as String).split(" ")[1].replace("id=\"","").replace("\"","") as Integer) : ceed
            edges.add(edgeRecordsForProjects(edgeID++,nodesRefl,p))
        }
        String nodesScope = "  <nodes>\n" +nodes.join(" ") +"\n  </nodes>\n"
        String edgesScope = "  <edges>\n" +edges.flatten().join("\n") +"\n  </edges>\n"
        String document = buildGEXF(nodesScope,edgesScope)
        //println document
        new File("S:/temp.gexf").delete()
        new File("S:/temp.gexf").createNewFile()
        def file = new File("S:/temp.gexf")
        file.write document
        println "\n--------------------------------------------\nTotal projects:${subprojects.size()}"
    }

    String serialize(Graph graph) {
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
