package com.benine.backend;//TODO add Javadoc comment

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;

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
  public Collection<Preset> createPresets(MovingCamera cam) {
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
  public Preset createPreset(MovingCamera cam, int level, int column, int row) {
//    try {
////      cam.moveTo(new Position(-60, -30), 30, 2);
//    } catch (CameraConnectionException e) {
//      ServerController.getInstance().getLogger()
//              .log("Cannot connect to camera with id: " + cam.getId(), e);
//    }
    return null;
  }


}
