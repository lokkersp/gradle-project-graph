package com.github.lokkersp.graph

class GraphNode<R> {
    R id
    Map attributes = new HashMap()

    int nodeInDegree = 0
    int nodeOutDegree = 0

    GraphNode(R id) {
        this.id = id
    }

    GraphNode(R id, Map attributes) {
        this.id = id
        this.attributes = attributes
    }

}
