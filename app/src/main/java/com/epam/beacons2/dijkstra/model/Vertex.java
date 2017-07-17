package com.epam.beacons2.dijkstra.model;

import com.google.android.gms.maps.model.LatLng;

public class Vertex {
    private int id;
    private String name;
    private LatLng latLng;

    public Vertex(int id,  LatLng latLng) {
        this.id = id;
        this.latLng = latLng;
    }
    public Vertex(int id) {
        this.id = id;
    }

    public Vertex(int id, String name, LatLng latLng) {
        this.id = id;
        this.name = name;
        this.latLng = latLng;
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

    public LatLng getLatLng() {
        return latLng;
    }
}