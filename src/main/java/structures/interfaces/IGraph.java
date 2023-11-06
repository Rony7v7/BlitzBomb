package structures.interfaces;

import java.util.ArrayList;

import structures.classes.Edge;
import structures.classes.Vertex;
import structures.enums.GraphType;

public interface IGraph<K,V> {
    
    public int getVertexAmount();
    public int getEdgesAmount();
    public ArrayList<Vertex<K,V>> getVertexList();
    public void setVertexList(ArrayList<Vertex<K, V>> vertexList);
    public ArrayList<Edge<K, V>> getEdgeList();
    public void setEdgeList(ArrayList<Edge<K, V>> edgeList);
    public GraphType getType();
    public void setType(GraphType type);
    public Vertex<K,V> insertVertex(K key, V value);
    public Edge<K,V> insertEdge(Edge<K,V> edge);
    public Vertex<K,V> removeVertex(Vertex<K,V> vertex);
    public Vertex<K,V> searchVertex(K key);
    public void BFS(Vertex<K,V> s);

}
