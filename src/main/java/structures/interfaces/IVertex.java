package structures.interfaces;

public interface IVertex<K,V> {
    public K getKey();
    public V getValue();
    public void setKey(K key);
    public void setValue(V value);
}