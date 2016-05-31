var localTags = [];

/**
* Function loads all the presets from the backend.
*/
function loadPresets() {
	$.get("/api/backend/presets/getpresets", function(data) {
		obj = JSON.parse(data);
		for (var p in obj.presets) {
			var preset = obj.presets[p];
			checkPreset(preset);
		}
		for (var t in obj.tags) {
			if (localTags.indexOf(obj.tags[t]) === -1) {
				localTags.push(obj.tags[t]);
			}
		}
	}).done(displayPresets);
}

/**
* Checks if the preset already exists if true the preset is updated otherwise it is added.
* @param preset array with the presets to add.
*/
function checkPreset(preset) {
	var exists = findPresetOnID(preset.id);
	if (exists === undefined) {
		presets.push(new Preset(preset.pan, preset.tilt, preset.zoom, preset.focus, preset.iris, preset.autofocus,
			preset.panspeed, preset.tiltspeed, preset.autoiris, preset.image, preset.id, preset.tags, preset.cameraid));
	} else {
		exists.update(preset.pan, preset.tilt, preset.zoom, preset.focus, preset.iris, preset.autofocus,preset.panspeed, 
																			preset.tiltspeed, preset.autoiris, preset.tags);
	}
}

/**
* Display all the presets.
*/
function displayPresets() {
	$("#preset_area").children().not('#tagsearch').empty();
	generatePresets(presets.map(function(item){
		return item.id;
	}));
	presets.forEach(function(item) {
		item.displayPreview();
	});
	setButton($('#editPresets'), false);
	Holder.run({images: "#preset_area img"});
}

/**
* Activates the presets to be editable.
*/
function loadEditablePresets() {
	var activepresets = $("#preset_area div:has(img:not([alt]))");
	var editButton = $('#editPresets');
	if (activepresets.hasClass("preset-overlay")) {
		activepresets.removeClass("preset-overlay");
		setButton(editButton, false);
	} else {
		activepresets.addClass("preset-overlay");
		setButton(editButton, true);
	}
}	

/**
* Initialize the tag list for type ahead.
*/
var tagnames = new Bloodhound({
	datumTokenizer: Bloodhound.tokenizers.whitespace,
	queryTokenizer: Bloodhound.tokenizers.whitespace,
	local: localTags,
	limit: 10
});

/**
* Return all tags when input is empty
*/
function tagsWithDefaults(q, sync) {
	tagnames.clearPrefetchCache();
	tagnames.initialize(true);
	if (q === '') {
		sync(tagnames.local);
	}	
	else {
		tagnames.search(q, sync);
	}
}

/**
* Function adds a new tag to the tag list.
*/
function newTag(val) {
	localTags.push(val);
	tagnames.clearPrefetchCache();
 	tagnames.initialize(true);
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
		source: tagsWithDefaults, 
		limit:25
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
		source: tagnames,
		limit:25
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
		source: tagsWithDefaults,
		limit:25
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

function editPreset() {
	//TODO should be the new values from the edit window
	// And the new preset values should be send to the backend.
	var tags = $('#preset_edit_div .tags_input').val();
	editingpreset.update(editingpreset.pan, editingpreset.tilt, editingpreset.zoom, editingpreset.focus, editingpreset.iris, editingpreset.autofocus,
																		editingpreset.panspeed, editingpreset.tiltspeed, editingpreset.autoiris, tags);
}

/**
* Load for the specified preset the edit modal.
* @preset to load the edit window for.
*/
function loadPresetEditModal(preset) {
	editingpreset = preset;
	var edit_div = $('.preset-edit-modal');
	edit_div.find('#presetID').text(preset.id);
	edit_div.find('img').replaceWith(preset.img.clone());
	var tags_input = $('#preset_edit_div .tags_input');
	tags_input.tagsinput('removeAll');
	preset.tags.forEach(function(item) {
		tags_input.tagsinput('add', item);
	});
	$('.preset-edit-modal').modal('show');
}