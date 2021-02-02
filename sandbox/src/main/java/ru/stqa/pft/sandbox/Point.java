package ru.stqa.pft.sandbox;

public class Point {

  public double x;
  public double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double distance(Point other) {
    return (Math.sqrt(Math.pow((other.x - this.x),2) + Math.pow((other.y - this.y),2)));
  }

}
