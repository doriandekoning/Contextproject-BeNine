package com.benine.backend.camera;

import com.benine.backend.camera.ipcameracontrol.ZoomPosition;
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
}
