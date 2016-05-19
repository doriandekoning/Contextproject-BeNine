package com.benine.backend;

import com.benine.backend.database.Database;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dorian on 18-5-16.
 */
public class PresetController {

  private ArrayList<Preset> presets = new ArrayList<Preset>();


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
    db.deletePreset(preset.getCameraId(), preset.getId());
  }

  /**
   * Adds a preset.
   * @param preset the preset to add.
   * @throws SQLException when an error occures in the database.
   */
  public void addPreset(Preset preset) throws SQLException {
    presets.add(preset);
    ServerController serverContr = ServerController.getInstance();
    serverContr.getDatabase().addPreset(preset.getCameraId(), preset);
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

}
