package com.benine.backend.http;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created on 22-05-16.
 */
public abstract class CameraRequestHandlerTest extends RequestHandlerTest {

  @Override
  public abstract CameraRequestHandler supplyHandler();

  @Test
  public void testGetCameraID() {
    setPath("/42/test");
    int camid = ((CameraRequestHandler) getHandler()).getCameraId(requestMock);
    System.out.println(requestMock.getPathInfo());

    Assert.assertEquals(42, camid, 0);
  }

  @Test
  public void testGetCameraIDError() {
    setPath("/camera/fortytwo/test");
    int camid = ((CameraRequestHandler) getHandler()).getCameraId(requestMock);

    Assert.assertEquals(-1, camid, 0);
  }

  @Test
  public void testGetRoute() {
    setPath("/42/test");
    String route = ((CameraRequestHandler) getHandler()).getRoute(requestMock);

    Assert.assertEquals("test", route);
  }

}
