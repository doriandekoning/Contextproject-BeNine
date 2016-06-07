package com.benine.backend.performance;

import com.benine.backend.preset.Preset;

import java.util.LinkedHashMap;



public class PresetQueue {
  
  private int lastValue = 0;
  private LinkedHashMap<Integer,Preset> queue = new LinkedHashMap<Integer,Preset>();
  
  /**
   * Returns the presetQueue.
   * @return queue the queue that is used.
   */
  private LinkedHashMap getQueue() {
    return this.queue;
  }
  
  /**
   * Delete a preset at a given place.
   * @param place the place of the preset to be deleted.
   */
  private void deletePreset(int place) {
    queue.remove(place);
    lastValue--;
  }
  
  
  /**
   * Replace a preset on a certain place.
   * @param place the place of the preset to be updated.
   * @param preset the preset that will replace the old preset on a certain place.
   */
  private void update(int place, Preset preset) {
    //put replaces the old value if there is already a mapping for this key.
    queue.put(place, preset); 
  }
  
  /**
   * Add a preset at the end of the queue.
   * @param preset the preset to be added at the end.
   */
  private void addPresetEnd(Preset preset) {
    queue.put(lastValue + 1, preset);
    lastValue++;
  }
  
  /**
   * Insert preset at given place.
   * @param place the place of the preset to be inserted.
   */
  private void put(int place) {
    lastValue++;
  }

  
  /**
   * Getter for the last key.
   * @return the last value in the hashmap.
   */
  private int getLastValue() {
    return lastValue;
  }

  public int getId() {
    return 0;
  }

  public Object toJSON() {
    // TODO Auto-generated method stub
    return null;
  }
}
