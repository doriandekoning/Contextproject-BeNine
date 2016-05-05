package com.benine.backend.database;

/**
 * A moving preset to be able to add to the database.
 * @author Ege
 *
 */
public class MovingPreset {

  Preset beginPreset;
  Preset endPreset;
  int time;

  /**
   * Constructs a moving preset.
   * @param begin The begin preset
   * @param end The end preset
   * @param time The time from begin to end in ms
   */
  public MovingPreset(Preset begin, Preset end, int time) {
    this.beginPreset = begin;
    this.endPreset = end;
    this.time = time;
  }

}
