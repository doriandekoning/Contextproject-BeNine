package com.benine.backend.performance;

import com.benine.backend.preset.Preset;

import java.util.ArrayList;


/**
 * Class that handles making a queue of presets.
 * Created by Team BeNine.
 */
public class PresetQueue {
  
  private ArrayList<Preset> queue = new ArrayList<Preset>();
  private String name;
  private int ID;
  
  /**
   * Constructor for the presetQueue with a name and ID.
   * @param ID the ID of the queue.
   * @param name the name of the queue.
   * @param queue the queue to be used. 
   */
  public PresetQueue(int ID, String name, ArrayList<Preset> queue) {
    this.ID = ID;
    this.name = name;
    this.queue = queue;
    
  }
  
  /**
   * Getter for the ID of the queue.
   * @return the id of the queue.
   */
  public int getID() {
    return ID;
  }
  
  /**
   * Getter for the name of the queue.
   * @return string with the name of the queue. 
   */
  public String getName() {
    return name;
  }
  
  /**
   * Returns the presetQueue.
   * @return queue the queue that is used.
   */
  public ArrayList<Preset> getQueue() {
    return this.queue;
  }
  
  /**
   * Delete a preset at a given place.
   * @param place the place of the preset to be deleted.
   */
  public void deletePreset(int place) {
    queue.remove(place);
    
  }
   
  /**
   * Replace a preset on a certain place.
   * @param place the place of the preset to be updated.
   * @param preset the preset that will replace the old preset on a certain place.
   */
  public void update(int place, Preset preset) {
    queue.set(place, preset); 
  }
  
  /**
   * Add a preset at the end of the queue.
   * @param preset the preset to be added at the end.
   */
  public void addPresetEnd(Preset preset) {
    queue.add(preset);
   
  }
  
  /**
   * Insert preset at given place.
   * @param place the place of the preset to be inserted.
   * @param preset the preset to be inserted in the queue. 
   */
  public void insertPreset(int place, Preset preset) {
    queue.add(place, preset);
  }

  
  /**
   * Getter for the size of the queue.
   * @return the last value in the ArrayList.
   */
  public int getSize() {
    return queue.size();
  }
}
