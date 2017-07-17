package com.epam.beacons2.dijkstra.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.epam.beacons2.dijkstra.model.Edge;
import com.epam.beacons2.dijkstra.model.Vertex;

public class Graph {
    private List<Vertex> vertexesList = new ArrayList<>();
    private List<Edge> edgesList = new ArrayList<>();

    public Graph addVertex(Vertex vertex) {
        if (vertexesList.contains(vertex))
            throw new RuntimeException("Vertex with such ID is already in existence");
        this.vertexesList.add(vertex);
        return this;
    }

    public Graph addVertex(int vertexId) {
        Vertex temp = new Vertex(vertexId);
        if (vertexesList.contains(temp))
            throw new RuntimeException("Vertex with such ID is already in existence");
        this.vertexesList.add(temp);
        return this;
    }

    public Graph removeVertex(Vertex vertex) {
        this.vertexesList.remove(vertex);
        return this;
    }

    public Graph removeVertex(int vertexId) {
        this.vertexesList.remove(new Vertex(vertexId));
        return this;
    }

    public Vertex getVertexById(int vertexId) {
        return this.vertexesList.get(vertexesList.indexOf(new Vertex(vertexId)));
    }

    public boolean containsVertex(int vertexId) {
        return vertexesList.contains(new Vertex(vertexId));
    }

    private boolean containsTwoVertexes(int sourceVertexId, int destinationVertexId) {
        return vertexesList.contains(new Vertex(sourceVertexId)) && vertexesList.contains(new Vertex(sourceVertexId));
    }

    public Graph addEdge(Edge edge) {
        if (!containsTwoVertexes(edge.getSource().getId(), edge.getDestination().getId()))
            throw new RuntimeException("There is no such couple of source-destination for this edge");

        if (edgesList.contains(edge))
            throw new RuntimeException("Edge with such source-destination couple is already in existence");
        this.edgesList.add(edge);
        return this;
    }

    public Graph addEdge(Vertex source, Vertex destination, int weight) {
        Edge temp = new Edge(source, destination, weight);
        if (!containsTwoVertexes(source.getId(), destination.getId()))
            throw new RuntimeException("There is no such couple of source-destination for this edge");
        if (edgesList.contains(temp))
            throw new RuntimeException("Edge with such source-destination couple is already in existence");

        this.edgesList.add(temp);
        return this;
    }

    public Graph addEdge(int sourceVertexId, int destinationVertexId, int weight) {
        Edge temp = new Edge(this.getVertexById(sourceVertexId), this.getVertexById(destinationVertexId), weight);
        if (!containsTwoVertexes(sourceVertexId, destinationVertexId))
            throw new RuntimeException("There is no such couple of source-destination for this edge");
        if (edgesList.contains(temp))
            throw new RuntimeException("Edge with such source-destination couple is already in existence");
        this.edgesList.add(temp);
        return this;
    }

    public Graph removeEdge(Edge edge) {
        this.edgesList.remove(edge);
        return this;
    }

    public Graph removeEdge(int sourceVertexId, int destinationVertexId) {
        Edge temp = new Edge(new Vertex(sourceVertexId), new Vertex(destinationVertexId), Integer.MAX_VALUE);
        this.edgesList.remove(temp);
        return this;
    }

    public Edge getEdge(int sourceVertexId, int destinationVertexId) {
        for (Edge e : edgesList)
            if (e.getSource().getId() == sourceVertexId && e.getDestination().getId() == destinationVertexId)
                return e;
        return null;
    }

    public List<Vertex> getVertexesList() {
        return Collections.unmodifiableList(vertexesList);
    }

    public List<Integer> getIdVertexesList() {
        List<Integer> list = new ArrayList<>();
        for (Vertex v : vertexesList)
            list.add(v.getId());
        return list;
    }

    public List<Edge> getEdgesListBySource() {
        Collections.sort(edgesList, (Edge e1, Edge e2) -> Integer.compare(e1.getSource().getId(), e2.getSource().getId()));
        return Collections.unmodifiableList(edgesList);
    }

    public List<Edge> getEdgesListBySource(Integer... sources) {
        List<Edge> list = new ArrayList<>();
        for (Edge e : edgesList)
            for (int i = 0; i < sources.length; i++)
                if (e.getSource().getId() == sources[i])
                    list.add(e);
        Collections.sort(list, (Edge e1, Edge e2) -> Integer.compare(e1.getSource().getId(), e2.getSource().getId()));
        return list;
    }

    public List<Edge> getEdgesListByDestination() {
        Collections.sort(edgesList, (Edge e1, Edge e2) -> Integer.compare(e1.getDestination().getId(), e2.getDestination().getId()));
        return Collections.unmodifiableList(edgesList);
    }

    public List<Edge> getEdgesListByDestination(Integer... destinations) {
        List<Edge> list = new ArrayList<>();
        for (Edge e : edgesList)
            for (int i = 0; i < destinations.length; i++)
                if (e.getSource().getId() == destinations[i])
                    list.add(e);
        Collections.sort(list, (Edge e1, Edge e2) -> Integer.compare(e1.getDestination().getId(), e2.getDestination().getId()));
        return list;
    }
}