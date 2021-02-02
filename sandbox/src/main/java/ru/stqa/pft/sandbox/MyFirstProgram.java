package ru.stqa.pft.sandbox;

public class MyFirstProgram {

  public static void main(String[] args) {
    hello("world");
    hello("user");
    hello("Elena");

    Square s = new Square(5);
    System.out.println("Площадь квадрата со стороной " + s.l + " = " + s.area());

    Rectangle r = new Rectangle(4, 6);
    System.out.println("Площадь прямоугольника со сторонами " + r.a + " и " + r.b + " = " + r.area());

    Point p1 = new Point(1, 3);
    Point p2 = new Point(8, 5);

    System.out.println("Точка 1 имеет координаты: " + p1.x + " и " + p1.y);
    System.out.println("Точка 2 имеет координаты: " + p2.x + " и " + p2.y);
    System.out.println("Расстояние между этими двумя точками = " + distance(p1, p2) + " (вычислено функцией)");
    System.out.println("Расстояние между этими двумя точками = " + p1.distance(p2) + " (вычислено методом класса)");
  }

  public static void hello(String somebody) {
    System.out.println("Hello, " + somebody + "!");
  }

  public static double distance(Point p1, Point p2) {
    return (Math.sqrt((Math.pow((p2.x - p1.x),2)) + Math.pow((p2.y - p1.y),2)));
  }

}