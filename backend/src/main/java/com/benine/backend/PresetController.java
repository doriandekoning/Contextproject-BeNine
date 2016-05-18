package com.benine.backend;

import java.util.ArrayList;

/**
 * Created by dorian on 18-5-16.
 */
public class PresetController {

  private ArrayList<Preset> presets = new ArrayList<Preset>();

  /**
   * Add a preset to be controlled by this presetcontroller.
   * @param preset the preset to add.
   */
  public void addPreset(Preset preset) {
    presets.add(preset);
  }

  /**
   * Adds a list of presets to be controllerd.
   * @param presets the list of presets to add.
   */
  public void addPresets(ArrayList<Preset> presets) {
    this.presets.addAll(presets);
  }

  /**
   * Returns a list of presets that are controlled by this controller.
   * @return List of presets.
   */
  public ArrayList<Preset> getPresets() {
    return presets;
  }
}
