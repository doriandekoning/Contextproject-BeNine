package com.benine;
/**
 * Interface for communication with remote camera's.
 * @author Bryan
 */
public interface Camera {
	
	/**
	 * Move the camera to the specified position.
	 * @param pan in degrees horizontal axis.
	 * @param tilt in degrees vertical axis.
	 * @param panSpeed integer to specify the speed of the pan movement.
	 * @param tiltSpeed integer to specify the speed of the tilt movement.
	 */
	void moveTo(double pan, double tilt, int panSpeed, int tiltSpeed);
	
	/**
	 * Move the camera with the specified speed.
	 * Values between 1 and 99 where 50 is stand still.
	 * 99 is max speed in right and up direction.
	 * 1 is max speed in left and down direction.
	 * @param pan movement direction over horizontal axis.
	 * @param tilt movement direction over vertical axis.
	 */
	void move(int pan, int tilt);
	
	/**
	 * Get the absolute position of the camera at this moment.
	 * @return array with two values 0: Pan, 1: Tilt both in degrees.
	 */
	double[] getPosition();
	
	/**
	 * Get the current zoom position.
	 * @return the current zoom position.
	 */
	int getZoomPosition();
	
	/**
	 * Zoom to a specified position.
	 * @param zPos position to zoom to.
	 * @return
	 */
	int zoomTo(int zPos);
	
	/**
	 * Zoom with the specified speed.
	 * Value between 1 and 99 where 51 is stop zoom.
	 * 99 is max speed in tele direction.
	 * 1 is max speed in wide direction.
	 * @param dir zoom direction.
	 * @return
	 */
	int zoom(int dir);
	
	/**
	 * Get the focus position.
	 * @return focus position.
	 */
	int getFocusPos();
	
	/**
	 * Set the focus position
	 * @param pos position of the focus to move to.
	 */
	void setFocusPos(int pos);
	
	/**
	 * Move the focus in the specified direction.
	 * Values between 1 and 99 where 50 is stop focusing.
	 * 1 is focus nearer with max speed
	 * 99 is focus further with max speed
	 * @param speed value with which speed is focusing.
	 */
	void moveFocus(int speed);
	
	/**
	 * Turn auto focus on or off.
	 * @param on true for auto focus on.
	 */
	void setAutoFocusOn(boolean on);
	
	/**
	 * Request if the auto focus is on.
	 * @return true if auto focus is on.
	 */
	boolean getAutoFocusOn();
	
	/**
	 * Set the control of the iris to on.
	 * @param on true for auto iris on.
	 */
	void setAutoIrisOn(boolean on);
	
	/**
	 * Request if the auto iris is on.
	 * @return true if the auto iris is on.
	 */
	boolean getAutoIrisOn();
	
	/**
	 * Set the iris position.
	 * Values between 1 and 99.
	 * 1 is closed iris.
	 * 99 is open iris.
	 * @param pos to set the iris to.
	 */
	void setIrisPos(int pos);
	
	/**
	 * Get the current iris position.
	 * @return the current iris position.
	 */
	int getIrisPos();
	

}
