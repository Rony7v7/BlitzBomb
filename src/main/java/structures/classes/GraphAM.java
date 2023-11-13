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

    @Override
    public Vertex<K, V> removeVertex(Vertex<K, V> vertex) {
        if (vertex == null) {
            return null;
        }

        disconnect(vertex);

        this.vertexList.remove(vertex);

        return vertex;
    }

    @Override
    public Vertex<K, V> searchVertex(K key) {
        for (Vertex<K, V> vertex : vertexList) {
            if (vertex.getKey().equals(key)) {
                return vertex;
            }
        }
        return null;
    }

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

    @Override
    public boolean areConnected(Vertex<K, V> vertex1, Vertex<K, V> vertex2) {
        if (vertex1 == null || vertex2 == null) {
            return false;
        }
        return adjacencyMatrix.get(vertexList.indexOf(vertex1)).get(vertexList.indexOf(vertex2)) != null;
    }

    private void disconnect(Vertex<K, V> vertex1) {
        for (Vertex<K, V> vertex2 : vertexList) {
            if (areConnected(vertex1, vertex2)) {
                disconnect(vertex1, vertex2);
            }
        }
    }

    private void disconnect(Vertex<K, V> vertex1, Vertex<K, V> vertex2) {
        edgeList.remove(adjacencyMatrix.get(vertexList.indexOf(vertex1)).get(vertexList.indexOf(vertex2)));
        adjacencyMatrix.get(vertexList.indexOf(vertex1)).set(vertexList.indexOf(vertex2), null);
    }

    
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

    @Override
    public List<Edge<K, V>> Dijkstra(Vertex<K, V> startVertex, Vertex<K, V> endVertex) {
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

    private List<Edge<K, V>> getVertexEdges(Vertex<K, V> vertex) {
        List<Edge<K, V>> edges = new ArrayList<>();
        for (Edge<K, V> edge : adjacencyMatrix.get(vertexList.indexOf(vertex))) {
            if (edge != null) {
                edges.add(edge);
            }
        }
        return edges;
    }

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
