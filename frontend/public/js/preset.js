/**
* Preset object to store all information the client has about a preset.
*/
function Preset(pan, tilt, zoom, focus, iris, autofocus, panspeed, tiltspeed, autoiris, image, id, tags, cameraid) {
	this.pan = pan;
	this.tilt = tilt;
	this.zoom = zoom;
	this.focus = focus;
	this.iris = iris;
	this.autofocus = autofocus;
	this.panspeed = panspeed;
	this.tiltspeed = tiltspeed;
	this.autoIris = autoiris;
	this.image = image;
	this.id = id;
	this.img = $('<img src="/api/backend' + image + '" >');
	this.tags = tags;
	this.cameraid = cameraid;
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
	* Update this preset with these new values.
	*/
	update: function(pan, tilt, zoom, focus, iris, autofocus, panspeed, tiltspeed, autoiris, tags) {
		this.pan = pan;
		this.tilt = tilt;
		this.zoom = zoom;
		this.focus = focus;
		this.iris = iris;
		this.autofocus = autofocus;
		this.panspeed = panspeed;
		this.tiltspeed = tiltspeed;
		this.autoIris = autoiris;
		this.tags = tags;
	}
};