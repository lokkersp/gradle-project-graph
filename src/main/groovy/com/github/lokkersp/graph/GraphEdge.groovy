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

    @Override
    String toString() {
        return "${id>-1?"$id:":""}${source} -> ${destination}"
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        GraphEdge graphEdge = (GraphEdge) o

        if (destination != graphEdge.destination) return false
        if (source != graphEdge.source) return false

        return true
    }

    int hashCode() {
        return Objects.hash(source,destination)
    }
}
