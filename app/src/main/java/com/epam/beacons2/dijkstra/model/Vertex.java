package com.epam.beacons2.dijkstra.model;

import com.google.android.gms.maps.model.LatLng;

public class Vertex {
    private int id;
    private LatLng latLng;
    private String name;


    public Vertex(int id) {
        this.id = id;
    }

    public Vertex(int id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        latLng = new LatLng(latitude, longitude);
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name != null ? name : "noName";
    }

    @Override
    public String toString() {
        return "id="+ id +"(" + getName()+")";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;

        Vertex vertex = (Vertex) o;

        return id == vertex.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}