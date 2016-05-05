package com.benine.backend.camera;

/**
 * Class to represent a position of a camera.
 * @author Bryan
 *
 */
public class Position {
	
	double pan;
	double tilt;
	
	public Position(double pan, double tilt){
		this.pan = pan;
		this.tilt = tilt;
	}
	
	/**
	 * Set the pan position.
	 * @param pan posiiton to set.
	 */
	public void setPan(double pan) {
		this.pan = pan;
	}
	
	/**
	 * Get the pan position.
	 * @return The Pan position.
	 */
	public double getPan() {
		return pan;
	}
	
	/**
	 * Set the tilt position.
	 * @param tilt position.
	 */
	public void setTilt(double tilt) {
		this.tilt = tilt;
	}
	
	/**
	 * Get the tilt position.
	 * @return tilt position.
	 */
	public double getTilt() {
		return tilt;
	}

}
