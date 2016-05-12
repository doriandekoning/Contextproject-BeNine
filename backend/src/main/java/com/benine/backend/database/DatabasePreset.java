package com.benine.backend.database;

import org.json.simple.JSONObject;

/**
 * A moving preset to be able to add to the database.
 * @author Ege
 */
public class DatabasePreset {

  private int pan;
  private int tilt;
  private int zoom;
  private int focus;
  private int iris;
  private boolean autofocus;
  private String image;

  /**
   * Constructs a preset.
   *
   * @param pan       The pan of the preset
   * @param tilt      The tilt of the preset
   * @param zoom      The zoom of the preset
   * @param focus     The focus of the prest
   * @param iris      The iris of the preset
   * @param autofocus The autofocus of the preset
   */
  public DatabasePreset(int pan, int tilt, int zoom, int focus, int iris, boolean autofocus) {
    this.pan = pan;
    this.tilt = tilt;
    this.zoom = zoom;
    this.focus = focus;
    this.iris = iris;
    this.autofocus = autofocus;
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
    json.put("image", String.valueOf(getImage()));

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

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

}
