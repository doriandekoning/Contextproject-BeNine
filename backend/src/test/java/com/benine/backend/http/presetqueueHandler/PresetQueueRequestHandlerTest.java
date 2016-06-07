package com.benine.backend.http.presetqueueHandler;

import org.junit.Assert;
import org.junit.Test;

import com.benine.backend.http.RequestHandlerTest;
import com.benine.backend.http.presetqueue.PresetQueueRequestHandler;

/**
 * Abstract test class for the PresetQueueHandlers.
 */
public abstract class PresetQueueRequestHandlerTest extends RequestHandlerTest {

  @Override
  public abstract PresetQueueRequestHandler supplyHandler();

  @Test
  public void testGetPresetQueueID() {
    setPath("/42/test");
    int camid = ((PresetQueueRequestHandler) getHandler()).getPresetsQueueId(requestMock);

    Assert.assertEquals(42, camid, 0);
  }

  @Test
  public void testGetCameraIDError() {
    setPath("/camera/fortytwo/test");
    int camid = ((PresetQueueRequestHandler) getHandler()).getPresetsQueueId(requestMock);

    Assert.assertEquals(-1, camid, 0);
  }

  @Test
  public void testGetRoute() {
    setPath("/42/test");
    String route = ((PresetQueueRequestHandler) getHandler()).getRoute(requestMock);

    Assert.assertEquals("test", route);
  }


}
