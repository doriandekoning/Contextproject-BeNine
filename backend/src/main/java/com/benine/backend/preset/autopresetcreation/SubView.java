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


  public Coordinate getCenter () {
    return new Coordinate(topLeft.getX() + (getWidth()/2), topLeft.getY() - (getHeight()/2));
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
    return bottomRight != null ?
            bottomRight.equals(subView.bottomRight)
            : subView.bottomRight == null;

  }

  @Override
  public int hashCode() {
    int result = topLeft != null ? topLeft.hashCode() : 0;
    result = 31 * result + (bottomRight != null ? bottomRight.hashCode() : 0);
    return result;
  }
}
