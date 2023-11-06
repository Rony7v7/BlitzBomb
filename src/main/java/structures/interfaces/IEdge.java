package structures.interfaces;

import structures.classes.Vertex;

public interface IEdge<K,V> {
    public Vertex<K,V> getVertex1();
    public Vertex<K,V> getVertex2();
    public int getWeight();
    public void setVertex1(Vertex<K,V> vertex);
    public void setVertex2(Vertex<K,V> vertex);
    public void setWeight(int weight);
}
