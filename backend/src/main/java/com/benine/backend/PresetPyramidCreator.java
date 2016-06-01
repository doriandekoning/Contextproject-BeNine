package com.benine.backend;//TODO add Javadoc comment

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

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
  public Collection<Preset> createPresets(IPCamera cam)
          throws CameraConnectionException, InterruptedException {
    Position camStartPos = cam.getPosition();
    ArrayList<Preset> presets = new ArrayList<Preset>();
    for (int level = 0; level < levels; level++ ) {
      for (int row = 0; row < rows; row++ ) {
        for (int column = 0; column < columns; column++ ) {
          presets.add(createPresetAtGridPos(cam, column, row));
        }
      }
      cam.moveTo(camStartPos, 30, 2);
      // TODO Zoom by constant value

      int newZoomPos = cam.getZoomPosition() + 500;
      cam.zoomTo(newZoomPos);
      cam.waitUntilAtPosition(camStartPos, newZoomPos, 2000);

    }
    return presets;
  }

  /**
   * Creates a preset for the specified position in the pyramid.
   * @param cam the camera to create the preset for.
   * @param column column within the level
   * @param row the row within the level
   * @return the preset at the specified position
   */
  public Preset createPresetAtGridPos(IPCamera cam, int column, int row)
          throws CameraConnectionException, InterruptedException {
    // TODO Determine position to move to
    int startZoom = cam.getZoomPosition();
    Position pos = new Position(-60, -30);
    cam.moveTo(pos, startZoom, 2);
    cam.waitUntilAtPosition(pos, startZoom, 2000);
    // TODO Check if cam is at correct location
    return new PresetFactory().createPreset(cam, 30, 2);
  }


}
