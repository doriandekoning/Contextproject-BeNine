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
	loadEdit: function() {
		loadPresetEditModal(this);
	},
	displayPreview: function() {
		var preset_div = $('#preset_area #preset_' + this.id);
		preset_div.attr("id", this.id);
		var preset_img = preset_div.find('img');
		preset_img.replaceWith(this.img);
		preset_div.click(presetClick);
	}, 
	callPreset: function() {
		var title = $('#preset_area #preset_' + this.id).find('h5');
		title.addClass("selected");
		$.get("/api/backend/presets/recallpreset?presetid=" + this.id + "&currentcamera=" + currentcamera  , function(data) {});
		switchCurrentView(this.cameraid);
		console.log(this.id);
	},
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
}

function presetClick() {
	var preset = findPresetOnID($(this).attr("id"));
	if ($(this).hasClass("preset-overlay")) {
		preset.loadEdit();
	} else {
		preset.callPreset();
	}
}