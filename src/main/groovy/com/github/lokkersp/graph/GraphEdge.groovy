package com.github.lokkersp.graph

class GraphEdge<R> {
    R source
    R destination
    int id = -1
    GraphEdge() {
    }

    GraphEdge(R source, R destination) {
        this.source = source
        this.destination = destination
    }

    int hashCode() {
        return Objects.hash(source,destination)
    }
}
