package com.benine.backend.camera;

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

}
