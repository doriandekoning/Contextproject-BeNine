package com.benine.backend.database;

/**
 * A moving preset to be able to add to the database
 * @author Ege
 *
 */
public class DatabaseMovingPreset {
	
	DatabasePreset beginPreset;
	DatabasePreset endPreset;
	int time;
	
	/**
	 * Constructs a moving preset.
	 * @param begin The begin preset
	 * @param end The end preset
	 * @param time The time from begin to end in ms
	 */
	public DatabaseMovingPreset(DatabasePreset begin, DatabasePreset end, int time){
		this.beginPreset = begin;
		this.endPreset = end;
		this.time = time;
	}

}
