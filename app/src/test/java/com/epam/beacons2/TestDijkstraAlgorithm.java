package com.epam.beacons2;

import com.epam.beacons2.dijkstra.engine.DijkstraAlgorithm;
import com.epam.beacons2.dijkstra.manager.Graph;
import com.epam.beacons2.dijkstra.model.Edge;
import com.epam.beacons2.dijkstra.model.Vertex;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class TestDijkstraAlgorithm {
    Graph graph;

    @Before
    public void create() {
        this.graph = new Graph();
        for (int i = 0; i < 11; i++)
            graph.addVertex(new Vertex(i, new LatLng((double)(i+100), (double)(i+100))));

        graph.addEdge(0, 1, 85).
                addEdge(1, 2, 217).
                addEdge(2, 3, 173).
                addEdge(3, 4, 186).
                addEdge(4, 5, 103).
                addEdge(5, 6, 183).
                addEdge(6, 7, 250).
                addEdge(7, 8, 84).
                addEdge(8, 9, 167).
                addEdge(9, 10, 100).
                addEdge(7, 10, 40).
                addEdge(8, 10, 70);

        // graph.addEdge(2, 10, 30);

    }

    @Test
    public void testExecute() {
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(0);
        List<Vertex> path = dijkstra.getPath(10);
        String str = "";
        int total = 0;
        for (int i = 0; i < path.size(); i++) {
            Edge cur = graph.getEdge(path.get(i).getId(), i + 1 != path.size() ? path.get(i + 1).getId() : Integer.MAX_VALUE);
            if (cur != null)
                total += cur.getWeight();
            str = str + "vertex " + path.get(i) + "|  chosen edge: " + cur + " -> \n";
            System.out.println("vertex " + path.get(i) + "|  chosen edge: " + cur + " -> ");

            for (Edge e : graph.getEdgesListBySource(path.get(i).getId())) {
                str += "       " + e.toString() + "\n";
                System.out.println("     " + e);
            }
            System.out.println("------------------------");
        }
        System.out.println("Total weight comprises " + total);
        str += "\n------------------------\nTotal weight comprises " + total;
        System.out.println(str);


    }
}