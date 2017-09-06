package com.github.lokkersp.graph

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

class GraphAttributeDefinition implements Serializable {
    @Input
    int id
    @Input
    String title
    @Input
    String type
    @Input
    @Optional
    String defaultValue

    GraphAttributeDefinition(int id) {
        this.id = id
    }

    GraphAttributeDefinition(int id, String title) {
        this.id = id
        this.title = title
    }

    GraphAttributeDefinition(int id, String title, String type) {
        this.id = id
        this.title = title
        this.type = type
    }

    GraphAttributeDefinition(int id, String title, String type, String defaultValue) {
        this.id = id
        this.title = title
        this.type = type
        this.defaultValue = defaultValue
    }

    @Override
    String toString() {
        return "[ baseID=$id|attTitle=$title|typeOf=$type|default=$defaultValue ]"
    }
}
