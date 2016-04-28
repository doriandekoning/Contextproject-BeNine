package com.benine;

public class MovingPreset {
	
	Preset beginPreset;
	Preset endPreset;
	int time;
	
	public MovingPreset(Preset begin, Preset end, int time){
		this.beginPreset = begin;
		this.endPreset = end;
		this.time = time;
	}

}
