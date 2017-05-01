package io.github.hugoamvieira.polygonsandpoints;

import java.io.Serializable;

public class Point implements Serializable {

    private double latitude;
    private double longitude;
    private String name;

    public Point(double _latitude, double _longitude, String _name) {
        this.latitude = _latitude;
        this.longitude = _longitude;
        this.name = _name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
