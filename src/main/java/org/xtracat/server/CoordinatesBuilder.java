package org.xtracat.server;

import org.xtracat.datatypes.Coordinates;

public class CoordinatesBuilder {
    CollectionManager cm;

    public CoordinatesBuilder(CollectionManager cm) {
        this.cm = cm;
    }

    public Coordinates build(long x, int y) {
        Coordinates coordinates = new Coordinates(x, y);
        coordinates.validate();
        return coordinates;
    }
}
