package com.benine.backend.camera;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
    
    

}
