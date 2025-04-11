package org.xtracat.server;

import org.xtracat.datatypes.Label;

public class LabelBuilder {

    public LabelBuilder() {
    }

    public Label build(long bands, double sales) {
        Label label = new Label(bands, sales);
        label.validate();
        return label;
    }
}
