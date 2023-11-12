package structures.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import structures.enums.Color;
import structures.enums.GraphType;
import structures.interfaces.IGraph;

public class GraphAL<K, V> implements IGraph<K, V> {
    private ArrayList<Vertex<K, V>> vertexList;
    private ArrayList<Edge<K, V>> edgeList;

    public GraphType type;

    public GraphAL(GraphType type) {
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
        this.type = type;
    }

    @Override
    public int getVertexAmount() {
        return vertexList.size();
    }

    /**
     * @return the edgesAmount
     */
    @Override
    public int getEdgesAmount() {
        return edgeList.size();
    }

    /**
     * @return the vertexList
     */
    @Override
    public ArrayList<Vertex<K, V>> getVertexList() {
        return vertexList;
    }

    /**
     * @param vertexList the vertexList to set
     */
    @Override
    public void setVertexList(ArrayList<Vertex<K, V>> vertexList) {
        this.vertexList = vertexList;
    }

    /**
     * @return the edgeList
     */
    @Override
    public ArrayList<Edge<K, V>> getEdgeList() {
        return edgeList;
    }

    /**
     * @param edgeList the edgeList to set
     */
    @Override
    public void setEdgeList(ArrayList<Edge<K, V>> edgeList) {
        this.edgeList = edgeList;
    }

    /**
     * @return the type
     */
    @Override
    public GraphType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    @Override
    public void setType(GraphType type) {
        this.type = type;
    }

    @Override
    public Vertex<K, V> insertVertex(K key, V value) {

        if (searchVertex(key) != null) {
            return null;
        }

        Vertex<K, V> vertex = new Vertex<K, V>(key, value);

        this.vertexList.add(vertex);
        return vertex;
    }

    @Override
    public Edge<K, V> insertEdge(Edge<K, V> edge) {

        if (!vertexList.contains(edge.getVertex1())) {
            vertexList.add(edge.getVertex1());
        }

        if (!vertexList.contains(edge.getVertex2())) {
            vertexList.add(edge.getVertex2());
        }

        switch (this.type) {
            case Simple -> {
                return insertSimpleEdge(edge);
            }
            case Directed -> {
                return insertDirectedEdge(edge);
            }
            case Multigraph -> {
                return insertMultigraphEdge(edge);
            }
            case Pseudograph -> {
                return insertPseudographEdge(edge);
            }
            case DirectedMultigraph -> {
                return insertMultiDirectedEdge(edge);
            }

            default -> {
                return null;
            }
        }

    }

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

