package com.benine.backend;//TODO add Javadoc comment

import com.benine.backend.camera.Camera;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Creates presets according to a pyramid model.
 */
public class PresetPyramidCreator implements AutoPresetCreator {

  private int rows;
  private int columns;
  private int levels;


  public PresetPyramidCreator(int rows, int columns, int levels) {
    this.rows = rows;
    this.columns = columns;
    this.levels = levels;
  }

  @Override
  public Collection<Preset> createPresets(Camera cam) {
    ArrayList<Preset> presets = new ArrayList<Preset>();
    for (int level = 0; level < levels; level++ ) {
      for (int row = 0; row < rows; row++ ) {
        for (int column = 0; column < columns; column++ ) {
          presets.add(createPreset(cam, level, column, row));
        }
      }
    }
    return presets;
  }

  /**
   * Creates a preset for the specified position in the pyramid.
   * @param cam the camera to create the preset for.
   * @param level the level of the preset within the pyramid
   * @param column column within the level
   * @param row the row within the level
   * @return the preset at the specified position
   */
  public Preset createPreset(Camera cam, int level, int column, int row) {
    return null;
  }
}
