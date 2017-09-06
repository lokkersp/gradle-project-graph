package com.github.lokkersp.graph

class GraphEdge<R> {
    R source
    R destination

    GraphEdge() {
    }

    GraphEdge(R source, R destination) {
        this.source = source
        this.destination = destination
    }

    @Override
    String toString() {
        return "${source} -> ${destination}"
    }
}
