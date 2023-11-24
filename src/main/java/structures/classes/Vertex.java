package structures.classes;

import java.util.ArrayList;

import structures.enums.Color;
import structures.interfaces.IVertex;

public class Vertex<K, V> implements IVertex<K, V>, Comparable<Vertex<K, V>> {

    private K key;
    private V value;
    private ArrayList<Edge<K, V>> edges;

    // BFS and DFS
    private Color color;
    private int distance;
    private Vertex<K, V> predecessor;

    private int timeStampD;
    private int timeStampF;

    public Vertex(K key, V value) {
        this.key = key;
        this.value = value;
        this.edges = new ArrayList<Edge<K, V>>();

        this.color = Color.WHITE;
        this.distance = 0;
        this.predecessor = null;
        this.timeStampD = 0;
        this.timeStampF = 0;
    }

    public void addEdge(Edge<K, V> edge) {
        this.edges.add(edge);
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public int getDistance() {
        return distance;
    }

    @Override
    public Vertex<K, V> getPredecessor() {
        return predecessor;
    }

    @Override
    public int getTimeStampD() {
        return timeStampD;
    }

    @Override
    public int getTimeStampF() {
        return timeStampF;
    }

    @Override
    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public void setColor(Color white) {
        this.color = white;
    }

    @Override
    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public void setPredecessor(Vertex<K, V> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public void setTimeStampD(int timeStampD) {
        this.timeStampD = timeStampD;
    }

    @Override
    public void setTimeStampF(int timeStampF) {
        this.timeStampF = timeStampF;
    }

    @Override
    public void disconnect() {

        for (int i = edges.size() - 1; i >= 0; i--) {
            disconnectFrom(edges.get(i).getVertex2());
        }

    }

    /**
     * Disconnects this vertex from the specified vertex by removing the edge between them.
     * 
     * @param vertex the vertex to disconnect from
     */
    @Override
    public void disconnectFrom(Vertex<K, V> vertex) {

        this.edges.remove(getEdgeFrom(vertex));

        vertex.getEdges().remove(vertex.getEdgeFrom(this));

    }

    /**
     * Removes the edge between this vertex and the specified vertex.
     * If an edge is found, it is removed from the list of edges.
     *
     * @param vertex the vertex to remove the edge with
     */
    @Override
    public void removeEdge(Vertex<K, V> vertex) {
        for (Edge<K, V> edge : edges) {
            if (edge.getVertex2().equals(vertex)) {
                this.edges.remove(edge);
                break;
            }
        }
    }

    @Override
    public ArrayList<Edge<K, V>> getEdges() {
        return edges;
    }

    @Override
    public boolean isConnected(Vertex<K, V> vertex) {
        return getEdgeFrom(vertex) != null ? true : false;
    }

    /**
     * Returns the edge connecting this vertex to the specified vertex.
     *
     * @param vertex the vertex to find the edge to
     * @return the edge connecting this vertex to the specified vertex, or null if no such edge exists
     */
    @Override
    public Edge<K, V> getEdgeFrom(Vertex<K, V> vertex) {
        for (Edge<K, V> edge : edges) {
            if (edge.getVertex2().equals(vertex)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public int compareTo(Vertex<K, V> other) {
        return Integer.compare(this.getDistance(), other.getDistance());
    }

}
