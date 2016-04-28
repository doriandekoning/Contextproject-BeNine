package com.benine;

public class Preset {
	
	private int pan;
	private int tilt;
	private int zoom;
	private int focus;
	private int iris;
	private boolean autofocus;
	
	public Preset(int pan, int tilt, int zoom, int focus, int iris, boolean autofocus){
		this.pan = pan;
		this.tilt = tilt;
		this.zoom = zoom;
		this.focus = focus;
		this.iris = iris;
		this.autofocus = autofocus;
	}

}
