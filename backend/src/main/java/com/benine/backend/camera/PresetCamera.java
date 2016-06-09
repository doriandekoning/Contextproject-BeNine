package com.benine.backend.camera;

import com.benine.backend.preset.Preset;

import java.util.Set;

/**
 * Decorator of a camera with functions to create a preset of this camera.
 */
public interface PresetCamera extends Camera {
  
  /**
   * Creates a preset from a camera.
   * @param tagList   The tag belonging to the preset.
   * @param name      The name of the preset
   * @return          A Preset object.
   * @throws CameraConnectionException If the camera cannot be reached.
   */
  Preset createPreset(Set<String> tagList, String name) throws CameraConnectionException;

}