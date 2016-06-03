package com.benine.backend.preset;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Set;

/**
 * A simple preset which just contains an image and tags.
 */
public class SimplePreset extends Preset {

  /**
   * Create a simple preset
   * @param cameraId of the camera this preset belongs to.
   */
  public SimplePreset(int cameraId) {
    super(cameraId);
  }
  
  /**
   * Create a simple preset
   * @param cameraId of the camera this preset belongs to.
   * @param tags of this preset
   */
  public SimplePreset(int cameraId, Set<String> tags) {
    this(cameraId);
    super.addTags(tags);
  }

  @Override
  public JSONObject toJSON() {
    System.out.println(imagePath);
    JSONObject json = new JSONObject();
    json.put("image", imagePath + getImage());
    json.put("id", getId());
    json.put("cameraid", getCameraId());
    JSONArray tagsJSON = new JSONArray();
    for (String tag : tags) {
      tagsJSON.add(tag);
    }
    json.put("tags", tagsJSON);

    return json;
  }

  @Override
  public String createAddSqlQuery() {
    return "INSERT INTO presetsdatabase.simplepresets VALUES(" + getId() + ",'" + getImage() + "',"
        + getCameraId() + ")";
  }

  @Override
  public void excecutePreset(Camera camera) throws CameraConnectionException {
    
  }
  
  @Override
  public String createDeleteSQL() {
    return "DELETE FROM simplepresets WHERE ID = " + getId();
  }
}
