package com.benine.backend.preset.autopresetcreation;//TODO add Javadoc comment

/**
 * Represents the subview of a current rectangle view.
 */
public class SubView {

  private Coordinate topLeft;
  private Coordinate bottomRight;

  public SubView(Coordinate topLeft, Coordinate bottomRight) {
    assert topLeft.getY() > bottomRight.getY();
    assert bottomRight.getX() > topLeft.getX();
    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
  }


  /**
   * Returns the height of this subview between 0-100 relative to superview size.
   */
  public double getHeight() {
    return topLeft.getY() - bottomRight.getY();
  }

  /**
   * Returns the width of this subview between 0-100 relative to superview size.
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
}
