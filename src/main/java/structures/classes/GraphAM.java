package structures.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

import structures.enums.Color;
import structures.interfaces.IGraph;

public class GraphAM<K, V> implements IGraph<K, V> {

    private ArrayList<Vertex<K, V>> vertexList;
    private ArrayList<Edge<K, V>> edgeList;
    private ArrayList<ArrayList<Edge<K, V>>> adjacencyMatrix;

    public GraphAM() {
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
        this.adjacencyMatrix = new ArrayList<>();

    }

    public ArrayList<ArrayList<Edge<K, V>>> getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    @Override
    public int getVertexAmount() {
        return vertexList.size();
    }

    @Override
    public int getEdgesAmount() {
        return edgeList.size();
    }

    @Override
    public ArrayList<Vertex<K, V>> getVertexList() {
        return vertexList;
    }

    @Override
    public void setVertexList(ArrayList<Vertex<K, V>> vertexList) {
        this.vertexList = vertexList;
    }

    @Override
    public ArrayList<Edge<K, V>> getEdgeList() {
        return edgeList;
    }

    @Override
    public void setEdgeList(ArrayList<Edge<K, V>> edgeList) {
        this.edgeList = edgeList;
    }

    /**
     * Inserts a new vertex with the specified key and value into the graph.
     * If a vertex with the same key already exists, returns null.
     *
     * @param key   the key of the vertex
     * @param value the value of the vertex
     * @return the newly inserted vertex, or null if a vertex with the same key already exists
     */
    @Override
    public Vertex<K, V> insertVertex(K key, V value) {

        if (searchVertex(key) != null) {
            return null;
        }

        Vertex<K, V> vertex = new Vertex<K, V>(key, value);

        // Add new row to adjacency matrix
        ArrayList<Edge<K, V>> newRow = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix.size(); i++) {
            newRow.add(null);
        }
        adjacencyMatrix.add(newRow);

        // Add new column to adjacency matrix
        for (ArrayList<Edge<K, V>> row : adjacencyMatrix) {
            row.add(null);
        }

