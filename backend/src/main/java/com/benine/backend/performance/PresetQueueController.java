package com.benine.backend.performance;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;

import com.benine.backend.database.Database;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;


/**
 * PresetQueue controller.
 */
public class PresetQueueController {
  
  private ArrayList<PresetQueue> presetQueues = new ArrayList<>();
  
  private Logger logger;
  
  private static volatile int highestIdInUse = 1;

  private Database database;
  
  /**
   * Constructs a Preset Queue controller.
   */
  public PresetQueueController() {
    ServerController serverController = ServerController.getInstance();
    logger = serverController.getLogger();
    database = serverController.getDatabaseController().getDatabase();
  }
  
  /**
   * Adds a new preset queue to this controller.
   * @param presetQueue to add to this controller.
   */
  public void addPresetQueue(PresetQueue presetQueue) {
    presetQueue = addPresetQueueID(presetQueue);
    presetQueues.add(presetQueue);
    database.addQueue(presetQueue);
    logger.log("Added a new presetQueue with id: " + presetQueue.getID(), LogEvent.Type.INFO);
  }
  
  /**
   * Adds a list of presetQueues to this controller.
   * @param presetQueues list of presetQueues.
   */
  public void addPresetQueues(ArrayList<PresetQueue> presetQueues) {
    for (PresetQueue p : presetQueues) {
      addPresetQueue(p);
    }
  }
  
  /**
   * Adds the right id to this presetqueue.
   * @param presetQueue to add the id to.
   * @return Presetqueue with right ID.
   */
  private static PresetQueue addPresetQueueID(PresetQueue presetQueue) {
    if (presetQueue.getID() == -1) {
      presetQueue.setID(PresetQueueController.highestIdInUse);
      PresetQueueController.highestIdInUse++;
    } else {
      PresetQueueController.highestIdInUse = 
              Math.max(PresetQueueController.highestIdInUse - 1, presetQueue.getID()) + 1;
    }
    return presetQueue;
  }
  
  /**
   * removes the preset Queue from the list.
   * @param presetQueue the presetQeueu to remove.
   */
  public void removePresetQueue(PresetQueue presetQueue) {
    presetQueues.remove(presetQueue);
    database.deleteQueue(presetQueue.getID());
  }
  
  /**
   * Updates a presetQueue in the list.
   * @param presetQueue the presetqueue to update.
   */
  public void updatePresetQueue(PresetQueue presetQueue) {
    PresetQueue old = getPresetQueueById(presetQueue.getID());
    presetQueues.remove(old);
    presetQueues.add(presetQueue);
    database.deleteQueue(old.getID());
    database.addQueue(presetQueue);
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
      if (p.getID() == id) {
        return p;
      }
    }
    return null;
  }

  public ArrayList<PresetQueue> getPresetQueues() {
    return presetQueues;
  }
}
