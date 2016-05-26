package com.benine.backend;

import com.benine.backend.database.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by dorian on 18-5-16.
 */
public class PresetController {
  
  private static int newID = 1;

  private ArrayList<Preset> presets = new ArrayList<Preset>();

  private HashSet<String> tags = new HashSet<>();


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
    Database db = ServerController.getInstance().getDatabase();
    db.deletePreset(preset.getId());
  }

  /**
   * Adds a preset.
   * @param preset the preset to add.
   * @return ID of the preset just created.
   * @throws SQLException when an error occures in the database.
   */
  public int addPreset(Preset preset) throws SQLException {
    preset.setId(newID);
    newID++;
    presets.add(preset);
    addAllTags(preset.getTags());
    ServerController serverContr = ServerController.getInstance();
    serverContr.getDatabase().addPreset(preset);
    return preset.getId();
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
  }

  /**
   * Returns a collection with all tags.
   * @return a collection with all tags
   */
  public Collection<String> getTags() {
    return tags;
  }

  /**
   * Adds a collection of tags.
   * @param tags a collection of tags to add.
   */
  public void addAllTags(Collection<String> tags) {
    this.tags.addAll(tags);
  }
}
