package com.benine.backend;//TODO add Javadoc comment

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

/**
 * Creates presets according to a pyramid model.
 */
public class PresetPyramidCreator implements AutoPresetCreator {

  private static final int HORIZONTAL_FOV = 120;
  private static final int VERTICAL_FOV = 90;
  private static final double OVERLAP = 0.1;

  private int rows;
  private int columns;
  private int levels;


  public PresetPyramidCreator(int rows, int columns, int levels) {
    assert rows > 0;
    assert columns > 0;
    this.rows = rows;
    this.columns = columns;
    this.levels = levels;
  }

  @Override
  public Collection<Preset> createPresets(IPCamera cam)
          throws CameraConnectionException, InterruptedException, TimeoutException {
    Position camStartPos = cam.getPosition();
    ArrayList<Preset> presets = new ArrayList<Preset>();
    for (int level = 0; level < levels; level++ ) {
      int zoom = cam.getZoomPosition();
      for (Position pos : generatePositionsLayer(zoom)) {
        cam.moveTo(pos, 30, 2);
        cam.waitUntilAtPosition(pos, zoom, 2000);
        presets.add(new PresetFactory().createPreset(cam, 2, 30));
      }

      int newZoomPos = cam.getZoomPosition() + ((IPCamera.MAX_ZOOM - IPCamera.MIN_ZOOM)* (level/levels));
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
          throws CameraConnectionException, InterruptedException, TimeoutException {
    // TODO Determine position to move to
    int startZoom = cam.getZoomPosition();
    Position pos = new Position(-90 + (90*column), (180- 30) + (30*row));
    cam.moveTo(pos, startZoom, 2);
    cam.waitUntilAtPosition(pos, startZoom, 2000);
    // TODO Check if cam is at correct location
    return new PresetFactory().createPreset(cam, 30, 2);
  }


  private ArrayList<Position> generatePositionsLayer(int zoomlevel) {
    ArrayList<Position> positions = new ArrayList<>();

    // 1 is completely zoomed out, 0 completely zoomed in

    final double zoomCoefficient =  1 - ((zoomlevel - IPCamera.MIN_ZOOM)/(IPCamera.MAX_ZOOM));
    final double curHorFov = (IPCamera.HORIZONTAL_FOV_MAX - IPCamera.HORIZONTAL_FOV_MIN) * zoomCoefficient;
    final double curVerFov = (IPCamera.VERTICAL_FOV_MAX - IPCamera.VERTICAL_FOV_MIN) * zoomCoefficient;
    double betweenVer = (curVerFov/columns) - (OVERLAP*curVerFov);
    double betweenHor = (curHorFov/columns) - (OVERLAP*curHorFov);
    // Calculate start positions
    double startPan = 0 - (columns*(betweenHor/2));
    double startTilt = 180 - ((rows/2)*betweenVer);

    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        positions.add(new Position(startPan + (betweenVer * column), startTilt + (betweenHor * row)));
      }
    }

    return positions;
  }

}
