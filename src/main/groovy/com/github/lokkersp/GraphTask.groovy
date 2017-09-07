package com.github.lokkersp

import com.github.lokkersp.graph.Graph
import com.github.lokkersp.graph.GraphEdge
import com.github.lokkersp.graph.GraphNode
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class GraphTask extends DefaultTask {

    Graph g
    @Input
    @Optional
    String graphName = "${project.name}-graph"


    GraphTask() {
        group = 'project-graph'
        description = "Generate project graph"

    }

    @TaskAction
    def buildGraph() {
        Project top = project // top project of the tree
        Project applied = project // project with applied plugin
        //go to the top of the build
        while (top?.subprojects?.size() == 0) {
            top = project.parent
        }
        g = new Graph();
        top.subprojects.each { s->
            g.addNode(new GraphNode<String>(s.name))
        }
        top.subprojects.each {s->
            def ds = s.configurations*.dependencies*.withType(ProjectDependency).flatten().unique()
            ds.each {d ->
                g.addEdge(new GraphEdge<String>(s.name,d.dependencyProject.name))
            }

        }
        project.afterEvaluate(this.&afterEvaluate())
    }
}
