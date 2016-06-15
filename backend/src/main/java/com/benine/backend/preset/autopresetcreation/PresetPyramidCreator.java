package com.benine.backend.preset.autopresetcreation;

import com.benine.backend.camera.CameraConnectionException;
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
   * @param presetController the presetController to add the presets to.
   */
  public PresetPyramidCreator(int rows, int columns, int levels, double overlap,
                              PresetController presetController) {
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
      Coordinate center = subView.getCenter();
      final double tilt = (curPos.getPan() - (curHorFov / 2)) + (center.getX() * curHorFov / 100);
      final double pan = (curPos.getTilt() - (curVerFov / 2)) + (center.getY() * curVerFov / 100);
      final int zoom = IPCamera.MAX_ZOOM - (int) ((subView.getWidth() / 100)
              * (IPCamera.MAX_ZOOM - IPCamera.MIN_ZOOM));

      positions.add(new ZoomPosition(tilt, pan, zoom));
    }
    return positions;
  }

  /**
   * Creates a collection of subviews with the specified amount of rows columns and levels.
   * @return A collection of subviews.
   */
  // According to findbugs the subViews var is useless but we need it to hold the views temporary
  // when using the forEach call on lastLayer
  // or else this will throw an concurrentModificationException
  public Collection<SubView> generateSubViews() {
    ArrayList<SubView> subViews = new ArrayList<>();
    // Level 1
    subViews.add(new SubView(0, 100, 100, 0));

    ArrayList<SubView> lastLayer = new ArrayList<>(subViews);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PresetPyramidCreator that = (PresetPyramidCreator) o;

    if (rows != that.rows) {
      return false;
    }
    if (columns != that.columns) {
      return false;
    }
    if (levels != that.levels) {
      return false;
    }
    return Double.compare(that.overlap, overlap) == 0;

  }

  @Override
  public int hashCode() {
    int result;
    result = rows;
    result = 31 * result + columns;
    result = 31 * result + levels;
    long temp;
    temp = Double.doubleToLongBits(overlap);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
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
        double subViewWidth = subView.getWidth() / columns;
        double subViewHeight = subView.getHeight() / rows;

        double subViewCenterX = subView.getTopLeft().getX()
                + ((0.5 + (double)column) * subViewWidth );
        double subViewCenterY = subView.getTopLeft().getY() - ((0.5 + (double)row) * subViewHeight);

        double subViewAspectRatio = (subViewHeight) / (subViewWidth);
        double cameraAspectRatio = 16/9;
        if (subViewAspectRatio < cameraAspectRatio) {
          // If subview is wider then camera view resize width
          subViewWidth = subViewHeight / cameraAspectRatio;
        } else {
          // If subview is higher then camera view then resize height
          subViewHeight = subViewWidth * cameraAspectRatio;
        }
        Coordinate topLeft = new Coordinate(subViewCenterX - (0.5 * subViewWidth),
                subViewCenterY + (0.5 * subViewHeight));
        Coordinate bottomRight = new Coordinate(subViewCenterX + (0.5 * subViewWidth),
                subViewCenterY - (0.5 * subViewHeight));
        subViews.add(new SubView(topLeft, bottomRight));
      }
    }
    return subViews;
  }


}