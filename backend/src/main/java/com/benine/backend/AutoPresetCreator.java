package com.benine.backend;

import com.benine.backend.camera.MovingCamera;

import java.util.Collection;

/**
 *
 */
public interface AutoPresetCreator {

  /**
   * Automatically creates presets for the selected camera.
   * @param cam the camera to create presets for.
   * @return A collection of the created presets.
   */
  Collection<Preset> createPresets(MovingCamera cam);

}
