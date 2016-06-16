package com.benine.backend.preset;

import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.video.MJPEGFrameResizer;
import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.StreamReader;
import com.benine.backend.video.VideoFrame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

/**
 * an abstract preset class containing the basics for a preset.
 */
public abstract class Preset {

  String image;
  int presetid = -1;
  Set<String> tags = new HashSet<String>();
  int cameraId;
  String name = "";

  /**
   * Constructs a preset.
   *
   * @param cameraId The id of the camera associated with this preset.
   */
  public Preset(int cameraId) {
    this.cameraId = cameraId;
  }
  
  /**
   * Constructs a preset.
   * @param cameraId The id of the camera associated with this preset.
   * @param tags belonging to this preset.
   */
  public Preset(int cameraId, Set<String> tags) {
    this(cameraId);
    this.tags = tags;
    
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  /**
   * Adds a new tag to this object.
   *
   * @param tag the tag to add.
   */
  public void addTag(String tag) {
    tags.add(tag);
  }
  
  /**
   * Sets the tags of this preset.
   *
   * @param tags the tags set to set
   */
  public void setTags(Set<String> tags) {
    this.tags = tags;
  }


  /**
   * Adds a list of keywords to this class.
   *
   * @param tags a list of keywords
   */
  public void addTags(Set<String> tags) {
    tags.remove("");
    this.tags.addAll(tags);
  }

  /**
   * Removes a keyword from this preset.
   *
   * @param tag the keyword to remove
   */
  public void removeTag(String tag) {
    tags.remove(tag);
  }

  /**
   * Remove all the tags from this preset.
   */
  public void removeTags() {
    this.tags.clear();
  }

  /**
   * Returns a JSON representation of this object.
   *
   * @return JSON representation of this object.
   */
  public JSONObject toJSON() {
    JSONObject json = new JSONObject();

    json.put("id", presetid);
    json.put("cameraid", cameraId);
    json.put("image", image);
    JSONArray tagsJSON = new JSONArray();
    for (String tag : tags) {
      tagsJSON.add(tag);
    }
    json.put("tags", tagsJSON);

    return json;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + presetid;
    result = prime * result + cameraId;
    result = prime * result + tags.hashCode();
    return result;
  }

  /**
   * Checking if two presets are equal.
   *
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
    if (presetid != preset.presetid || cameraId != preset.cameraId
        || !tags.equals(preset.tags) || !name.equals(preset.name)) {
      return false;
    }
    return true;
  }

  /**
   * Creates an image for a preset.
   * @param streamReader to get the snapShot from to save as image.
   * @param folder to write the image to.
   * @param width of the image created in pixels
   * @param height of the image created in pixels
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws IOException  If the image cannot be written.
   * @throws SQLException if the image can not be saved in the database.
   */
  public void createImage(StreamReader streamReader, String folder, int width, int height) throws
          StreamNotAvailableException, IOException, SQLException {

    File path = getNewImagePath(folder);

    VideoFrame snapShot = streamReader.getSnapShot();
    MJPEGFrameResizer resizer = new MJPEGFrameResizer(width, height);
    snapShot = resizer.resize(snapShot);

    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(snapShot.getImage()));
    ImageIO.write(bufferedImage, "jpg", path);

    PresetController presetController = ServerController.getInstance().getPresetController();
    image = path.getName();
    presetController.updatePreset(this);
  }
  
  /**
   * Find a file path which not exists.
   * @param folder to check the files for.
   * @return non existing path to save the image to.
   */
  private File getNewImagePath(String folder) {
    File path;
    int imageID = 0;
    do {
      path = new File(folder + "preset_" + imageID + ".jpg");
      imageID++;
    } while (path.exists());
    
    return path;
  }

  /**
   * Recall this preset by moving the camera to the right position.
   *
   * @param cameraController used to move the right camera.
   * @throws CameraConnectionException when camera can't be moved
   * @throws CameraBusyException if the camera is busy
   */
  public abstract void excecutePreset(CameraController cameraController)
          throws CameraConnectionException, CameraBusyException;
}
