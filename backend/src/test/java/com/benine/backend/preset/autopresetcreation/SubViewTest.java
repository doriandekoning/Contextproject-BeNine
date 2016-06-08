package com.benine.backend.preset.autopresetcreation;//TODO add Javadoc comment

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 */
public class SubViewTest {

  @Test
  public void testGetTopLeft() {
    Coordinate c = Mockito.mock(Coordinate.class);
    SubView subview = new SubView(c, new Coordinate(90, 10));
    Assert.assertEquals(c, subview.getTopLeft());
  }

  @Test
  public void testGetBottomRight() {
    Coordinate c = Mockito.mock(Coordinate.class);
    SubView subview = new SubView(new Coordinate(90, 10), c);
    Assert.assertEquals(c, subview.getTopLeft());
  }

  @Test (expected = AssertionError.class)
  public void testTopLeftSmallerY() {
    Coordinate c = Mockito.mock(Coordinate.class);
    Mockito.when(c.getY()).thenReturn(0.0);
    new SubView(c, new Coordinate(90, 10));
  }

  @Test (expected = AssertionError.class)
  public void testBottomRightSmallerX() {
    Coordinate c = Mockito.mock(Coordinate.class);
    Mockito.when(c.getX()).thenReturn(50.0);
    new SubView(c, new Coordinate(10, 10));
  }
}
