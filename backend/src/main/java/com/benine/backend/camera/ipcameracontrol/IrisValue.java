package com.benine.backend.camera.ipcameracontrol;

public class IrisValue {
  
  private int iris;
  private boolean autoiris;
  
  /**
   * Constructs a iris value containing auto iris and value.
   * @param iris position.
   * @param autoiris true if autoiris is on.
   */
  public IrisValue(int iris, boolean autoiris) {
    this.setIris(iris);
    this.setAutoiris(autoiris);
  }


  public int getIris() {
    return iris;
  }


  public void setIris(int iris) {
    this.iris = iris;
  }


  public boolean isAutoiris() {
    return autoiris;
  }


  public void setAutoiris(boolean autoiris) {
    this.autoiris = autoiris;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (autoiris ? 1231 : 1237);
    result = prime * result + iris;
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    IrisValue other = (IrisValue) obj;
    if (autoiris != other.autoiris) {
      return false;
    }
    if (iris != other.iris) {
      return false;
    }
    return true;
  }

}
