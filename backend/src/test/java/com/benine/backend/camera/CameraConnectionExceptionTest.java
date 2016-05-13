package com.benine.backend.camera;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CameraConnectionExceptionTest {

    private CameraConnectionException exception;

    @Before
    public void setUp() {
        exception = new CameraConnectionException("Message", 1);
    }

    @Test
    public void testGetCameraId() {
        Assert.assertEquals(1, exception.getCamId());
    }
}