        return edge;

    }

    private Edge<K, V> insertSimpleEdge(Edge<K, V> edge) {

        if (edge.getVertex2().equals(edge.getVertex1())) {
            return null;
        }

        if (edge.getVertex2().isConnected(edge.getVertex1())) {
            return null;
        }

        Vertex<K, V> vertex1 = edge.getVertex1();
        Vertex<K, V> vertex2 = edge.getVertex2();

        vertex1.getEdges().add(edge);
        vertex2.getEdges().add(new Edge<>(vertex2, vertex1, edge.getWeight()));

        edgeList.add(edge);

        return edge;
    }

    private Edge<K, V> insertDirectedEdge(Edge<K, V> edge) {

        if (edge.getVertex1().isConnected(edge.getVertex2())) {
            return null;
        }

        edge.getVertex1().addEdge(edge);
        edgeList.add(edge);

        return edge;
    }

    private Edge<K, V> insertMultigraphEdge(Edge<K, V> edge) {

        if (edge.getVertex2().equals(edge.getVertex1())) {
            return null;
        }

        edge.getVertex2().addEdge(new Edge<>(edge.getVertex2(), edge.getVertex1(), edge.getWeight()));
        edgeList.add(edge);

        return edge;
    }

    private Edge<K, V> insertPseudographEdge(Edge<K, V> edge) {
        edge.getVertex1().addEdge(edge);
        edge.getVertex2().addEdge(new Edge<>(edge.getVertex2(), edge.getVertex1(), edge.getWeight()));
        edgeList.add(edge);
        return edge;
    }

    private Edge<K, V> insertMultiDirectedEdge(Edge<K, V> edge) {
        edgeList.add(edge);
        return edge;
    }

    @Override
    public Vertex<K, V> removeVertex(Vertex<K, V> vertex) {
        if (vertex == null) {
            return null;
        }

        vertex.disconnect();

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
    public void BFS(Vertex<K, V> vertex) {

        for (Vertex<K, V> u : vertexList) {
            u.setColor(Color.WHITE);
            u.setDistance(Integer.MAX_VALUE);
            u.setPredecessor(null);
        }

        vertex.setColor(Color.GRAY);
        vertex.setDistance(0);
        vertex.setPredecessor(null);

        Queue<Vertex<K, V>> queue = new LinkedList<>();
        queue.add(vertex);

        while (!queue.isEmpty()) {
            Vertex<K, V> u = queue.poll();
            for (Edge<K, V> edge : u.getEdges()) {
                Vertex<K, V> v = edge.getVertex2();
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

    public boolean isConex() {
        BFS(vertexList.get(0));

        for (Vertex<K, V> vertex : vertexList) {
            if (vertex.getColor() == Color.WHITE) {
                return false;
            }
        }

        return true;
    }

    public boolean isStronglyConex() {
        for (Vertex<K, V> vertex : vertexList) {
            BFS(vertex);

            if (vertex.getEdges().isEmpty()) {
                return false;
            }

            for (Edge<K, V> edge : vertex.getEdges()) {
                if (edge.getVertex2().getColor() == Color.WHITE) {
                    return false;
                }
            }
        }

        return true;
    }

    /*
     * This method returns the total weight of the minimum spanning tree
     * performed with the DFS algorithm.
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

    private int DFSVisit(Vertex<K, V> vertex) {
        int totalWeight = 0;

        vertex.setColor(Color.GRAY);

        for (Edge<K, V> edge : vertex.getEdges()) {
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
    public List<Edge<K, V>> Dijkstra(Vertex<K, V> s) {
        s.setDistance(0);

        PriorityQueue<Vertex<K, V>> q = new PriorityQueue<>();

        for (Vertex<K, V> vertex : vertexList) {
            if (!vertex.equals(s)) {
                vertex.setDistance(Integer.MAX_VALUE);
            }
            vertex.setPredecessor(null);
            q.add(vertex);
        }

        while (!q.isEmpty()) {
            Vertex<K, V> u = q.poll();
            for (Edge<K, V> edge : u.getEdges()) {
                Vertex<K, V> v = edge.getVertex2();
                if (v.getDistance() > u.getDistance() + edge.getWeight()) {
                    v.setDistance(u.getDistance() + edge.getWeight());
                    v.setPredecessor(u);
                    q.remove(v);
                }
            }
        }

        List<Edge<K, V>> edgeList = new ArrayList<>();

        for (Vertex<K, V> vertex : vertexList) {
            if (!vertex.equals(s)) {
                edgeList.add(new Edge<>(vertex.getPredecessor(), vertex, vertex.getDistance()));
            }
        }

        return edgeList;

    }


    /**
     * Implements the Floyd-Warshall algorithm to find the shortest path between all pairs of vertices in a graph.
     */
    @Override
    public int[][] floydWarshall() {
        int numVertices = getVertexAmount();
    
        // Initialize the distance matrix with infinity for unconnected vertex pairs
        int[][] distances = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            Arrays.fill(distances[i], Integer.MAX_VALUE);
            distances[i][i] = 0;  // Distance from a vertex to itself is zero
        }
    
        // Fill the matrix with initial distances from the graph's edges
        for (Edge<K, V> edge : getEdgeList()) {
            int row = getVertexIndex(edge.getVertex1());
            int col = getVertexIndex(edge.getVertex2());
            distances[row][col] = edge.getWeight();
    
            // If the graph is undirected, update the opposite direction as well
            if (getType() != GraphType.Directed) {
                distances[col][row] = edge.getWeight();
            }
        }
    
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (distances[i][k] != Integer.MAX_VALUE && distances[k][j] != Integer.MAX_VALUE) {
                        distances[i][j] = Math.min(distances[i][j], distances[i][k] + distances[k][j]);
                    }
                }
            }
        }
    
        return distances;
    }
    

    private int getVertexIndex(Vertex<K, V> vertex) {
        return getVertexList().indexOf(vertex);
    }

    /**
     * Implements the Prim's algorithm to generate a Minimum Spanning Tree (MST)
     * starting from a given vertex.
     * 
     * @param begin The starting vertex for the MST generation.
     * @return A new graph representing the Minimum Spanning Tree.
     */
    @Override
    public IGraph<K, V> prim(Vertex<K, V> begin) {


        if (getVertexAmount() == 0) {
            return null;
        }

        GraphAL<K, V> minimumSpanningTree = new GraphAL<>(this.type);

        // Set of vertices already included in the Minimum Spanning Tree
        Set<Vertex<K, V>> includedVertices = new HashSet<>();

        // Priority queue to select the edge with the minimum weight in each iteration
        PriorityQueue<Edge<K, V>> minHeap = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));

        Vertex<K, V> currentVertex = begin;
        includedVertices.add(currentVertex);

        // Continue until all vertices are included in the Minimum Spanning Tree
        while (includedVertices.size() < getVertexAmount() && currentVertex != null) {

            
            for (Edge<K, V> edge : currentVertex.getEdges()) {
                if (!includedVertices.contains(edge.getVertex2())) {
                    minHeap.add(edge);
                }
            }


            Edge<K, V> minEdge = minHeap.poll();

            if (minEdge != null) {
                // Get the next vertex connected by the selected edge
                Vertex<K, V> nextVertex = minEdge.getVertex2();

                // Add the edge to the Minimum Spanning Tree
                minimumSpanningTree.insertEdgePrim(minEdge);

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

        GraphAL<K, V> minimumSpanningTree = new GraphAL<>(this.type);

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

            // If the vertices are in different sets, add the edge to the minimum spanning tree
            if (set1 != null && set2 != null && set1 != set2) {
                minimumSpanningTree.insertEdgePrim(edge);

                // Merge the disjoint sets
                set1.addAll(set2);
                disjointSets.remove(set2);
            }
        }

        return minimumSpanningTree;
    }

    @Override
    public Vertex<K, V> insertVertex(Vertex<K, V> vertex) {
        if (searchVertex(vertex.getKey()) != null) {
            return null;
        }

        this.vertexList.add(vertex);
        return vertex;
    }

}
