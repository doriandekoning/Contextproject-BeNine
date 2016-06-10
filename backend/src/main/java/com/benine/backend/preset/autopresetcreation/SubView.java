package com.benine.backend.preset.autopresetcreation;

import org.json.simple.JSONObject;

/**
 * Represents the subview of a current rectangle view.
 */
public class SubView {

  private Coordinate topLeft;
  private Coordinate bottomRight;

  /**
   * Creates a new subview object.
   * @param topLeft coordinate specifying the top left corner
   * @param bottomRight coordinate specifying the bottom right corner
   */
  public SubView(Coordinate topLeft, Coordinate bottomRight) {
    assert topLeft.getY() > bottomRight.getY();
    assert bottomRight.getX() > topLeft.getX();
    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
  }

  /**
   * Creates a new subview object.
   * @param topX x coordinate of the top left corner
   * @param topY y coordinate of the top left corner
   * @param bottomX x coordinate of the bottom right corner
   * @param bottomY y coordinate of the bottom right corner
   */
  public SubView(double topX, double topY, double bottomX, double bottomY) {
    assert topY > bottomY;
    assert bottomX > topX;
    this.topLeft = new Coordinate(topX, topY);
    this.bottomRight = new Coordinate(bottomX, bottomY);
  }


  /**
   * Returns the height of this subview between 0-100 relative to superview size.
   * @return the height of this subview.
   */
  public double getHeight() {
    return topLeft.getY() - bottomRight.getY();
  }

  /**
   * Returns the width of this subview between 0-100 relative to superview size.
   * @return the width of this subview
   */
  public double getWidth() {
    return bottomRight.getX() - topLeft.getX();
  }


  public Coordinate getTopLeft() {
    return topLeft;
  }

  public Coordinate getBottomRight() {
    return bottomRight;
  }


  public Coordinate getCenter() {
    return new Coordinate(topLeft.getX() + (getWidth() / 2), topLeft.getY() - (getHeight() / 2));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SubView subView = (SubView) o;

    if (topLeft != null ? !topLeft.equals(subView.topLeft) : subView.topLeft != null) {
      return false;
    }
    return bottomRight != null
            ? bottomRight.equals(subView.bottomRight)
            : subView.bottomRight == null;

  }

  @Override
  public int hashCode() {
    int result = topLeft != null ? topLeft.hashCode() : 0;
    result = 31 * result + (bottomRight != null ? bottomRight.hashCode() : 0);
    return result;
  }

  /**
   * Creates string of this object.
   * @return String representation of this object
   */
  @Override
  public String toString() {
    return "SubView{"
            + "topLeft=" + topLeft
            + ", bottomRight=" + bottomRight
            + '}';
  }

  /**
   * Returns a JSON representation of this object.
   * @return JSON representation of this object.
   */
  public JSONObject toJSON() {
    JSONObject object = new JSONObject();
    object.put("topLeft", topLeft.toJSON());
    object.put("bottomRight", bottomRight.toJSON());
    return object;
  }
}
