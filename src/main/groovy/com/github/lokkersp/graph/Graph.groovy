package com.github.lokkersp.graph

import com.github.lokkersp.providers.Provider

class Graph<T extends Provider,R> {
    T provider // one of registered providers

    Set<GraphNode> nodes = new HashSet<>()
    List<GraphEdge> edges = []
    Map<R,Integer> nodesToID = new HashMap<>()
    Map<R,Integer> edgesToID = new HashMap<>()
    /**
     *
     * @param ID
     * @param map
     * @param h
     * @return
     */
    static def updateMapping(R ID, Map<R,Integer> map, Collection h) {
        if(h.size() == 0) {
            map.put(ID, 0)
        } else {
            map.put(ID, map.values().last()++)
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
            updateMapping(node.id,nodesToID,nodes)
        return checkKeyObjectExist(node.id,nodesToID)
    }
    /**
     * you should create node every time you call method
     * @param node GraphNode<T> object
     * @return true if node was added
     */
    boolean addEdge(GraphEdge<R> e) {
            def passedID =(edges.last().id)++
            e.id = (edges.size() == 0)?0:passedID
            edges.add(e)
            return edges.last().id == passedID
    }

    private boolean removeNode(R id){

    }
    static boolean checkKeyObjectExist(R id,Map map) {
        return map.get(id, -1) != -1
    }

    Graph(T provider) {
        this.provider = provider
    }
    Graph(){

    }


}
