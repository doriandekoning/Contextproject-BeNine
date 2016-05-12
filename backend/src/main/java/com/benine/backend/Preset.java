package com.benine.backend;

import org.json.simple.JSONObject;

/**
 * A moving preset to be able to add to the database.
 * @author Ege
 */
public class Preset {

  private int pan;
  private int tilt;
  private int zoom;
  private int focus;
  private int iris;
  private boolean autofocus;
  private int panspeed;
  private int tiltspeed;
  private boolean autoiris;

  /**
   * Constructs a preset.
   *
   * @param pan       The pan of the preset
   * @param tilt      The tilt of the preset
   * @param zoom      The zoom of the preset
   * @param focus     The focus of the prest
   * @param iris      The iris of the preset
   * @param autofocus The autofocus of the preset
   * @param autoiris  The autoiris of the preset
   * @param tiltspeed The tiltspeed of the preset
   * @param panspeed  The panspeed of the preset
   */
  public Preset(int pan, int tilt, int zoom, int focus, int iris, boolean autofocus, int panspeed, 
      int tiltspeed, boolean autoiris) {
    this.pan = pan;
    this.tilt = tilt;
    this.zoom = zoom;
    this.focus = focus;
    this.iris = iris;
    this.autofocus = autofocus;
    this.panspeed = panspeed;
    this.tiltspeed = tiltspeed;
    this.autoiris = autoiris;
  }

  /**
   * Returns a JSON representation of this object.
   *
   * @return JSON representation of this object.
   */
  public String toJSON() {
    JSONObject json = new JSONObject();

    json.put("pan", new Integer(pan));
    json.put("tilt", new Integer(tilt));
    json.put("zoom", new Integer(zoom));
    json.put("focus", new Integer(focus));
    json.put("iris", new Integer(iris));
    json.put("autofocus", new Boolean(autofocus));
    json.put("panspeed", new Integer(panspeed));
    json.put("tiltspeed", new Integer(tiltspeed));
    json.put("autoiris", new Boolean(autoiris));

    return json.toString();
  }

  public int getPan() {
    return pan;
  }

  public void setPan(int pan) {
    this.pan = pan;
  }

  public int getTilt() {
    return tilt;
  }

  public void setTilt(int tilt) {
    this.tilt = tilt;
  }

  public int getZoom() {
    return zoom;
  }

  public void setZoom(int zoom) {
    this.zoom = zoom;
  }

  public int getFocus() {
    return focus;
  }

  public void setFocus(int focus) {
    this.focus = focus;
  }

  public int getIris() {
    return iris;
  }

  public void setIris(int iris) {
    this.iris = iris;
  }

  public boolean isAutofocus() {
    return autofocus;
  }

  public void setAutofocus(boolean autofocus) {
    this.autofocus = autofocus;
  }

  public int getPanspeed() { return panspeed; }

  public void setPanspeed(int iris) {
    this.panspeed = panspeed;
  }

  public int getTiltspeed() {
    return tiltspeed;
  }

  public void setTiltSpeed(int tiltspeed) {
    this.tiltspeed = tiltspeed;
  }

  public boolean getAutoiris() {return autoiris; }

  public void setAutoiris(boolean autoiris) {
    this.autoiris = autoiris;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Preset preset = (Preset) o;

    if (pan != preset.pan) return false;
    if (tilt != preset.tilt) return false;
    if (zoom != preset.zoom) return false;
    if (focus != preset.focus) return false;
    if (iris != preset.iris) return false;
    return autofocus == preset.autofocus;

  }
}
