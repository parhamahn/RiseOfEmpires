package model;

public class Direction {
    private double dx;
    private double dy;

    public Direction(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

   public double getDx() {
       return dx;
   }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

}
