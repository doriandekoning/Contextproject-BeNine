package com.benine.backend.camera.ipcameracontrol;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test the iris value class.
 */
public class IrisValueTest {
  
  IrisValue iris;
  
  @Before
  public void setup() {
    iris = new IrisValue(1, true);
  }
  
  @Test
  public void testGetirisPosition() {
    Assert.assertEquals(1, iris.getIris());
  }
  
  @Test
  public void testGetAutoiris() {
    Assert.assertEquals(true, iris.isAutoiris());
  }
  
  @Test
  public void testSetAutoiris() {
    iris.setAutoiris(false);
    Assert.assertEquals(false, iris.isAutoiris());
  }
  
  @Test
  public void testSetiris() {
    iris.setIris(5);
    Assert.assertEquals(5, iris.getIris());
  }
  
  @Test
  public void testNotEqualsNull() {
    Assert.assertNotEquals(null, iris);
  }
  
  @Test
  public void testNotEqualsAutoiris() {
    IrisValue iris2 = new IrisValue(1, false);
    Assert.assertNotEquals(iris2, iris);
  }
  
  @Test
  public void testNotEqualsirisPosition() {
    IrisValue iris2 = new IrisValue(2, true);
    Assert.assertNotEquals(iris2, iris);
  }
  
  @Test
  public void testNotEqualsOne() {
    Assert.assertNotEquals(1, iris);
  }
  
  @Test
  public void testEqualsSelf() {
    Assert.assertEquals(iris, iris);
  }
  
  @Test
  public void testEqualsOther() {
    IrisValue iris2 = new IrisValue(1, true);
    Assert.assertEquals(iris2, iris);
  }
  
  @Test
  public void testEqualsHashCode() {
    IrisValue iris2 = new IrisValue(1, true);
    Assert.assertEquals(iris2.hashCode(), iris.hashCode());
  }
}
