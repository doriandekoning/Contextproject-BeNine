package com.benine.backend.preset;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.database.Database;
import com.benine.backend.video.StreamController;
import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.StreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;

/**
 * Created on 18-5-16.
 */
public class PresetController {
  
  private static volatile int newID = 1;

  private ArrayList<Preset> presets = new ArrayList<Preset>();

  private HashSet<String> tags = new HashSet<>();
  
  private Database database;
  
  private Config config;
  
  private StreamController streamController;
  
  private Logger logger;
  
  /**
   * Constructor of the presetController.
   */
  public PresetController() {
    ServerController serverController = ServerController.getInstance();
    database = serverController.getDatabaseController().getDatabase();
    config = serverController.getConfig();
    streamController = serverController.getStreamController();
    logger = serverController.getLogger();
  }
  
  /**
   * Returns a json string of all the presets including available tags.
   * @param tag the presets requested must contain.
   * @return json string of all the presets.
   */
  public String getPresetsJSON(String tag) {
    JSONObject jsonObject = new JSONObject();
    String imagePath = config.getValue("imagepath");
    ArrayList<Preset> resultPresets = getPresets();
    if (tag == null) {
      JSONArray tagsJSON = new JSONArray();
      Collection<String> tags = getTags();
      tags.forEach(t -> tagsJSON.add(t));
      jsonObject.put("tags", tagsJSON);
    } else {
      resultPresets = getPresetsByTag(tag);
    }

    JSONArray presetsJSON = new JSONArray();
    for (Preset p : resultPresets) {
      JSONObject presetJson = p.toJSON();
      presetJson.put("image", "/" + imagePath + presetJson.get("image"));
      presetsJSON.add(presetJson);
    }
    jsonObject.put("presets", presetsJSON);
    
    return jsonObject.toString();
  }


  /**
   * Returns the preset with the specified id.
   * @param id the id of the preset
   * @return an preset with the id.
   */
  public Preset getPresetById(int id) {
    for (Preset p : presets) {
      if (id == p.getId()) {
        return p;
      }
    }
    return null;
  }
  
  /**
   * Create an image for this preset using camera with cameraID
   * @param preset to create an image for.
   */
  public void createImage(Preset preset) {
    StreamReader streamReader;
    try {
      streamReader = streamController.getStreamReader(preset.getCameraId());
      String imagePath = config.getValue("imagepath")
                          .replaceAll("/", Matcher.quoteReplacement(File.separator));
      int width = Integer.parseInt(config.getValue("preset_image_width"));
      int height = Integer.parseInt(config.getValue("preset_image_height"));
      preset.createImage(streamReader, imagePath, width, height);
      updatePreset(preset);
    } catch (StreamNotAvailableException e) {
      logger.log("Stream is not available for creating image.", e);
    } catch (IOException e) {
      logger.log("Image could not be saved.", e);
    } catch (SQLException e) {
      logger.log("Image could not be saved in database.", e);
    }
  }
  
  /**
   * Returns an list of all the presets that are tagged with the specified tag.
   * @param tag the tag with which the presets have to be tagged.
   * @return an arraylist with all presets tagged with the tag.
   */
  public ArrayList<Preset> getPresetsByTag(String tag) {
    ArrayList<Preset> returnList = new ArrayList<>();
    presets.forEach(p -> {
      if (p.getTags().contains(tag)) {
        returnList.add(p);
      }
    });
    return returnList;
  }


  /**
   * Removes a preset from this presetcontroller.
   * @param preset the preset to remove.
   * @throws SQLException if error with database occures.
   */
  public void removePreset(Preset preset) throws SQLException {
    presets.remove(preset);
    database.deletePreset(preset.getId());
    File path = new File(config.getValue("imagepath")
        .replaceAll("/", Matcher.quoteReplacement(File.separator)) + preset.getImage());
    if (!path.delete()) {
      logger.log(preset.getImage() + " could not be deleted", LogEvent.Type.WARNING);
    }
  }
  
  /**
   * Adds the right id to this preset.
   * @param preset to add the id to.
   * @return Preset with right ID.
   */
  private static Preset addPresetID(Preset preset) {
    if (preset.getId() == -1) {
      preset.setId(PresetController.newID);
      PresetController.newID++;
    } else {
      PresetController.newID = Math.max(PresetController.newID - 1, preset.getId()) + 1;
    }
    return preset;
  }
 
  /**
   * Adds a preset.
   * @param preset the preset to add.
   * @return ID of the preset just created.
   * @throws SQLException when an error occures in the database.
   */
  public int addPreset(Preset preset) throws SQLException {   
    preset = addPresetID(preset);
    if (preset.getName().equals("")) {
      preset.setName("Preset " + preset.getId());
    }
    presets.add(preset);
    addAllTags(preset.getTags());
    database.addPreset(preset);
    return preset.getId();
  }
  
  /**
   * Updates a preset in preset list and database.
   * @param preset the preset to update.
   * @throws SQLException when an error occurs in the database.
   */
  public void updatePreset(Preset preset) throws SQLException {
    Preset old = getPresetById(preset.getId());
    presets.remove(old);
    presets.add(preset);
    addAllTags(preset.getTags());
    database.updatePreset(preset);
  }

  /**
   * Adds a list of presets to be controllerd.
   * @param presets the list of presets to add.
   * @throws SQLException when an error occures in the database.
   */
  public void addPresets(ArrayList<Preset> presets) throws SQLException {
    for (Preset preset : presets) {
      addPreset(preset);
    }
  }

  /**
   * Returns a list of presets that are controlled by this controller.
   * @return List of presets.
   */
  public ArrayList<Preset> getPresets() {
    return presets;
  }


  /**
   * Adds a tag.
   * @param tag the name of the tag to add.
   */
  public void addTag(String tag) {
    tags.add(tag);
    database.addTag(tag);
  }

  /**
   * Returns a collection with all tags.
   * @return a collection with all tags
   */
  public Collection<String> getTags() {
    this.addAllTags(database.getTags());
    return tags;
  }

  /**
   * Adds a collection of tags.
   * @param tags a collection of tags to add.
   */
  public void addAllTags(Collection<String> tags) {
    this.tags.addAll(tags);
  }

  /**
   * Removes a tag from a presetcontroller object and all its presets.
   * @param tag the tag to remove.
   */
  public void removeTag(String tag) {
    tags.remove(tag);
    presets.forEach(p -> p.removeTag(tag));
    presets.forEach(p -> database.deleteTagFromPreset(tag, p.getId()));
    database.deleteTag(tag);
  }
}
