package structures.interfaces;

import java.util.ArrayList;
import java.util.List;

import structures.classes.Edge;
import structures.classes.Vertex;

public interface IGraph<K, V> {

    public int getVertexAmount();

    public int getEdgesAmount();

    public ArrayList<Vertex<K, V>> getVertexList();

    public void setVertexList(ArrayList<Vertex<K, V>> vertexList);

    public ArrayList<Edge<K, V>> getEdgeList();

    public void setEdgeList(ArrayList<Edge<K, V>> edgeList);

    public Vertex<K, V> insertVertex(K key, V value);

    public Vertex<K, V> insertVertex(Vertex<K, V> vertex);

    public Edge<K, V> insertEdge(Edge<K, V> edge);

    public Edge<K, V> insertEdgePrim(Edge<K, V> edge);

    public Vertex<K, V> removeVertex(Vertex<K, V> vertex);

    public Vertex<K, V> searchVertex(K key);

    public boolean areConnected(Vertex<K, V> vertex1, Vertex<K, V> vertex2);

    public void BFS(Vertex<K, V> s);

    public int DFS(IGraph<K, V> minimumSpanningTree);

    public List<Edge<K, V>> Dijkstra(Vertex<K, V> s, Vertex<K, V> e);

    public int[][] floydWarshall();

    public IGraph<K, V> prim(Vertex<K, V> begin);

    public IGraph<K, V> kruskal();

}