/**
* Preset object to store all information the client has about a preset.
* @param object is the json object send by the backend.
*/
function Preset(object) {
	this.pan = object.pan;
	this.tilt = object.tilt;
	this.zoom = object.zoom;
	this.focus = object.focus;
	this.iris = object.iris;
	this.autofocus = object.autofocus;
	this.panspeed = object.panspeed;
	this.tiltspeed = object.tiltspeed;
	this.autoIris = object.autoiris;
	this.image = object.image;
	this.id = object.id;
	this.img = $('<img src="/api/backend' + this.image + '" >');
	this.tags = object.tags;
	this.cameraid = object.cameraid;
}

Preset.prototype = {
	/**
	* Load the edit window for this preset.
	*/
	loadEdit: function() {
		loadPresetEditModal(this);
	},
	
	/**
	* Show this preset in the preset area.
	*/
	displayPreview: function() {
		var preset_area = $('#preset_area');
		var preset_div = preset_area.find('#preset_' + this.id);
		preset_div.attr("id", this.id);
		var preset_img = preset_div.find('img');
		preset_img.replaceWith(this.img);
		preset_div.click(presetClick);
	},
	
	/**	
	* Recall this preset.
	*/
	callPreset: function() {
		var preset_area = $('#preset_area');
		var preset_div = preset_area.find('#preset_' + this.id);
		var title = preset_div.find('h5');
		title.addClass("selected");
		$.get("/api/backend/presets/recallpreset?presetid=" + this.id  , function(data) {});
		switchCurrentView(this.cameraid);
		console.log("Recall preset id: " + this.id);
	},
	
	/**
	* Update this preset with these new values.
	* @newpreset is the information this preset should contain.
	*/
	update: function(newpreset) {
		this.pan = newpreset.pan;
		this.tilt = newpreset.tilt;
		this.zoom = newpreset.zoom;
		this.focus = newpreset.focus;
		this.iris = newpreset.iris;
		this.autofocus = newpreset.autofocus;
		this.panspeed = newpreset.panspeed;
		this.tiltspeed = newpreset.tiltspeed;
		this.autoIris = newpreset.autoiris;
		this.tags = newpreset.tags;
	}
};