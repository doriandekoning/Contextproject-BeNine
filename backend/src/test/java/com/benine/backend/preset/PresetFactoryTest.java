package com.benine.backend;//TODO add Javadoc comment

import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.IPCameraPresetFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class PresetFactoryTest {

  int zoom, focus, iris, panspeed, tiltspeed, cameraId;
  Position pos;
  boolean autoiris, autofocus;
  IPCameraPresetFactory factory;

  @Before
  public final void initialize() {

    factory = new IPCameraPresetFactory();
    zoom = 32;
    focus = 3;
    iris = 13;
    panspeed = 3;
    tiltspeed = 1;
    cameraId = 324;
    pos = new Position(4.2, 42.0);
    autoiris = false;
    autoiris = true;
  }



  @Test
  public final void testCreatePresetByParams() {
    IPCameraPreset expectedP = getDefaultPreset();

    IPCameraPresetFactory presetFactory = new IPCameraPresetFactory();
    IPCameraPreset actualP = presetFactory.createPreset(new ZoomPosition(pos,zoom), focus, iris, autofocus, panspeed, tiltspeed, autoiris, cameraId);

    Assert.assertEquals(expectedP, actualP);

  }


  @Test
  public final void testCreatePresetByParamsWithTags() {
    IPCameraPreset expectedP = getDefaultPreset();
    Collection<String> tags = new ArrayList<String>();
    tags.add("Tag1");
    expectedP.addTags(tags);

    IPCameraPreset actualP = factory.createPreset(new ZoomPosition(pos, zoom), focus, iris,
            autofocus, panspeed, tiltspeed, autoiris, cameraId, tags);

    Assert.assertEquals(expectedP, actualP);

  }

  @Test
  public final void testCreatePresetByCamera() throws Exception {
    IPCameraPreset expectedP = getDefaultPreset();
    IPCamera cam = mock(IPCamera.class);
    when(cam.getPosition()).thenReturn(pos);
    when(cam.getId()).thenReturn(cameraId);
    when(cam.getZoom()).thenReturn(zoom);
    when(cam.getFocusPosition()).thenReturn(focus);
    when(cam.getIrisPosition()).thenReturn(iris);
    when(cam.isAutoFocusOn()).thenReturn(autofocus);
    when(cam.isAutoIrisOn()).thenReturn(autoiris);

    IPCameraPreset actualP = factory.createPreset(cam, panspeed, tiltspeed);
    Assert.assertEquals(expectedP, actualP);

  }




  public final IPCameraPreset getDefaultPreset() {
    IPCameraPreset preset = new IPCameraPreset(cameraId);
    preset.setZoom(zoom);
    preset.setFocus(focus);
    preset.setIris(iris);
    preset.setPanspeed(panspeed);
    preset.setTiltspeed(tiltspeed);
    preset.setCameraId(cameraId);
    preset.setPosition(pos);
    preset.setAutofocus(autofocus);
    preset.setAutoiris(autoiris);
    return preset;
  }
}
