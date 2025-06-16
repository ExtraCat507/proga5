package org.xtracat.server;

import org.xtracat.datatypes.MusicLabel;

public class LabelBuilder {

    private Long bands;
    private Double sales;

    public LabelBuilder() {
    }

    public LabelBuilder setBands(long bands) {
        this.bands = bands;
        return this;
    }

    public LabelBuilder setSales(double sales) {
        this.sales = sales;
        return this;
    }

    public MusicLabel build() {
        try {
            MusicLabel musicLabel = new MusicLabel(this.bands, this.sales);
            musicLabel.validate();
            return musicLabel;
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Error building MusicLabel: " + e.getMessage());
            return null;
        }
    }

    public MusicLabel build(long bands, double sales) {
        try {
            MusicLabel musicLabel = new MusicLabel(bands, sales);
            musicLabel.validate();
            return musicLabel;
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Error building MusicLabel: " + e.getMessage());
            return null;
        }
    }
}