package com.benine.backend;//TODO add Javadoc comment

import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class PresetFacotryTest {

  int zoom, focus, iris, panspeed, tiltspeed, cameraId;
  Position pos;
  boolean autoiris, autofocus;
  PresetFactory factory;

  @Before
  public final void initialize() {

    factory = new PresetFactory();
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
    Preset expectedP = getDefaultPreset();

    PresetFactory presetFactory = new PresetFactory();
    Preset actualP = presetFactory.createPreset(pos, zoom, focus, iris, autofocus, panspeed, tiltspeed, autoiris, cameraId);

    Assert.assertEquals(expectedP, actualP);

  }


  @Test
  public final void testCreatePresetByParamsWithTags() {
    Preset expectedP = getDefaultPreset();
    Collection<String> tags = mock(Collection.class);
    expectedP.addTags(tags);

    Preset actualP = factory.createPreset(pos, zoom, focus, iris,
            autofocus, panspeed, tiltspeed, autoiris, cameraId, tags);

    Assert.assertEquals(expectedP, actualP);

  }

  @Test
  public final void testCreatePresetByCamera() throws Exception {
    Preset expectedP = getDefaultPreset();
    IPCamera cam = mock(IPCamera.class);
    when(cam.getPosition()).thenReturn(pos);
    when(cam.getId()).thenReturn(cameraId);
    when(cam.getZoomPosition()).thenReturn(zoom);
    when(cam.getFocusPosition()).thenReturn(focus);
    when(cam.getIrisPosition()).thenReturn(iris);
    when(cam.isAutoFocusOn()).thenReturn(autofocus);
    when(cam.isAutoIrisOn()).thenReturn(autoiris);

    Preset actualP = factory.createPreset(cam, panspeed, tiltspeed);
    Assert.assertEquals(expectedP, actualP);

  }




  public final Preset getDefaultPreset() {
    Preset preset = new Preset();
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
