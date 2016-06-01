package com.benine.backend;

import com.benine.backend.camera.Position;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A moving preset to be able to add to the database.
 */
public class Preset {

  private Position position;
  private int zoom;
  private int focus;
  private int iris;
  private boolean autofocus;
  private int panspeed;
  private int tiltspeed;
  private boolean autoiris;
  private String image;
  private int presetid;
  private Set<String> tags = new HashSet<String>();
  private int cameraId;

  /**
   * Constructs a preset.
   *
   * @param pos       The position of this preset.
   * @param zoom      The zoom of the preset
   * @param focus     The focus of the prest
   * @param iris      The iris of the preset
   * @param autofocus The autofocus of the preset
   * @param autoiris  The autoiris of the preset
   * @param tiltspeed The tiltspeed of the preset
   * @param panspeed  The panspeed of the preset
   * @param cameraId  The id of the camera associated with this preset.
   */
  public Preset(Position pos, int zoom, int focus,int iris,
               boolean autofocus, int panspeed, int tiltspeed, boolean autoiris, int cameraId) {
    this.position = pos;
    this.zoom = zoom;
    this.focus = focus;
    this.iris = iris;
    this.autofocus = autofocus;
    this.panspeed = panspeed;
    this.tiltspeed = tiltspeed;
    this.autoiris = autoiris;
    this.cameraId = cameraId;
  }

  /**
   * Constructs a preset.
   *
   * @param pos       The position of this preset.
   * @param zoom      The zoom of the preset
   * @param focus     The focus of the prest
   * @param iris      The iris of the presetz
   * @param autofocus The autofocus of the preset
   * @param autoiris  The autoiris of the preset
   * @param tiltspeed The tiltspeed of the preset
   * @param panspeed  The panspeed of the preset
   * @param cameraId  The id of the camera associated with this preset.
   * @param keyWords  The keywords of this preset
   */
  public Preset(Position pos, int zoom, int focus, int iris,
                boolean autofocus, int panspeed, int tiltspeed,
                boolean autoiris, int cameraId, List<String> keyWords) {
    this(pos, zoom, focus, iris, autofocus, panspeed, tiltspeed, autoiris, cameraId);
    this.tags.addAll(keyWords);
  }

  /**
   * Returns a JSON representation of this object.
   *
   * @return JSON representation of this object.
   */
  public String toJSON() {
    JSONObject json = new JSONObject();

    json.put("pan", position.getPan());
    json.put("tilt", position.getTilt());
    json.put("zoom", zoom);
    json.put("focus", focus);
    json.put("iris", iris);
    json.put("autofocus", autofocus);
    json.put("panspeed", panspeed);
    json.put("tiltspeed", tiltspeed);
    json.put("autoiris", autoiris);
    json.put("image", image);
    json.put("id", presetid);
    JSONArray tagsJSON = new JSONArray();
    for (String tag : tags) {
      tagsJSON.add(tag);
    }
    json.put("tags", tagsJSON);

    return json.toString();
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position pos) {
    this.position = pos;
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

  public int getPanspeed() { 
    return panspeed;
  }

  public void setPanspeed(int panspeed) {
    this.panspeed = panspeed;
  }

  public int getTiltspeed() {
    return tiltspeed;
  }

  public void setTiltspeed(int tiltspeed) {
    this.tiltspeed = tiltspeed;
  }

  public boolean isAutoiris() {
    return autoiris; 
  }

  public void setAutoiris(boolean autoiris) {
    this.autoiris = autoiris;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setId(int id) {
    this.presetid = id;
  }

  public int getId() {
    return presetid;
  }

  public void setCameraId(int id) {
    this.cameraId = id;
  }

  public int getCameraId() {
    return cameraId;
  }

  public Set<String> getTags() {
    return tags;
  }


  /**
   * Adds a new tag to this object.
   * @param tag the tag to add.
   */
  public void addTag(String tag) {
    tags.add(tag);
  }


  /**
   * Adds a list of keywords to this class.
   * @param tags a list of keywords
   */
  public void addTags(List<String> tags) {
    this.tags.addAll(tags);
  }

  /**
   * Removes a keyword from this preset.
   * @param tag the keyword to remove
   */
  public void removeTag(String tag) {
    tags.remove(tag);
  }
  
  /**
   * Remove all the tags from this preset. 
   */
  public void removeTags() {
    this.tags.removeAll(getTags());
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (autofocus ? 1231 : 1237);
    result = prime * result + (autoiris ? 1231 : 1237);
    result = prime * result + focus;
    result = prime * result + iris;
    result = prime * result + panspeed;
    result = prime * result + tiltspeed;
    result = prime * result + zoom;
    result = prime * result + tags.hashCode();
    return result;
  }

  /**
   * Checking if two presets are equal.
   * @param o the object to be checked with.
   * @return true if two presets are equal, false otherwise.
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Preset preset = (Preset) o;
    
    if (presetid != preset.presetid) {
      return false;
    }

    if (!position.equals(preset.position)) {
      return false;
    }
    if (zoom != preset.zoom) {
      return false;
    }
    if (focus != preset.focus) { 
      return false;
    }
    if (iris != preset.iris) {
      return false;
    }
    if (tiltspeed != preset.tiltspeed) {
      return false;
    }
    if (panspeed != preset.panspeed) {
      return false;
    }
    if (autoiris != preset.autoiris) {
      return false;
    }
    if (autofocus != preset.autofocus) {
      return false;
    }
    if (!tags.equals(preset.getTags())) {
      return false;
    }
    return true;
  }


}
