package com.benine.backend;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.video.StreamNotAvailableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;


/**
 * Created by BeNine on 25-5-16.
 */
public abstract class AutoPresetCreator {

  /**
   * Automatically creates presets for the selected camera.
   * Calll generatePositions to get the positions of the presets.
   * @param cam the camera to create presets for.
   * @return A collection of the created presets.
   * @throws CameraConnectionException exception thrown when the camera connection failed.
   * @throws InterruptedException exception thrown when camera is interrupted.
   * @throws TimeoutException exception thrown when there's a timeout.
   */
  public Collection<Preset> createPresets(IPCamera cam)
          throws CameraConnectionException, InterruptedException, TimeoutException, IOException, StreamNotAvailableException {
    Position camStartPos = cam.getPosition();
    ArrayList<Preset> presets = new ArrayList<Preset>();
    for (ZoomPosition pos : generatePositions(cam)) {
      int zoom = cam.getZoomPosition();
      cam.moveTo(pos, 30, 2);
      //TODO change to waitUntillAtPosition(zoom)
      cam.waitUntilAtPosition(pos, pos.getZoom(), 2000);
      presets.add(new PresetFactory().createPreset(cam, 2, 30));
    }
    return presets;
  }


  /**
   * 
   * @param cam the camera for which a position is generated.
   * @return zoompostion
   * @throws CameraConnectionException exception thrown when the camera connection failed.
   */
  protected abstract Collection<ZoomPosition> generatePositions(IPCamera cam) 
      throws CameraConnectionException;

}
