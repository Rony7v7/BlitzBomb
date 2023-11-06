package structures.interfaces;

import java.util.ArrayList;

import structures.classes.Edge;
import structures.classes.Vertex;
import structures.enums.Color;

public interface IVertex<K,V> {
    public K getKey();
    public V getValue();
    public void setKey(K key);
    public void setValue(V value);
    public Color getColor();
    public void setColor(Color color);
    public int getDistance();
    public void setDistance(int distance);
    public Vertex<K,V> getPredecessor();
    public void setPredecessor(Vertex<K,V> predecessor);
    public int getTimeStampD();
    public void setTimeStampD(int timeStampD);
    public int getTimeStampF();
    public void setTimeStampF(int timeStampF);
    public ArrayList<Edge<K,V>> getEdges();
    public void disconnect();
    public void disconnectFrom(Vertex<K,V> vertex);
    public void removeEdge(Vertex<K,V> vertex);
    public boolean isConnected(Vertex<K,V> vertex);
    public Edge<K,V> getEdgeFrom(Vertex<K,V> vertex);

}