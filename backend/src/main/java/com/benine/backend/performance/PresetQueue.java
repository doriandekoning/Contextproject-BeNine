package com.benine.backend.performance;

public class PresetQueue {
  
  private int lastValue = 0;
  /**
   * Returns the presetQueue.
   */
  private void getQueue(){
    
  }
  
  /**
   * Delete a preset at a given place.
   * @param place the place of the preset to be deleted.
   */
  private void deletePreset(int place){
    lastValue--;
  }
  
  
  /**
   * Replace a preset on a certain place.
   * @param place the place of the preset to be updated.
   */
  private void update(int place) {
    
  }
  
  /**
   * Add a preset at the end of the queue.
   */
  private void addPresetEnd() {
    lastValue++;
  }
  
  /**
   * Insert preset at given place.
   * @param place the place of the preset to be inserted.
   */
  private void put(int place){
    lastValue++;
  }

}
