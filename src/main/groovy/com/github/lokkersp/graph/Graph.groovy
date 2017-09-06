package com.github.lokkersp.graph

import com.github.lokkersp.providers.Provider

class Graph<T extends Provider,R> {
    T provider // one of registered providers

    Set<GraphNode> nodes = new HashSet<>()
    List<GraphEdge> edges = []
    Map<R,Integer> nodesToID = new HashMap<>()

    def updateMapping(R ID) {
        if(nodes.size() == 0) {
            nodesToID.put(ID, 0)
        } else {
            nodesToID.put(ID, nodesToID.values().last()++)
        }
    }
    /**
     * Method create a record inside collection of nodes
     * you should create node every time you call this method
     *
     * @param node GraphNode<T> object
     * @return true if node was added
     */
    boolean addNode(GraphNode<R> node) {
            nodes.add(node)
            updateMapping(node.id)
        return checkNodeObjectExist(node.id)
    }
    /**
     * you should create node every time you call method
     * @param node GraphNode<T> object
     * @return true if node was added
     */
    boolean addEdge(GraphEdge<R> e) {
        if (checkNodeObjectExist(e.source) && checkNodeObjectExist(e.destination)) {
            edges.add(e)
            return true
        }
        false
    }

    private boolean removeNode(R id){

    }
    boolean checkNodeObjectExist(R id) {
        return nodesToID.get(id, -1) != -1
    }

    Graph(T provider) {
        this.provider = provider
    }
    Graph(){

    }


}
