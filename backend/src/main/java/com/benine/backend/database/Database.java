package com.benine.backend.database;

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
	void addPreset(int camera, int cameraPresetNumber, DatabasePreset preset);
	
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
	void updatePreset(int camera, int cameraPresetNumber, DatabasePreset preset);
	
	/**
	 * Add a moving preset to the database.
	 * @param camera ID of the camera
	 * @param cameraPresetNumber ID of the preset for the camera
	 * @param mPreset The preset to be added
	 */
	void addMovingPreset(int camera, int cameraPresetNumber, DatabaseMovingPreset mPreset);
	
	/**
	 * Update a moving preset to the database.
	 * @param camera ID of the camera
	 * @param cameraPresetNumber ID of the preset for the camera
	 * @param mPreset The preset to be updated
	 */
	void updateMovingPreset(int camera, int cameraPresetNumber, DatabaseMovingPreset mPreset);
	
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
	DatabasePreset getPreset(int camera, int cameraPresetNumber);
	
	/**
	 * Returns all the presets.
	 * @return all the presets
	 */
	ArrayList<DatabasePreset> getAllPresets();
	
	/**
	 * Returns all the presets of the camera.
	 * @param cameraID ID of the camera
	 * @return the presets of the given camera
	 */
	ArrayList<DatabasePreset> getAllPresetsCamera(int cameraID);
	
	/**
	 * Returns a moving preset of the camera.
	 * @param camera ID of the camera
	 * @param cameraPresetNumber ID of the moving preset of the camera
	 * @return A moving preset
	 */
	DatabaseMovingPreset getMovingPreset(int camera, int cameraPresetNumber);
	
	/**
	 * Returns all the moving presets.
	 * @return The moving presets
	 */
	ArrayList<DatabaseMovingPreset> getAllMovingPresets();
	
	/**
	 * Returns The moving presets of the camera.
	 * @param cameraID ID of the camera
	 * @return The moving presets of the given camera
	 */
	ArrayList<DatabaseMovingPreset> getAllMovingPresetsCamera(int cameraID);
	
	/**
	 * Tries to connect to database server.
	 * @return True if the connection is succeeded.
	 */
	boolean connectToDatabaseServer();

	/**
	 * Check if the database is present in the server.
	 * @return True if the database is present, false otherwise
     */
	boolean checkDatabase();

	/**
	 * Creates new database, overrides the old one if there is one.
     */
	void resetDatabase();

	/**
	 * Closes the connection to the server.
	 */
	void closeConnection();

}
