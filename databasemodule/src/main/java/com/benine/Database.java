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
	 * @param mPreset The preset to be added
	 */
	void addMovingPreset(int camera, int cameraPresetNumber, MovingPreset mPreset);
	
	/**
	 * Update a moving preset to the database.
	 * @param camera ID of the camera
	 * @param cameraPresetNumber ID of the preset for the camera
	 * @param mPreset The preset to be updated
	 */
	void updateMovingPreset(int camera, int cameraPresetNumber, MovingPreset mPreset);
	
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
	 * @return A preset
	 */
	Preset getPreset(int camera, int cameraPresetNumber);
	
	/**
	 * Returns all the presets.
	 * @return all the presets
	 */
	ArrayList<Preset> getAllPresets();
	
	/**
	 * Returns all the presets of the camera.
	 * @param cameraID ID of the camera
	 * @return the presets of the given camera
	 */
	ArrayList<Preset> getAllPresetsCamera(int cameraID);
	
	/**
	 * Returns a moving preset of the camera.
	 * @param camera ID of the camera
	 * @param cameraPresetNumber ID of the moving preset of the camera
	 * @return A moving preset
	 */
	MovingPreset getMovingPreset(int camera, int cameraPresetNumber);
	
	/**
	 * Returns all the moving presets.
	 * @return The moving presets
	 */
	ArrayList<MovingPreset> getAllMovingPresets();
	
	/**
	 * Returns The moving presets of the camera.
	 * @param cameraID ID of the camera
	 * @return The moving presets of the given camera
	 */
	ArrayList<MovingPreset> getAllMovingPresetsCamera(int cameraID);
	
	/**
	 * Tries to connect to database server and database.
	 * @return The database
	 */
	Database connectToDatabase();
}
