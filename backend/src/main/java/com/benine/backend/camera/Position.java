package com.benine.backend.camera;

/**
 * Class to represent a position of a camera.
 * @author Bryan
 *
 */
public class Position {

  double pan;
  double tilt;

  /**
   * Constructor of a position object used for camera positions.
   * @param pan position in pan direction.
   * @param tilt position in the tilt direction.
   */
  public Position(double pan, double tilt) {
    this.pan = pan;
    this.tilt = tilt;
  }

  /**
   * Set the pan position.
   * @param pan posiiton to set.
   */
  public void setPan(double pan) {
    this.pan = pan;
  }

  /**
   * Get the pan position.
   * @return The Pan position.
   */
  public double getPan() {
    return pan;
  }

  /**
   * Set the tilt position.
   * @param tilt position.
   */
  public void setTilt(double tilt) {
    this.tilt = tilt;
  }

  /**
   * Get the tilt position.
   * @return tilt position.
   */
  public double getTilt() {
    return tilt;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(pan);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(tilt);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Position) {
      Position that = (Position) o;
      if (Double.compare(this.pan, that.pan) == 0
                && Double.compare(this.tilt, that.tilt) == 0 ) {
        return true;
      }
    }
    return false;
  }

}
