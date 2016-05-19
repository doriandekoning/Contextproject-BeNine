package com.benine.backend;

import org.json.simple.JSONArray;

import java.sql.PreparedStatement;
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
   */
  public void removePreset(Preset preset) {
    presets.remove(preset);
  }

  /**
   * Add a preset to be controlled by this presetcontroller.
   * @param preset the preset to add.
   */
  public void addPreset(Preset preset) {
    presets.add(preset);
  }

  /**
   * Adds a list of presets to be controllerd.
   * @param presets the list of presets to add.
   */
  public void addPresets(ArrayList<Preset> presets) {
    this.presets.addAll(presets);
  }

  /**
   * Returns a list of presets that are controlled by this controller.
   * @return List of presets.
   */
  public ArrayList<Preset> getPresets() {
    return presets;
  }

}
