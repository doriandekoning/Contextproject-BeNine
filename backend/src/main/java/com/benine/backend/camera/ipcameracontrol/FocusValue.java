package com.benine.backend.camera.ipcameracontrol;

/**
 * Focus value.
 */
public class FocusValue {
  
  private int focus;
  private boolean autofocus;
  
  /**
   * Constructs a focus value containing auto focus and value.
   * @param focus position.
   * @param autofocus true if autofocus is on.
   */
  public FocusValue(int focus, boolean autofocus) {
    this.setFocus(focus);
    this.setAutofocus(autofocus);
  }


  public int getFocus() {
    return focus;
  }


  public void setFocus(int focus) {
    this.focus = focus;
  }


  public boolean isAutofocus() {
    return autofocus;
  }


  public void setAutofocus(boolean autofocus) {
    this.autofocus = autofocus;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (autofocus ? 1231 : 1237);
    result = prime * result + focus;
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
    FocusValue other = (FocusValue) obj;
    if (autofocus != other.autofocus) {
      return false;
    }
    if (focus != other.focus) {
      return false;
    }
    return true;
  }
}
