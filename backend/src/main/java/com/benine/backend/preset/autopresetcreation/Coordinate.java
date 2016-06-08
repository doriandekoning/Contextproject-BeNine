package com.benine.backend.preset.autopresetcreation;//TODO add Javadoc comment

import org.json.simple.JSONObject;

/**
 * Represents a location in the current camera view as a Cartesian coordinate.
 */
public class Coordinate {

  private double x;
  private double y;

  /**
   * Constructs a new coordinate.
   * @param x relative x coordinate (between 0-100)
   * @param y relative y coordinate (between 0-100)
   */
  public Coordinate(double x, double y) {
    assert x >= 0 && x <= 100;
    assert y >= 0 && y <= 100;
    this.x = x;
    this.y = y;
  }

  /**
   * Getter for x.
   * @return relative x coordinate (between 0-100)
   */
  public double getX(){
    return x;
  }

  /**
   * Getter for y.
   * @return relative y coordinate (between 0-100)
   */
  public double getY(){
    return y;
  }

  /**
   * Setter for x coordinate.
   * @param x the relative x coordinate (between 0 - 100)
   */
  public void setX(double x) {
    assert x >= 0 && x <= 100;
    this.x = x;
  }

  /**
   * Setter for y cordinate.
   * @param y the relative y coordinate (between 0 - 100)
   */
  public void setY(double y) {
    assert y >= 0 && y <= 100;
    this.y = y;
  }


  /**
   * Checks if this equals o
   * @param o the object to check this with
   * @return true if this is equal to o else otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Coordinate that = (Coordinate) o;

    if (Double.compare(that.x, x) != 0) {
      return false;
    }
    return Double.compare(that.y, y) == 0;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(x);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  /**
   * Returns a string representation of this coordinate.
   * @return a string of this object.s
   */
  @Override
  public String toString() {
    return "("+x+","+y+")";
  }

  /**
   * Returns a json object representing this object.
   * @return JSON representation of this object.
   */
  public JSONObject toJSON() {
    JSONObject obj = new JSONObject();
    obj.put("x", x);
    obj.put("y", y);
    return obj;
  }

}
