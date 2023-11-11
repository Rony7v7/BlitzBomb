package structures.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import structures.enums.Color;
import structures.enums.GraphType;
import structures.interfaces.IGraph;

public class GraphAM<K, V> implements IGraph<K, V> {

    private ArrayList<Vertex<K, V>> vertexList;
    private ArrayList<Edge<K, V>> edgeList;
    private ArrayList<ArrayList<Edge<K, V>>> adjacencyMatrix;
    public GraphType type;

    public GraphAM(GraphType type) {
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
        this.adjacencyMatrix = new ArrayList<>();

        this.type = type;
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
    public GraphType getType() {
        return type;
    }

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

    private Edge<K, V> insertDirectedEdge(Edge<K, V> edge) {

        if (areConnected(edge.getVertex1(), edge.getVertex2())) {
            return null;
        }

        adjacencyMatrix.get(vertexList.indexOf(edge.getVertex1())).set(vertexList.indexOf(edge.getVertex2()), edge);

        edgeList.add(edge);

        return edge;
    }

    private Edge<K, V> insertMultigraphEdge(Edge<K, V> edge) {

        if (edge.getVertex2().equals(edge.getVertex1())) {
            return null;
        }

        Edge<K, V> prevEdgeAtoB = adjacencyMatrix.get(vertexList.indexOf(edge.getVertex1()))
                .get(vertexList.indexOf(edge.getVertex2()));
        Edge<K, V> prevEdgeBtoA = adjacencyMatrix.get(vertexList.indexOf(edge.getVertex2()))
                .get(vertexList.indexOf(edge.getVertex1()));
        Edge<K, V> edgeBtoA = new Edge<>(edge.getVertex2(), edge.getVertex1(), edge.getWeight());

        adjacencyMatrix.get(vertexList.indexOf(edge.getVertex1())).set(vertexList.indexOf(edge.getVertex2()), edge);
        adjacencyMatrix.get(vertexList.indexOf(edge.getVertex2())).set(vertexList.indexOf(edge.getVertex1()), edgeBtoA);

        if (prevEdgeAtoB != null) {
            edge.setNextEdge(prevEdgeAtoB);
            edgeBtoA.setNextEdge(prevEdgeBtoA);
        }

        edgeList.add(edge);

        return edge;
    }

    private Edge<K, V> insertPseudographEdge(Edge<K, V> edge) {
        Edge<K, V> prevEdgeAtoB = adjacencyMatrix.get(vertexList.indexOf(edge.getVertex1()))
                .get(vertexList.indexOf(edge.getVertex2()));
        Edge<K, V> prevEdgeBtoA = adjacencyMatrix.get(vertexList.indexOf(edge.getVertex2()))
                .get(vertexList.indexOf(edge.getVertex1()));
        Edge<K, V> edgeBtoA = new Edge<>(edge.getVertex2(), edge.getVertex1(), edge.getWeight());

        adjacencyMatrix.get(vertexList.indexOf(edge.getVertex1())).set(vertexList.indexOf(edge.getVertex2()), edge);
        adjacencyMatrix.get(vertexList.indexOf(edge.getVertex2())).set(vertexList.indexOf(edge.getVertex1()), edgeBtoA);

        if (prevEdgeAtoB != null) {
            edge.setNextEdge(prevEdgeAtoB);
            edgeBtoA.setNextEdge(prevEdgeBtoA);
        }

        edgeList.add(edge);
        return edge;
    }

    private Edge<K, V> insertMultiDirectedEdge(Edge<K, V> edge) {
        Edge<K, V> prevEdgeAtoB = adjacencyMatrix.get(vertexList.indexOf(edge.getVertex1()))
                .get(vertexList.indexOf(edge.getVertex2()));

        adjacencyMatrix.get(vertexList.indexOf(edge.getVertex1())).set(vertexList.indexOf(edge.getVertex2()), edge);

        if (prevEdgeAtoB != null) {
            edge.setNextEdge(prevEdgeAtoB);
        }

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

    private boolean areConnected(Vertex<K, V> vertex1, Vertex<K, V> vertex2) {
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
    public void DFS() {
        for (Vertex<K, V> vertex : vertexList) {
            vertex.setColor(Color.WHITE);
            vertex.setPredecessor(null);
        }

        for (Vertex<K, V> vertex : vertexList) {
            if (vertex.getColor() == Color.WHITE) {
                DFSVisit(vertex);
            }
        }

    }

    private void DFSVisit(Vertex<K, V> u) {
        u.setColor(Color.GRAY);
        u.setTimeStampD(u.getTimeStampD() + 1);
        for (Vertex<K, V> v : getAdjacents(u)) {
            if (v.getColor() == Color.WHITE) {
                v.setPredecessor(u);
                DFSVisit(v);
            }
        }
        u.setTimeStampF(u.getTimeStampF() + 1);
        u.setColor(Color.BLACK);
    }

    @Override
    public List<Edge<K, V>> Dijkstra(Vertex<K, V> s) {
        s.setDistance(0);
        s.setPredecessor(null);

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
            for (Vertex<K, V> v : getAdjacents(u)) {
                if (v.getDistance() > u.getDistance() + getEdge(u, v).getWeight()) {
                    v.setDistance(u.getDistance() + getEdge(u, v).getWeight());
                    v.setPredecessor(u);
                    q.remove(v);
                }
            }
        }

        List<Edge<K, V>> edgeList = new ArrayList<>();
        for (Vertex<K, V> vertex : vertexList) {
            if (vertex.getPredecessor() != null) {
                edgeList.add(getEdge(vertex.getPredecessor(), vertex));
            }
        }

        return edgeList;

    }

    @Override
    public void FloydWarshall() {
    }

    @Override
    public void Prim(Vertex<K, V> s) {
    }

    @Override
    public void Kruskal() {
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
