package com.benine.backend.preset.autopresetcreation;//TODO add Javadoc comment

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.PresetController;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Creates presets according to a pyramid model.
 */
public class PresetPyramidCreator extends AutoPresetCreator {

  private int rows;
  private int columns;
  private int levels;
  private double overlap;

  /**
   * Constructs a PresetPyramidCreator.
   * @param rows the amount of rows > 0
   * @param columns the amount of columns > 0
   * @param levels the amount of levels > 0
   * @param overlap the overlap between presets (negative allowed for space between)
   */
  public PresetPyramidCreator(int rows, int columns, int levels, double overlap, PresetController presetController) {
    super(presetController);
    assert rows > 0;
    assert columns > 0;
    assert levels > 0;
    this.rows = rows;
    this.columns = columns;
    this.levels = levels;
    this.overlap = overlap;
  }

  @Override
  protected Collection<ZoomPosition> generatePositions(IPCamera cam, Collection<SubView> subViews)
          throws CameraConnectionException {
    ArrayList<ZoomPosition> positions = new ArrayList<>();

    // 1 is completely zoomed out, 0 completely zoomed in
    ZoomPosition curPos = new ZoomPosition(cam.getPosition(), cam.getZoom());

    final double zoomCoefficient =  1 - ((curPos.getZoom() - IPCamera.MIN_ZOOM)
            / (IPCamera.MAX_ZOOM));
    final double curHorFov = IPCamera.HORIZONTAL_FOV_MAX * zoomCoefficient;
    final double curVerFov = IPCamera.VERTICAL_FOV_MAX * zoomCoefficient;

    for (SubView subView : subViews) {
      System.out.println(1);
      Coordinate center = subView.getCenter();
      final double tilt = (curPos.getPan() - (curHorFov / 2)) + (center.getX() * curHorFov / 100);
      final double pan = (curPos.getTilt() - (curVerFov / 2)) + (center.getY() * curVerFov / 100);
      final int zoom = IPCamera.MAX_ZOOM - (int) ((subView.getWidth() / 100) * (IPCamera.MAX_ZOOM-IPCamera.MIN_ZOOM));
      System.out.println("Curzoom: " + curPos.getZoom() + " zoom:   " + zoom);

      positions.add(new ZoomPosition(tilt, pan, zoom));
    }
    return positions;
  }

  /**
   * Creates a collection of subviews with the specified amount of rows columns and levels.
   * @return A collection of subviews.
   */
  public Collection<SubView> generateSubViews() {
    ArrayList<SubView> subViews = new ArrayList<>();
    ArrayList<SubView> lastLayer = new ArrayList<>();
    // Level 1
    subViews.add(new SubView(0, 100, 100, 0));
    lastLayer.addAll(subViews);
    // Other levels
    for (int level = 1; level < levels; level++) {
      ArrayList<SubView> newSubViews = new ArrayList<>();
      lastLayer.forEach(sv -> newSubViews.addAll(generateSubViewLayer(sv)));
      subViews.addAll(newSubViews);
      lastLayer.clear();
      lastLayer.addAll(newSubViews);
    }
    return subViews;
  }

  /**
   * Generates subviews for a specific subview for a single layer.
   * @param subView the subview to generate subviews for.
   * @return A collection of the generated subviews for the layer.
   */
  private Collection<SubView> generateSubViewLayer(SubView subView) {
    ArrayList<SubView> subViews = new ArrayList<>();
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        double topLeftX = subView.getTopLeft().getX() + (column * (subView.getWidth() / columns));
        double topLeftY = subView.getTopLeft().getY() - (row * (subView.getHeight() / rows));
        double bottomRightX =  topLeftX + (subView.getWidth() / columns);
        double bottomRightY =  topLeftY - (subView.getHeight() / rows);
        subViews.add(new SubView(topLeftX, topLeftY, bottomRightX, bottomRightY));
      }
    }
    return subViews;
  }


}