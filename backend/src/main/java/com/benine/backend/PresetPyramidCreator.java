package com.benine.backend;//TODO add Javadoc comment

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Creates presets according to a pyramid model.
 */
public class PresetPyramidCreator implements AutoPresetCreator {

  private static final int TIMEOUT_DURATION = 200;

  private int rows;
  private int columns;
  private int levels;


  public PresetPyramidCreator(int rows, int columns, int levels) {
    this.rows = rows;
    this.columns = columns;
    this.levels = levels;
  }

  @Override
  public Collection<Preset> createPresets(IPCamera cam) throws CameraConnectionException {
    Position camStartPos = cam.getPosition();
    ArrayList<Preset> presets = new ArrayList<Preset>();
    for (int level = 0; level < levels; level++ ) {
      for (int row = 0; row < rows; row++ ) {
        for (int column = 0; column < columns; column++ ) {
          presets.add(createPresetAtGridPos(cam, column, row));
        }
      }
      cam.moveTo(camStartPos, 30, 2);
      // TODO move to seperate method (wait until at certain position)
      while(!cam.getPosition().equals(camStartPos)) {
        try {
          Thread.sleep(200);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      // TODO Zoom by constant value
      cam.zoomTo(cam.getZoomPosition()+500);
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
  public Preset createPresetAtGridPos(IPCamera cam, int column, int row) throws CameraConnectionException {
    // TODO Determine position to move to
    cam.moveTo(new Position(-60, -30), 30, 2);
    for(int i = 0; i < 10; i++) {
      try {
        Thread.sleep(i*100);
        // TODO Check if cam is at correct location
        return new PresetFactory().createPreset(cam, 15, 2);
      } catch (InterruptedException e) {
        break;
      }
    }
    return null;
  }

  /**
   * Waits until the camera has arrived at a location or the timeout has expired.
   * @param cam The ipcamera to wait for to be at the position
   * @param pos The position the camera should be at.
   * @param zoom the zoom of the camera
   * @param timeout the timeout after which to give up waiting
   * @return true if the camera is at the specified location false otherwise
   */
  public boolean waitUntilAtPosition(IPCamera cam, Position pos, int zoom, long timeout)
          throws InterruptedException, CameraConnectionException {
    long timedOutTime = System.currentTimeMillis() + timeout;
    while(System.currentTimeMillis() < timedOutTime ) {
      if (cam.getPosition().equals(pos) && zoom == cam.getZoomPosition()) {
        return true;
      }
      Thread.sleep(TIMEOUT_DURATION);
    }
    return false;
  }
}
