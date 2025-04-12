package org.xtracat.datatypes;

import java.io.Serializable;

public class Label implements Comparable, Serializable {
    private Long bands;
    private Double sales; //Значение поля должно быть больше 0

    public Label(){}

    public Label(Long bands, Double sales) {
        this.bands = bands;
        this.sales = sales;
    }

    public Long getBands() {
        return bands;
    }

    public void setBands(long bands) {
        this.bands = bands;
    }

    public Double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    @Override
    public int compareTo(Object o) { //  сортировка пр кол-ву банд
        if (o.getClass() != Label.class) {
            throw new ClassCastException();
        }

        if (this.getBands() > ((Label) o).getBands()) {
            return 1;
        } else if (this.getBands() < ((Label) o).getBands()) {
            return -1;
        } else {
            //if(this.name < ((MusicBand) o).name)
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Label: кол-во групп: " + bands + "; Продажи: " + sales + "\n";
    }

    public void validate() throws IllegalArgumentException {
        if(sales == null || bands == null) throw new IllegalArgumentException();
        if (sales <= 0) throw new IllegalArgumentException();
    }
}
