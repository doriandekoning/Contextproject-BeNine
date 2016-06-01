package com.benine.backend;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public interface AutoPresetCreator {

  /**
   * Automatically creates presets for the selected camera.
   * @param cam the camera to create presets for.
   * @return A collection of the created presets.
   */
  Collection<Preset> createPresets(IPCamera cam) throws CameraConnectionException, InterruptedException, TimeoutException;

}
