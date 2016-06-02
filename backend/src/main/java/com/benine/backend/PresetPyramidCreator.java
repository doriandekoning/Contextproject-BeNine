package com.benine.backend;//TODO add Javadoc comment

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Creates presets according to a pyramid model.
 */
public class PresetPyramidCreator extends AutoPresetCreator {

  private static final int HORIZONTAL_FOV = 120;
  private static final int VERTICAL_FOV = 90;
  private static double overlap;

  private int rows;
  private int columns;
  private int levels;


  /**
   * 
   * @param rows          Amount of rows in the pyramid
   * @param columns       Amount of columns in the pyramid
   * @param levels        Amount of levels in the pyramid
   * @param overlap       How much overlap there is between different presets.
   */
  public PresetPyramidCreator(int rows, int columns, int levels, double overlap) {
    assert rows > 0;
    assert columns > 0;
    this.rows = rows;
    this.columns = columns;
    this.levels = levels;
    this.overlap = overlap;
  }

  @Override
  protected Collection<ZoomPosition> generatePositions(IPCamera cam)
          throws CameraConnectionException {
    ArrayList<ZoomPosition> positions = new ArrayList<>();
    for (int level = 0; level < levels; level++ ) {
      int zoomlevel = (IPCamera.MAX_ZOOM - cam.getZoomPosition()) * (level / levels);
      Collection<Position> positionsInLayer =  generatePositionsLayer(
          new ZoomPosition(cam.getPosition(), zoomlevel));
      positionsInLayer.forEach( p -> positions.add(new ZoomPosition(p, zoomlevel)));
    }
    return positions;
  }

  /**
   * 
   * @param curPos      The current zoomposition of the camera.
   * @return            The new position.
   */
  private Collection<Position> generatePositionsLayer(ZoomPosition curPos) {
    ArrayList<Position> positions = new ArrayList<>();

    // 1 is completely zoomed out, 0 completely zoomed in
    final double zoomCoefficient =  1 - ((curPos.getZoom() - IPCamera.MIN_ZOOM)
        / (IPCamera.MAX_ZOOM));
    final double curHorFov = (IPCamera.HORIZONTAL_FOV_MAX - IPCamera.HORIZONTAL_FOV_MIN) 
        * zoomCoefficient;
    final double curVerFov = (IPCamera.VERTICAL_FOV_MAX - IPCamera.VERTICAL_FOV_MIN) 
        * zoomCoefficient;
    double betweenVer = (curVerFov / columns) - (overlap * curVerFov);
    double betweenHor = (curHorFov / columns) - (overlap * curHorFov);
    // Calculate start positions
    // TODO Change to current cam locations as starting point
    double startPan = curPos.getPan() - ((columns - 1) * (betweenHor / 2));
    double startTilt = curPos.getTilt() - ((rows - 1) * (betweenVer / 2));

    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        positions.add(new Position(startPan + (betweenHor * column), startTilt 
            + (betweenVer * row)));
      }
    }


    return positions;
  }


}
