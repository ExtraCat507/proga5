package org.xtracat.datatypes;

public class Coordinates {
    private Long x; //Максимальное значение поля: 517
    private Integer y; //Максимальное значение поля: 822

    public Coordinates(Long x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates() {
        new Coordinates(0L, 0);
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void validate() throws IllegalArgumentException {
        if(x == null || y == null){
            throw new IllegalArgumentException();
        }
        if (x > 517 || y > 822) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString(){
        return "Координаты: x:" + getX() + " y:" + getY();
    }
}
