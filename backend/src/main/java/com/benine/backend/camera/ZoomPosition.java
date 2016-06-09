package com.benine.backend.camera;

/**
 * Contains a position and a zoom level.
 */
public class ZoomPosition extends Position {

  private int zoom;

  @Override
  public String toString() {
    return "ZoomPosition{" +
            "zoom=" + zoom
            + ",pan=" + getPan()
            + ",tilt=" + getTilt()
            +'}';
  }

  /**
   * Constructor of a position object used for zoomingcamera positions.
   *
   * @param pan  position in pan direction.
   * @param tilt position in the tilt direction.
   * @param zoom zoom position
   */
  public ZoomPosition(double pan, double tilt, int zoom) {
    super(pan, tilt);
    this.zoom = zoom;
  }

  /**
   * Constructor of a position object used for zoomingcamera positions.
   * @param position the positon
   * @param zoom the zoom level
   */
  public ZoomPosition(Position position, int zoom) {
    super(position.getPan(), position.getTilt());
    this.zoom = zoom;
  }

  public void setZoom(int zoom) {
    this.zoom = zoom;
  }

  public int getZoom() {
    return zoom;
  }

  @Override
  public boolean equals(Object o) {
    if (super.equals(o) && o instanceof ZoomPosition ) {
      ZoomPosition other = (ZoomPosition)o;
      return Math.abs((other.getZoom() -  this.getZoom())) < 5;
    }
    return false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp = super.hashCode();
    temp = Double.doubleToLongBits(zoom);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

}
