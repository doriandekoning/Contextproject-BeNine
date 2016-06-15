/**
* Preset object to store all information the client has about a preset.
* @param newPreset is the json object send by the backend.
*/
function Preset(newPreset) {
	this.pan = newPreset.pan;
	this.tilt = newPreset.tilt;
	this.zoom = newPreset.zoom;
	this.focus = newPreset.focus;
	this.iris = newPreset.iris;
	this.autofocus = newPreset.autofocus;
	this.panspeed = newPreset.panspeed;
	this.tiltspeed = newPreset.tiltspeed;
	this.autoIris = newPreset.autoiris;
	this.image = newPreset.image;
	this.id = newPreset.id;
	this.img = $('<img src="/api/backend' + this.image + '" >');
	this.tags = newPreset.tags;
	this.cameraid = newPreset.cameraid;
	this.name = newPreset.name;
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
		if (this.name != undefined && this.name !== '') {
			preset_div.find('h5').text(this.name);
		}
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
		this.recallPreset();
		switchCurrentView(this.cameraid);
	},

	/**
	 * Recall this preset.
	 */
	recallPreset: function() {
		$.get("/api/backend/presets/recallpreset?presetid=" + this.id  , function(data) {});
		console.log("Recall preset id: " + this.id);
	},

	/**
	 * Delete this preset.
	 */
	delete: function() {
		$.get("/api/backend/presets/deletepreset?id=" + this.id  , function(data) {});
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
		this.image = newpreset.image;
		this.img = $('<img src="/api/backend' + this.image + '" >');
	}
};
