package com.benine.backend.performance;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * PresetQueue controller.
 */
public class PresetQueueController {
  
  private ArrayList<PresetQueue> presetQueues = new ArrayList<>();
  
  private Logger logger;
  
  private int highestIdInUse = 1;
  
  /**
   * Constructs a Preset Queue controller.
   */
  public PresetQueueController() {
    ServerController serverController = ServerController.getInstance();
    logger = serverController.getLogger();
  }
  
  /**
   * Adds a new preset queue to this controller.
   * @param presetQueue to add to this controller.
   */
  public void addPresetQueue(PresetQueue presetQueue) {
    presetQueue.setId(highestIdInUse);
    highestIdInUse++;
    presetQueues.add(presetQueue);
    logger.log("Added a new presetQueue with id: " + (highestIdInUse - 1), LogEvent.Type.INFO);
  }
  
  /**
   * removes the preset Queue from the list.
   * @param presetQueue the presetQeueu to remove.
   */
  public void removePresetQueue(PresetQueue presetQueue) {
    presetQueues.remove(presetQueue);
  }
  
  /**
   * Updates a presetQueue in the list.
   * @param presetQueue the presetqueue to update.
   */
  public void updatePresetQueue(PresetQueue presetQueue) {
    PresetQueue old = getPresetQueueById(presetQueue.getId());
    presetQueues.remove(old);
    presetQueues.add(presetQueue);
  }
  
  /**
   * Returns a JSON string of all preset queues.
   * @return JSON string of all preset queues.
   */
  public String getPresetQueueJSON() {
    JSONObject json = new JSONObject();
    JSONArray array = new JSONArray();
    for (PresetQueue presetQueue : getPresetQueues()) {
      array.add(presetQueue.toJSON());
    }
    json.put("presetqueues", array);
    return json.toJSONString();
  }
  
  /**
   * Finds the preset queue with the specified ID.
   * @param id to find.
   * @return preset queue with the specified id or null if it does not exist.
   */
  public PresetQueue getPresetQueueById(int id) {
    for (PresetQueue p : presetQueues) {
      if (p.getId() == id) {
        return p;
      }
    }
    return null;
  }

  public ArrayList<PresetQueue> getPresetQueues() {
    return presetQueues;
  }
}
