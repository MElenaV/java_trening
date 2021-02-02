package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PointTest {

  @Test
  public void PositiveTestForStaticMethod(){
    Point p1 = new Point(1, 3);
    Point p2 = new Point(8, 5);
    Assert.assertEquals(MyFirstProgram.distance(p1,p2), 7.280109889280518);
  }


  @Test
  public void NegativeTestForStaticMethod(){
    Point p1 = new Point(0, 0);
    Point p2 = new Point(0, 0);
    Assert.assertEquals(MyFirstProgram.distance(p1,p2), 0);
  }

  @Test
  public void PositiveTestForNotStaticMethod(){
    Point p1 = new Point(1, 3);
    Point p2 = new Point(8, 5);
    Assert.assertEquals(p1.distance(p2), 7.280109889280518);
  }

  @Test
  public void NegativeTestForNotStaticMethod(){
    Point p1 = new Point(0, 0);
    Point p2 = new Point(0, 0);
    Assert.assertNotEquals(p1.distance(p2), 0);
  }

  @Test
  public void ZeroDistance(){
    Point p1 = new Point(4, 7);
    Point p2 = new Point(4, 7);
    Assert.assertEquals(p1.distance(p2), 0);
  }

  @Test
  public void CoordsNegative(){
    Point p1 = new Point(-1, -3);
    Point p2 = new Point(-8, -5);
    Assert.assertEquals(p1.distance(p2), 7.280109889280518);
  }

  @Test
  public void CoordsNegativePositiveZero(){
    Point p1 = new Point(-1, 3);
    Point p2 = new Point(0, -5);
    Assert.assertEquals(p1.distance(p2), 8.06225774829855);
  }




}
