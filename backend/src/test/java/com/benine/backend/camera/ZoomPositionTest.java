package com.benine.backend.camera;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ZoomPositionTest {

  @Test
  public final void testGetZoomTest() {
    ZoomPosition pos = new ZoomPosition(10, 20, 50);
    Assert.assertEquals(50, pos.getZoom());
  }

  @Test
  public final void testSetZoomTest() {
    ZoomPosition pos = new ZoomPosition(10, 20, 50);
    pos.setZoom(60);
    Assert.assertEquals(60, pos.getZoom());
  }

  @Test
  public final void testPositionConstructor() {
    ZoomPosition actualPos = new ZoomPosition(new Position(50, 20), 50);
    ZoomPosition expectedPos = new ZoomPosition(50, 20, 50);
    Assert.assertEquals(actualPos, expectedPos);
  }

  @Test
  public final void testEqualsEquals() {
    ZoomPosition pos1 = new ZoomPosition(new Position(50, 20), 50);
    ZoomPosition pos2 = new ZoomPosition(new Position(50, 20), 50);
    Assert.assertEquals(pos1, pos2);
  }

  @Test
  public final void testEqualsNotEqualZoom() {
    ZoomPosition pos1 = new ZoomPosition(new Position(50, 20), 3);
    ZoomPosition pos2 = new ZoomPosition(new Position(50, 20), 50);
    Assert.assertNotEquals(pos1, pos2);
  }

  @Test
  public final void testEqualsNotEqualPosition() {
    ZoomPosition pos1 = new ZoomPosition(new Position(3, 20), 50);
    ZoomPosition pos2 = new ZoomPosition(new Position(50, 20), 50);
    Assert.assertNotEquals(pos1, pos2);
  }

  @Test
  public final void testEqualsNotEqualNull() {
    ZoomPosition pos1 = new ZoomPosition(new Position(3, 20), 50);
    Assert.assertNotEquals(pos1, null);
  }

  @Test
  public final void testEqualsNotEqualOtherObject() {
    ZoomPosition pos1 = new ZoomPosition(new Position(3, 20), 50);
    Position pos2 = new Position(50, 20);
    Assert.assertNotEquals(pos1, pos2);
  }

  @Test
  public final void testHashCode() {
    int pos1Hash = new ZoomPosition(new Position(3, 20), 50).hashCode();
    int pos2Hash = new ZoomPosition(new Position(3, 20), 50).hashCode();
    Assert.assertEquals(pos1Hash, pos1Hash);

  }

  @Test
  public final void testToString() {
    ZoomPosition pos = new ZoomPosition(2, 3, 4);
    Assert.assertEquals("ZoomPosition{zoom=4,pan=2.0,tilt=3.0}", pos.toString());
  }
}
