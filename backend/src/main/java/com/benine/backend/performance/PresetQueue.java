package com.benine.backend.performance;

import com.benine.backend.preset.Preset;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;


/**
 * Class that handles making a queue of presets.
 * Created by Team BeNine.
 */
public class PresetQueue {

  private ArrayList<Preset> queue = new ArrayList<>();
  private String name;
  private int ID = -1;
  
  /**
   * Constructor for the presetQueue with a name and ID.
   * @param name the name of the queue.
   * @param queue the queue to be used. 
   */
  public PresetQueue(String name, ArrayList<Preset> queue) {
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
   * Set the ID of the presetQueue.
   * @param ID the id to be set to the queue.
   */
  public void setID(int ID) {
    this.ID = ID;
  }
  
  /**
   * Getter for the name of the queue.
   * @return string with the name of the queue. 
   */
  public String getName() {
    return name;
  }
  
  /**
   * Set the name of the presetQueue.
   * @param name the name to be set to the queue.
   */
  public void setName(String name) {
    this.name = name;
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
   * If place > size it's added to the end of the list.
   * @param place the place of the preset to be inserted.
   * @param preset the preset to be inserted in the queue. 
   */
  public void insertPreset(int place, Preset preset) {
    try {
      queue.add(place, preset);
    } catch (IndexOutOfBoundsException e) {
      queue.add(preset);
    }
  }

  /**
  * PresetQueue toJSON. 
  * @return a JSON object of the presetqueue.
  */
  public JSONObject toJSON() {
    JSONObject json = new JSONObject();
    json.put("id", getID());
    json.put("name", getName());
    JSONArray queueJSON = new JSONArray();
    for (Preset preset: getQueue()) {
      queueJSON.add(preset.getId());
    }
    json.put("queue", queueJSON);

    return json;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ID;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((queue == null) ? 0 : queue.hashCode());
    return result;
  }

  /**
   * Equals method for the presetQueue.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    PresetQueue other = (PresetQueue) obj;
    if (ID != other.ID) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (queue == null) {
      if (other.queue != null) {
        return false;
      }
    } else if (!queue.equals(other.queue)) {
      return false;
    }
    return true;
  }


}
