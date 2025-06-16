package org.xtracat.server;

import org.xtracat.datatypes.Coordinates;

public class CoordinatesBuilder {

    private Long x;
    private Integer y;

    public CoordinatesBuilder() {
    }

    public CoordinatesBuilder setX(long x) {
        this.x = x;
        return this;
    }

    public CoordinatesBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public Coordinates build() {
        try {
            Coordinates coordinates = new Coordinates(this.x, this.y);
            coordinates.validate();
            return coordinates;
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Error building Coordinates: " + e.getMessage());
            return null;
        }
    }

    public Coordinates build(long x, int y) {
        try {
            Coordinates coordinates = new Coordinates(x, y);
            coordinates.validate();
            return coordinates;
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Error building Coordinates: " + e.getMessage());
            return null;
        }
    }
}