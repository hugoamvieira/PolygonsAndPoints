package io.github.hugoamvieira.polygonsandpoints;

import java.io.Serializable;

public class Polygon implements Serializable {
    private Point[] polygonPoints;
    private String name;

    public Polygon(Point[] _polygonPoints, String _name) {
        this.polygonPoints = _polygonPoints;
        this.name = _name;
    }

    public Point[] getPolygonPoints() {
        return polygonPoints;
    }

    public void setPolygonPoints(Point[] polygonPoints) {
        this.polygonPoints = polygonPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
