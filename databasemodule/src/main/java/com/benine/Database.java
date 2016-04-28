package com.benine;

import java.util.ArrayList;

/**
 * Interface for communication with the database.
 * @author Ege
 */
public interface Database {
	
	/**
	 * Add a preset to the database.
	 * @param camera ID of the camera
	 * @param cameraPresetNumber ID of the preset for the camera
	 * @param preset The preset to be added
	 */
	void addPreset(int camera, int cameraPresetNumber, Preset preset);
	
	/**
	 * Delete a preset from the database.
	 * @param camera ID of the camera
	 * @param cameraPresetNumber ID of the preset for the camera
	 */
	void deletePreset(int camera, int cameraPresetNumber);
	
	/**
	 * Update a preset to the database.
	 * @param camera ID of the camera
	 * @param cameraPresetNumber ID of the preset for the camera
	 * @param preset The preset to be updated
	 */
	void updatePreset(int camera, int cameraPresetNumber, Preset preset);
	
	/**
	 * Add a moving preset to the database.
	 * @param camera ID of the camera
	 * @param cameraPresetNumber ID of the preset for the camera
	 * @param beginPreset Begin of the preset
	 * @param endPreset End of the preset
	 * @param time Time from begin to end in ms
	 */
	void addMovingPreset(int camera, int cameraPresetNumber, Preset beginPreset, Preset endPreset, int time);
	
	/**
	 * Update a moving preset to the database.
	 * @param camera ID of the camera
	 * @param cameraPresetNumber ID of the preset for the camera
	 * @param beginPreset Begin of the preset
	 * @param endPreset End of the preset
	 * @param time Time from begin to end in ms
	 */
	void updateMovingPreset(int camera, int cameraPresetNumber, Preset beginPreset, Preset endPreset, int time);
	
	/**
	 * Delete a moving preset from the database.
	 * @param camera ID of the camera
	 * @param cameraMovingPresetNumber ID of the preset for the camera
	 */
	void deleteMovingPreset(int camera, int cameraMovingPresetNumber);
	
	/**
	 * Returns a preset of the camera.
	 * @param camera ID of the camera
	 * @param cameraPresetNumber ID of the preset of the camera
	 * @return
	 */
	Preset getPreset(int camera, int cameraPresetNumber);
	
	/**
	 * Returns all the presets
	 * @return
	 */
	ArrayList<Preset> getAllPresets();
	
	/**
	 * Returns all the presets of the camera
	 * @return
	 */
	ArrayList<Preset> getAllPresetsCamera();
}