        this.vertexList.add(vertex);
        return vertex;
    }

    /**
     * Inserts an edge into the graph.
     * If the vertices of the edge do not exist in the graph, they will be inserted first.
     * 
     * @param edge the edge to be inserted
     * @return the inserted edge
     */
    @Override
    public Edge<K, V> insertEdge(Edge<K, V> edge) {

        if (!vertexList.contains(edge.getVertex1())) {
            insertVertex(edge.getVertex1());
        }

        if (!vertexList.contains(edge.getVertex2())) {
            insertVertex(edge.getVertex2());
        }

        return insertSimpleEdge(edge);

    }

    /**
     * Inserts an edge into the graph using Prim's algorithm.
     * If the vertices of the edge are not already in the graph, they are added.
     * The edge is added to the adjacency list of the first vertex and a reverse edge is added to the adjacency list of the second vertex.
     * The edge is also added to the list of edges in the graph.
     * Returns the inserted edge.
     *
     * @param edge the edge to be inserted
     * @return the inserted edge
     */
    @Override
    public Edge<K, V> insertEdgePrim(Edge<K, V> edge) {

        if (!vertexList.contains(edge.getVertex1())) {
            vertexList.add(edge.getVertex1());
        }

        if (!vertexList.contains(edge.getVertex2())) {
            vertexList.add(edge.getVertex2());
        }

        Vertex<K, V> vertex1 = edge.getVertex1();
        Vertex<K, V> vertex2 = edge.getVertex2();

        vertex1.getEdges().add(edge);
        vertex2.getEdges().add(new Edge<>(vertex2, vertex1, edge.getWeight()));

        edgeList.add(edge);

        return insertSimpleEdge(edge);
    }

    /**
     * Inserts a simple edge into the graph.
     * 
     * @param edge the edge to be inserted
     * @return the inserted edge, or null if the edge cannot be inserted
     */
    private Edge<K, V> insertSimpleEdge(Edge<K, V> edge) {

        if (edge.getVertex2().equals(edge.getVertex1())) {
            return null;
        }

        if (areConnected(edge.getVertex2(), edge.getVertex1())) {
            return null;
        }

        int index1 = vertexList.indexOf(edge.getVertex1());
        int index2 = vertexList.indexOf(edge.getVertex2());

        if (index1 == -1 || index2 == -1) {
            return null;
        }

        adjacencyMatrix.get(index1).set(index2, edge);
        adjacencyMatrix.get(index2).set(index1, new Edge<>(edge.getVertex2(), edge.getVertex1(), edge.getWeight()));
        edgeList.add(edge);

        return edge;
    }

    /**
     * Removes a vertex from the graph.
     * 
     * @param vertex the vertex to be removed
     * @return the removed vertex, or null if the vertex is null
     */
    @Override
    public Vertex<K, V> removeVertex(Vertex<K, V> vertex) {
        if (vertex == null) {
            return null;
        }

        disconnect(vertex);

        this.vertexList.remove(vertex);

        return vertex;
    }

    /**
     * Searches for a vertex with the specified key in the graph.
     * 
     * @param key the key of the vertex to search for
     * @return the vertex with the specified key, or null if not found
     */
    @Override
    public Vertex<K, V> searchVertex(K key) {
        for (Vertex<K, V> vertex : vertexList) {
            if (vertex.getKey().equals(key)) {
                return vertex;
            }
        }
        return null;
    }

    /**
     * Performs a breadth-first search (BFS) starting from the specified vertex.
     * The BFS algorithm explores all vertices reachable from the starting vertex in a breadth-first manner.
     * It assigns colors, distances, and predecessors to each vertex during the exploration.
     *
     * @param s the starting vertex for the BFS
     */
    @Override
    public void BFS(Vertex<K, V> s) {
        for (Vertex<K, V> vertex : vertexList) {
            vertex.setColor(Color.WHITE);
            vertex.setDistance(Integer.MAX_VALUE);
            vertex.setPredecessor(null);
        }

        s.setColor(Color.GRAY);
        s.setDistance(0);
        s.setPredecessor(null);

        ArrayList<Vertex<K, V>> queue = new ArrayList<>();
        queue.add(s);

        while (!queue.isEmpty()) {
            Vertex<K, V> u = queue.remove(0);
            for (Vertex<K, V> v : getAdjacents(u)) {
                if (v.getColor() == Color.WHITE) {
                    v.setColor(Color.GRAY);
                    v.setDistance(u.getDistance() + 1);
                    v.setPredecessor(u);
                    queue.add(v);
                }
            }
            u.setColor(Color.BLACK);
        }
    }

    /**
     * Returns a list of adjacent vertices to the given vertex.
     *
     * @param vertex the vertex for which to find adjacent vertices
     * @return a list of adjacent vertices
     */
    private ArrayList<Vertex<K, V>> getAdjacents(Vertex<K, V> vertex) {
        ArrayList<Vertex<K, V>> adjacents = new ArrayList<>();
        for (Edge<K, V> edge : adjacencyMatrix.get(vertexList.indexOf(vertex))) {
            if (edge != null) {
                adjacents.add(edge.getVertex2());
            }
        }
        return adjacents;
    }

    private Edge<K, V> getEdge(Vertex<K, V> vertex1, Vertex<K, V> vertex2) {
        return adjacencyMatrix.get(vertexList.indexOf(vertex1)).get(vertexList.indexOf(vertex2));
    }

    /**
     * Checks if two vertices are connected in the graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @return true if the vertices are connected, false otherwise
     */
    @Override
    public boolean areConnected(Vertex<K, V> vertex1, Vertex<K, V> vertex2) {
        if (vertex1 == null || vertex2 == null) {
            return false;
        }
        return adjacencyMatrix.get(vertexList.indexOf(vertex1)).get(vertexList.indexOf(vertex2)) != null;
    }

    /**
     * Disconnects the given vertex from all its connected vertices in the graph.
     * 
     * @param vertex1 the vertex to disconnect from
     */
    private void disconnect(Vertex<K, V> vertex1) {
        for (Vertex<K, V> vertex2 : vertexList) {
            if (areConnected(vertex1, vertex2)) {
                disconnect(vertex1, vertex2);
            }
        }
    }

    /**
     * Disconnects two vertices in the graph by removing the corresponding edge between them.
     * 
     * @param vertex1 the first vertex to disconnect
     * @param vertex2 the second vertex to disconnect
     */
    private void disconnect(Vertex<K, V> vertex1, Vertex<K, V> vertex2) {
        edgeList.remove(adjacencyMatrix.get(vertexList.indexOf(vertex1)).get(vertexList.indexOf(vertex2)));
        adjacencyMatrix.get(vertexList.indexOf(vertex1)).set(vertexList.indexOf(vertex2), null);
    }

    /**
     * Performs a Depth-First Search (DFS) traversal on the graph represented by the minimumSpanningTree.
     * Returns the total weight of the visited vertices.
     *
     * @param minimumSpanningTree the graph on which the DFS traversal is performed
     * @return the total weight of the visited vertices
     */
    @Override
    public int DFS(IGraph<K, V> minimumSpanningTree) {

        int totalWeight = 0;

        // Initialize the vertices
        for (Vertex<K, V> vertex : minimumSpanningTree.getVertexList()) {
            vertex.setColor(Color.WHITE);
            vertex.setPredecessor(null);
        }

        // Visit all vertices
        for (Vertex<K, V> vertex : minimumSpanningTree.getVertexList()) {
            if (vertex.getColor() == Color.WHITE) {
                totalWeight += DFSVisit(vertex);
            }
        }

        return totalWeight;

    }

    /**
     * Performs a Depth First Search (DFS) visit starting from the given vertex.
     * This method recursively explores the graph, visiting each vertex and accumulating the total weight of the edges traversed.
     *
     * @param vertex the starting vertex for the DFS visit
     * @return the total weight of the edges traversed during the DFS visit
     */
    private int DFSVisit(Vertex<K, V> vertex) {
        int totalWeight = 0;

        vertex.setColor(Color.GRAY);

        for (Edge<K, V> edge : getVertexEdges(vertex)) {
            Vertex<K, V> nextVertex = edge.getVertex2();

            if (nextVertex.getColor() == Color.WHITE) {
                nextVertex.setPredecessor(vertex);
                totalWeight += edge.getWeight();
                totalWeight += DFSVisit(nextVertex);
            }
        }

        vertex.setColor(Color.BLACK);

        return totalWeight;
    }

    /**
     * Applies Dijkstra's algorithm to find the shortest path between two vertices in the graph.
     * 
     * @param startVertex the starting vertex
     * @param endVertex the ending vertex
     * @return a list of edges representing the shortest path from the start vertex to the end vertex,
     *         or null if no path is found
     */
    @Override
    public List<Edge<K, V>> dijkstra(Vertex<K, V> startVertex, Vertex<K, V> endVertex) {
        // Initialize data structures for the algorithm
        Map<Vertex<K, V>, Integer> distance = new HashMap<>();
        Map<Vertex<K, V>, Edge<K, V>> previousEdge = new HashMap<>();
        PriorityQueue<Vertex<K, V>> priorityQueue = new PriorityQueue<>(getVertexAmount(),
                (v1, v2) -> Integer.compare(distance.get(v1), distance.get(v2)));

        // Initialize distance to all vertices as infinity and the start vertex's
        // distance to 0
        for (Vertex<K, V> vertex : vertexList) {
            distance.put(vertex, Integer.MAX_VALUE);
            previousEdge.put(vertex, null);
        }
        distance.put(startVertex, 0);

        // Add the start vertex to the priority queue
        priorityQueue.add(startVertex);

        while (!priorityQueue.isEmpty()) {
            Vertex<K, V> currentVertex = priorityQueue.poll();

            if (currentVertex == endVertex) {
                return buildPath(previousEdge, endVertex);
            }

            for (Edge<K, V> edge : getVertexEdges(currentVertex)) {
                Vertex<K, V> neighbor = edge.getVertex2();
                int newDistance = distance.get(currentVertex) + edge.getWeight();

                if (newDistance < distance.get(neighbor)) {
                    distance.put(neighbor, newDistance);
                    previousEdge.put(neighbor, edge);
                    priorityQueue.add(neighbor);
                }
            }
        }

        return null; // No path found
    }

    /**
     * Returns a list of edges connected to the specified vertex.
     *
     * @param vertex the vertex for which to retrieve the edges
     * @return a list of edges connected to the specified vertex
     */
    public List<Edge<K, V>> getVertexEdges(Vertex<K, V> vertex) {
        List<Edge<K, V>> edges = new ArrayList<>();
        for (Edge<K, V> edge : adjacencyMatrix.get(vertexList.indexOf(vertex))) {
            if (edge != null) {
                edges.add(edge);
            }
        }
        return edges;
    }

    /**
     * Builds a path from the given previousEdge map and endVertex.
     * The path is represented as a list of edges.
     *
     * @param previousEdge a map containing the previous edge for each vertex in the path
     * @param endVertex the end vertex of the path
     * @return a list of edges representing the path
     */
    private List<Edge<K, V>> buildPath(Map<Vertex<K, V>, Edge<K, V>> previousEdge, Vertex<K, V> endVertex) {
        List<Edge<K, V>> path = new ArrayList<>();
        Vertex<K, V> currentVertex = endVertex;

        while (previousEdge.get(currentVertex) != null) {
            Edge<K, V> edge = previousEdge.get(currentVertex);
            path.add(0, edge); // Add the edge at the beginning of the path
            currentVertex = edge.getVertex1();
        }

        return path;
    }

    /**
     * Applies the Floyd-Warshall algorithm to find the shortest distances between all pairs of vertices in the graph.
     * Returns a 2D array representing the distance matrix.
     *
     * @return The distance matrix with the shortest distances between all pairs of vertices.
     */
    @Override
    public int[][] floydWarshall() {

        int[][] distanceMatrix = new int[vertexList.size()][vertexList.size()];

        for (int i = 0; i < vertexList.size(); i++) {
            for (int j = 0; j < vertexList.size(); j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0;
                } else if (areConnected(vertexList.get(i), vertexList.get(j))) {
                    distanceMatrix[i][j] = getEdge(vertexList.get(i), vertexList.get(j)).getWeight();
                } else {
                    distanceMatrix[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        for (int k = 0; k < vertexList.size(); k++) {
            for (int i = 0; i < vertexList.size(); i++) {
                for (int j = 0; j < vertexList.size(); j++) {
                    if (distanceMatrix[i][k] != Integer.MAX_VALUE && distanceMatrix[k][j] != Integer.MAX_VALUE) {
                        distanceMatrix[i][j] = Math.min(distanceMatrix[i][j],
                                distanceMatrix[i][k] + distanceMatrix[k][j]);
                    }
                }
            }
        }

        return distanceMatrix;
    }

    /**
     * Prim's algorithm implementation, this method returns a minimum spanning
     * tree of the graph, with a given vertex of the original graph.
     */
    @Override
    public IGraph<K, V> prim(Vertex<K, V> begin) {

        if (getVertexAmount() == 0) {
            return null;
        }

        IGraph<K, V> minimumSpanningTree = new GraphAM<>();

        // Set of vertices already included in the Minimum Spanning Tree
        Set<Vertex<K, V>> includedVertices = new HashSet<>();

        // Priority queue to select the edge with the minimum weight in each iteration
        PriorityQueue<Edge<K, V>> minHeap = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));

        Vertex<K, V> currentVertex = begin;
        includedVertices.add(currentVertex);

        // Continue until all vertices are included in the Minimum Spanning Tree
        while (includedVertices.size() < getVertexAmount() && currentVertex != null) {

            for (Edge<K, V> edge : getVertexEdges(currentVertex)) {
                if (!includedVertices.contains(edge.getVertex2())) {
                    minHeap.add(edge);
                }
            }

            Edge<K, V> minEdge = minHeap.poll();

            if (minEdge != null) {
                // Get the next vertex connected by the selected edge
                Vertex<K, V> nextVertex = minEdge.getVertex2();

                // Add the edge to the Minimum Spanning Tree
                minimumSpanningTree.insertEdge(minEdge);

                // Add the next vertex to the set of included vertices
                includedVertices.add(nextVertex);

                // Move to the next vertex
                currentVertex = nextVertex;
            } else {

                currentVertex = null;
            }
        }

        return minimumSpanningTree;

    }

    /**
     * Kruskal's algorithm to find a minimum spanning tree in a graph.
     *
     * @return A new graph representing the minimum spanning tree.
     */
    @Override
    public IGraph<K, V> kruskal() {

        if (getVertexAmount() == 0) {
            return null;
        }

        GraphAL<K, V> mst = new GraphAL<>();

        // Create a list of edges sorted by weight
        List<Edge<K, V>> sortedEdges = new ArrayList<>(getEdgeList());
        Collections.sort(sortedEdges, Comparator.comparingInt(Edge::getWeight));

        // Initialize disjoint sets for each vertex
        Set<Set<Vertex<K, V>>> disjointSets = new HashSet<>();

        for (Vertex<K, V> vertex : getVertexList()) {
            // Create a singleton set for each vertex
            Set<Vertex<K, V>> singletonSet = new HashSet<>();
            singletonSet.add(vertex);
            disjointSets.add(singletonSet);
        }

        for (Edge<K, V> edge : sortedEdges) {
            Vertex<K, V> vertex1 = edge.getVertex1();
            Vertex<K, V> vertex2 = edge.getVertex2();

            // Find the sets to which the vertices belong
            Set<Vertex<K, V>> set1 = null;
            Set<Vertex<K, V>> set2 = null;

            for (Set<Vertex<K, V>> disjointSet : disjointSets) {
                if (disjointSet.contains(vertex1)) {
                    set1 = disjointSet;
                }
                if (disjointSet.contains(vertex2)) {
                    set2 = disjointSet;
                }
            }

            // If the vertices are in different sets, add the edge to the minimum spanning
            // tree
            if (set1 != null && set2 != null && set1 != set2) {
                mst.insertEdgePrim(edge);

                // Merge the disjoint sets
                set1.addAll(set2);
                disjointSets.remove(set2);
            }
        }

        return mst;
    }

    /**
     * Inserts a new vertex into the graph.
     * If a vertex with the same key already exists, returns null.
     * Otherwise, adds a new row and column to the adjacency matrix,
     * adds the vertex to the vertex list, and returns the inserted vertex.
     *
     * @param vertex the vertex to be inserted
     * @return the inserted vertex, or null if a vertex with the same key already exists
     */
    @Override
    public Vertex<K, V> insertVertex(Vertex<K, V> vertex) {
        if (searchVertex(vertex.getKey()) != null) {
            return null;
        }

        // Add new row to adjacency matrix
        ArrayList<Edge<K, V>> newRow = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix.size(); i++) {
            newRow.add(null);
        }
        adjacencyMatrix.add(newRow);

        // Add new column to adjacency matrix
        for (ArrayList<Edge<K, V>> row : adjacencyMatrix) {
            row.add(null);
        }

        this.vertexList.add(vertex);
        return vertex;
    }

}
