package structures.classes;

import structures.interfaces.IEdge;

public class Edge<K, V> implements IEdge<K, V> {

    private Vertex<K, V> vertex1;
    private Vertex<K, V> vertex2;
    private int weight;

    // GraphAM
    private Edge<K, V> nextEdge;

    public Edge(Vertex<K, V> vertex1, Vertex<K, V> vertex2, int weight) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
        this.nextEdge = null;
    }

    @Override
    public Vertex<K, V> getVertex1() {
        return vertex1;
    }

    @Override
    public Vertex<K, V> getVertex2() {
        return vertex2;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setVertex1(Vertex<K, V> vertex) {
        this.vertex1 = vertex;
    }

    @Override
    public void setVertex2(Vertex<K, V> vertex) {
        this.vertex2 = vertex;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Edge<K, V> getNextEdge() {
        return nextEdge;
    }

    public void setNextEdge(Edge<K, V> nextEdge) {
        this.nextEdge = nextEdge;
    }
}
