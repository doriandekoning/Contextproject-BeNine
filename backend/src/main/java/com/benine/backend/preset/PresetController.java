package com.benine.backend.preset;

import com.benine.backend.LogEvent;
import com.benine.backend.ServerController;
import com.benine.backend.database.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created on 18-5-16.
 */
public class PresetController {
  
  private static volatile int newID = 1;

  private ArrayList<Preset> presets = new ArrayList<Preset>();

  private HashSet<String> tags = new HashSet<>();
  
  private Database database;
  
  /**
   * Constructor of the presetController.
   * @param serverController to interact with the rest of the system.
   */
  public PresetController(ServerController serverController) {
    database = serverController.getDatabaseController().getDatabase();
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
    database.deletePreset(preset);
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
    presets.forEach(p -> database.deleteTagFromPreset(tag, p));
    database.deleteTag(tag);
  }
}
