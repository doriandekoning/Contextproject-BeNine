package com.benine.backend.preset.autopresetcreation;//TODO add Javadoc comment

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

/**
 *
 */
public class SubViewTest {

  @Test (expected = AssertionError.class)
  public void testConstructorBottomHigher() {
    new SubView(10, 40, 50, 50);
  }

  @Test (expected = AssertionError.class)
  public void testConstructorRightBottomLeftOfLeftTop() {
    new SubView(70, 90, 50, 50);
  }

  @Test
  public void testConstructorNoCoordinates() {
    SubView subView = new SubView(10, 90, 45, 55);
    Assert.assertEquals(new Coordinate(10, 90), subView.getTopLeft());
    Assert.assertEquals(new Coordinate(45, 55), subView.getBottomRight());
  }

  @Test
  public void testGetTopLeft() {
    Coordinate c = new Coordinate (10, 90);
    SubView subview = new SubView(c, new Coordinate(90, 10));
    Assert.assertEquals(c, subview.getTopLeft());
  }

  @Test
  public void testGetBottomRight() {
    Coordinate c = new Coordinate (90, 10);
    SubView subview = new SubView(new Coordinate(10, 90), c);
    Assert.assertEquals(c, subview.getBottomRight());
  }

  @Test (expected = AssertionError.class)
  public void testTopLeftSmallerY() {
    Coordinate c = Mockito.mock(Coordinate.class);
    when(c.getY()).thenReturn(0.0);
    new SubView(c, new Coordinate(90, 10));
  }

  @Test (expected = AssertionError.class)
  public void testBottomRightSmallerX() {
    Coordinate c = Mockito.mock(Coordinate.class);
    when(c.getX()).thenReturn(50.0);
    new SubView(c, new Coordinate(10, 10));
  }

  @Test
  public void testGetWidth() {
    SubView subView = new SubView(new Coordinate(10, 50), new Coordinate(60, 20));
    Assert.assertEquals(50, subView.getWidth(), 0.05);
  }

  @Test
  public void testGetHeight() {
    SubView subView = new SubView(new Coordinate(10, 50), new Coordinate(60, 20));
    Assert.assertEquals(30, subView.getHeight(), 0.05);
  }

  @Test
  public void testBottomRightNotEqual() {
    Coordinate c1 = new Coordinate(2.4, 6.4);
    Coordinate c2 = new Coordinate(5.3, 4.3);
    Assert.assertNotEquals(new SubView(c1, c2),  new SubView(c1, new Coordinate(5.4, 4.3)));
  }

  @Test
  public void testTopLeftNotEqual() {
    Coordinate c1 = new Coordinate(2.4, 6.4);
    Coordinate c2 = new Coordinate(5.3, 4.3);
    Assert.assertNotEquals(new SubView(c1, c2),  new SubView(new Coordinate(2.4, 6.3), c2));
  }

  @Test
  public void testEqualsOtherObject() {
    SubView view = new SubView(new Coordinate(1, 5), new Coordinate(2, 4));
    Assert.assertNotEquals(view, 3);
  }

  @Test
  public void testEqualsNUll() {
    SubView view = new SubView(new Coordinate(1, 5), new Coordinate(2, 4));
    Assert.assertNotEquals(view, null);
  }


  @Test
  public void testEquals() {
    Coordinate c1 = new Coordinate(1.4, 5.3);
    Coordinate c2 = new Coordinate(3.4, 3.4);
    Assert.assertEquals(new SubView(c1,c2), new SubView(c1, c2));
  }

  @Test
  public void testEqualsSameObject() {
    SubView view = new SubView(new Coordinate(1.4, 5.3), new Coordinate(3.3, 3.2));
    Assert.assertEquals(view, view);
  }

  @Test
  public void testHashCode() {
    Coordinate c1 = new Coordinate(2.3, 5.3);
    Coordinate c2 = new Coordinate(5.3, 3.3);
    SubView subview1 = new SubView(c1, c2);
    SubView subview2 = new SubView(c1, c2);
    Assert.assertEquals(subview1.hashCode(), subview2.hashCode());
  }

  @Test
  public void testToString() {
    SubView subView = new SubView(10, 100, 20, 20);
    String expected = "SubView{topLeft="+subView.getTopLeft().toString()+", bottomRight="+subView.getBottomRight().toString()+"}";
    Assert.assertEquals(expected, subView.toString());
  }
}
