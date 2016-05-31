var localTags = [];
//["Trumpet", "Guitar", "Bass", "Violin", "Trombone", "Piano", "Banjo"];

/**
* Function loads all the presets from the backend.
* @param cameraID the presets of this camera are loaded.
*/
function loadPresets() {
	$.get("/api/backend/presets/getpresets", function(data) {
		obj = JSON.parse(data);
		console.log(obj);
		for (var p in obj.presets) {
			var preset = obj.presets[p];
			updatePreset(preset);
		}
		for (var t in obj.tags) {
			if (localTags.indexOf(t) === -1) {
				localTags.push(obj.tags[t]);
			}
		}
	}).done(displayPresets);
}

function updatePreset(preset) {
	var exists = findPresetOnID(preset.id);
	if (exists === undefined) {
		presets.push(new Preset(preset.pan, preset.tilt, preset.zoom, preset.focus, preset.iris, preset.autofocus,
			preset.panspeed, preset.tiltspeed, preset.autoiris, preset.image, preset.id, preset.tags, preset.cameraid));
	} else {
		exists.pan = preset.pan;
		exists.tilt =preset.tilt;
		exists.zoom =preset.zoom;
		exists.focus = preset.focus;
		exists.iris = preset.iris;
		exists.autofocus = preset.autofocus;
		exists.panspeed = preset.panspeed;
		exists.tiltspeed = preset.tiltspeed;
		exists.autoiris = preset.autoiris;
		exists.tags = preset.tags;
	}
}

/**
* Display all the presets.
* @param presets to display.
*/
function displayPresets() {
	console.log(presets);
	$("#preset_area").children().not('#tagsearch').empty();
	generatePresets(presets.map(function(item){
		return item.id;
	}));
	presets.forEach(function(item) {
		item.displayPreview();
	});
	
	Holder.run({images: "#preset_area img"});
}

/**
* Activates the presets to be editable.
*/
function loadEditablePresets() {
	var activepresets = $("#preset_area div:has(img:not([alt]))");
	if (activepresets.hasClass("preset-overlay")) {
		activepresets.removeClass("preset-overlay");
	} else {
		activepresets.addClass("preset-overlay");
	}
}	



/**
* Initialize the tag list for type ahead.
*/
var tagnames = new Bloodhound({
	datumTokenizer: Bloodhound.tokenizers.whitespace,
	queryTokenizer: Bloodhound.tokenizers.whitespace,
	local: localTags
});

/**
* Return all tags when input is empty
*/
function tagsWithDefaults(q, sync) {
  if (q === '') {
    sync(localTags);
  }

  else {
    tagnames.search(q, sync);
  }
}

/**
* Function adds a new tag to the tag list.
*/
function newTag(val) {
	//todo should be sending the new tag to the backend.
	//$.get("/api/backend/presets/getpresets", function(data) {
	localTags.push(val);
}


//// Below is for the create preset modal

/**
* Load everyting to create a preset.
*/
function loadCreatePreset() {
	if (currentcamera !== undefined) {
		findCameraOnID(currentcamera).createView();
	}
}

/**
* What to display in the type ahead of the tags input of create preset.
*/
$('#preset_create_div .tags_input').tagsinput({
	typeaheadjs:( 
	{
		hint: true,
		highlight: true,
		minLength: 0,
		autoselect: true
	},
	{
		name: 'tags',
		source: tagsWithDefaults
	}
	)
});

/**
* Called when the tags input losses focus in the create modal.
*/
$('#preset_create_div .tags_input').tagsinput('input').blur(function(){
	if ($(this).val()) {
		newTag($(this).val());
		$('#preset_create_div .tags_input').tagsinput('add', $(this).val());
		$(this).val('');
	}
});

/**
* Called on enter in the tags input in the create modal.
*/
$('#preset_create_div .tags_input').tagsinput('input').keypress(function (e) {
	if (e.which == 13 && $(this).val()) {
		newTag($(this).val());
		$('#preset_create_div .tags_input').tagsinput('add', $(this).val());
		$(this).val('');
	}
});

/**
* Create a preset of the current camera view.
*/
function createPreset() {
	var preset_create_div = $('#preset_create_div');
	var presetName = preset_create_div.find('#preset_name').val();
	var presetTag = $('#preset_create_div .tags_input').val();
	console.log(presetTag + "--" + currentcamera);
	if (currentcamera !== undefined) {
		$.get("/api/backend/presets/createpreset?camera=" + currentcamera + "&tags=" + presetTag , function(data) {console.log(data);})
		.done(loadPresets);
	}	
}

//// Below is for the edit preset window

/**
* What to display in the type ahead of the tags input of edit preset.
*/
$('#preset_edit_div .tags_input').tagsinput({
	typeaheadjs:( 
	{	highlight: true,
		minLength: 0
	},
	{
		name: 'tags',
		source: tagsWithDefaults
	}
	)
});


/**
* Called when the tags input losses focus in edit modal.
*/
$('#preset_edit_div .tags_input').tagsinput('input').blur(function(){
	if ($(this).val()) {
		newTag($(this).val());
		$('#preset_edit_div .tags_input').tagsinput('add', $(this).val());
		$(this).val('');
	}
});

/**
* Called on enter in the tags input in edit.
*/
$('#preset_edit_div .tags_input').tagsinput('input').keypress(function (e) {
	if (e.which == 13 && $(this).val()) {
		newTag($(this).val());
		$('#preset_edit_div .tags_input').tagsinput('add', $(this).val());
		$(this).val('');
	}
});

//// Below is for the search input.

/**
* Handles input on the tag search field.
*/
function tagSearchInput(t) {
	if (!t.val()) {
		//$.get("/api/backend/presets/getpresets" , function(data) {loadPresetsOnTag(JSON.parse(data));});
	} else {
		//$.get("/api/backend/presets?tag=" + t.val() , function(data) {loadPresetsOnTag(JSON.parse(data));});
	}
}

/**
* Display type ahead in the search input.
*/
$('#tagsearch_input').typeahead({
		highlight: true,
		minLength: 0
	},
	{
		name: 'tags',
		source: tagsWithDefaults
	}
);

/**
* Returns the preset object with the specified ID
* @param id of the preset requested.
*/
function findPresetOnID(id){
	var res = $.grep(presets, function(item, n) {
		return parseInt(item.id) === parseInt(id);
	});
	return res[0];
}

/**
* Load for the specified preset the edit modal.
* @preset to load the edit window for.
*/
function loadPresetEditModal(preset) {
	$('.preset-edit-modal').find('img').replaceWith(preset.img.clone());
	preset.tags.forEach(function(item) {
		$('#preset_edit_div .tags_input').tagsinput('add', item);
	});
	$('.preset-edit-modal').modal('show');
}