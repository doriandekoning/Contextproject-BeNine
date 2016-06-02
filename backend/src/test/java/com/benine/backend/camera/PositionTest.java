package com.benine.backend.camera;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.benine.backend.camera.ipcameracontrol.ZoomPosition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PositionTest {

  private Position position;

  @Before
  public void setUp() {
    position = new Position(1, 2);
  }

  @Test
  public void testSetPan() {
    position.setPan(42);
    Assert.assertEquals(42, position.getPan(), 0);
  }

  @Test
  public void testSetTilt() {
    position.setTilt(42);
    Assert.assertEquals(42, position.getTilt(), 0);
  }
    
  @Test
  public void testNotEqualsNull() {
    Position position1 = new Position(1, 2);
    assertNotEquals(position1, null);
  }
    
  @Test
  public void tesEquals() {
    Position position1 = new Position(1, 2);
    Position position2 = new Position(1, 2);
    assertEquals(position1, position2);
  }
    
  @Test
  public void tesNotEqualsPan() {
    Position position1 = new Position(1.45, 2);
    Position position2 = new Position(1, 2);
    assertNotEquals(position1, position2);
  }
    
  @Test
  public void tesNotEqualsTilt() {
    Position position1 = new Position(1, 2);
    Position position2 = new Position(1, 2.95);
    assertNotEquals(position1, position2);
  }
    
  @Test
  public void tesHashCode() {
    Position position1 = new Position(1, 2);
    Position position2 = new Position(1, 2.95);
    assertNotEquals(position1.hashCode(), position2.hashCode());
  }


  @Test
  public void testToString() {
    Position pos = new Position(42.42, 4.2);
    Assert.assertEquals("Position{pan=42.42, tilt=4.2}", pos.toString());
  }


  @Test
  public void testEqualsEqual() {
    Position pos1 = new Position(42.42, 2.3);
    Position pos2 = new Position(42.42, 2.3);
    Assert.assertEquals(pos1, pos2);
  }

  @Test
  public void testEqualsPanNotEqual() {
    Position pos1 = new Position(2, 2.3);
    Position pos2 = new Position(42.42, 2.3);
    Assert.assertNotEquals(pos1, pos2);
  }

  @Test
  public void testEqualsTiltNotEqual() {
    Position pos1 = new Position(2, 2.3);
    Position pos2 = new Position(2, 1);
    Assert.assertNotEquals(pos1, pos2);
  }

  @Test
  public void testEqualsNotEqualNull() {
    Position pos1 = new Position(2, 2.3);
    Assert.assertNotEquals(pos1, null);
  }

  @Test
  public void testEqualsZoomPositionNotEqual() {
    Position pos1 = new Position(2, 2.3);
    Position pos2 = new ZoomPosition(2, 1, 3);
    Assert.assertNotEquals(pos1, pos2);
  }

}
