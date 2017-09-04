package com.github.lokkersp

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

class GraphTask extends DefaultTask {

    @Input
    @Optional
    String graphName = "${project.name}-graph"


    GraphTask() {
        group = 'project-graph'
        description = "Generate project graph"

    }
}
