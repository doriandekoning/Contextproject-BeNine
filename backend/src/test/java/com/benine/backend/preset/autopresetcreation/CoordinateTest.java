package com.benine.backend.preset.autopresetcreation;//TODO add Javadoc comment

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 */
public class CoordinateTest {

  @Test (expected = AssertionError.class)
  public void testConstructorXSmaller0() {
    Coordinate c = Mockito.spy(new Coordinate(-1.2, 1.2));
  }

  @Test (expected = AssertionError.class)
  public void testConstructorYSmaller0() {
    Coordinate c = Mockito.spy(new Coordinate(1.2, -1.2));
  }

  @Test (expected = AssertionError.class)
  public void testConstructorXLarger110() {
    Coordinate c = Mockito.spy(new Coordinate(1.2, 103));
  }

  @Test (expected = AssertionError.class)
  public void testConstructorYLarger100() {
    Coordinate c = Mockito.spy(new Coordinate(1.2, 103));
  }

  @Test
  public void testSetX() {
    Coordinate c = new Coordinate(1, 1);
    c.setX(90);
    Assert.assertEquals(90, c.getX(), 0.05);
  }

  @Test
  public void testSetY() {
    Coordinate c = new Coordinate(1, 1);
    c.setY(90);
    Assert.assertEquals(90, c.getY(), 0.05);
  }

  @Test
  public void testSetXSmaller0() {
    Coordinate c = new Coordinate(1, 2);
    try {
      c.setX(-10);
      // Only get here if no assertion exception is thrown
      Assert.fail();
    } catch (AssertionError e) {
      // Not using (expected =...) so we can check the x value has not changed
      Assert.assertEquals(1, c.getX(), 0.05);
    }
  }

  @Test
  public void testSetYSmaller0() {
    Coordinate c = new Coordinate(1, 2);
    try {
      c.setY(-10);
      // Only get here if no assertion exception is thrown
      Assert.fail();
    } catch (AssertionError e) {
      // Not using (expected =...) so we can check the x value has not changed
      Assert.assertEquals(2, c.getY(), 0.05);
    }
  }

  @Test
  public void testSetXLarger100() {
    Coordinate c = new Coordinate(1, 2);
    try {
      c.setX(110);
      // Only get here if no assertion exception is thrown
      Assert.fail();
    } catch (AssertionError e) {
      // Not using (expected =...) so we can check the x value has not changed
      Assert.assertEquals(1, c.getX(), 0.05);
    }
  }

  @Test
  public void testSetYLarger100() {
    Coordinate c = new Coordinate(1, 2);
    try {
      c.setY(110);
      // Only get here if no assertion exception is thrown
      Assert.fail();
    } catch (AssertionError e) {
      // Not using (expected =...) so we can check the x value has not changed
      Assert.assertEquals(2, c.getY(), 0.05);
    }
  }
}
