package com.benine.backend.database;

import org.json.simple.JSONObject;

/**
 * A moving preset to be able to add to the database.
 * @author Ege
 *
 */
public class Preset {
  
  private int pan;
  private int tilt;
  private int zoom;
  private int focus;
  private int iris;
  private boolean autofocus;

  /**
   * Constructs a preset.
   * @param pan The pan of the preset
   * @param tilt The tilt of the preset
   * @param zoom The zoom of the preset
   * @param focus The focus of the prest
   * @param iris The iris of the preset
   * @param autofocus The autofocus of the preset
   */
  public Preset(int pan, int tilt, int zoom, int focus, int iris, boolean autofocus) {
    this.pan = pan;
    this.tilt = tilt;
    this.zoom = zoom;
    this.focus = focus;
    this.iris = iris;
    this.autofocus = autofocus;
  }

  /**
   * Returns a JSON representation of this object.
   * @return JSON representation of this object.
   */
  public String toJSON() {
    JSONObject json = new JSONObject();

    json.put("pan", Integer.valueOf(pan));
    json.put("tilt", Integer.valueOf(tilt));
    json.put("zoom", Integer.valueOf(zoom));
    json.put("focus", Integer.valueOf(focus));
    json.put("iris", Integer.valueOf(iris));
    json.put("autofocus", Boolean.valueOf(autofocus));

    return json.toString();
  }

}
