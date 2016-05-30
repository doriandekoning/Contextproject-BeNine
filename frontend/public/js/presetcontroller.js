var localTags = ["Trumpet", "Guitar", "Bass", "Violin", "Trombone", "Piano", "Banjo"];

/**
* Function loads all the presets from the backend.
* @param cameraID the presets of this camera are loaded.
*/
function loadPresets() {
	var presets = [];
	presets.push(new Preset(50, 180, 30, 20, 60, true, 40, 30, true, "/static/presets/1_0.jpg", 1, "Guitar,Piano,Trompet", 1));
	presets.push(new Preset(50, 180, 30, 20, 60, true, 40, 30, true, "/static/presets/4_0.jpg", 2, "Guitar,Piano,Trompet", 2));
	presets.push(new Preset(50, 180, 30, 20, 60, true, 40, 30, true, "/static/presets/1_0.jpg", 3, "Guitar,Piano,Trompet", 3));
	presets.push(new Preset(50, 180, 30, 20, 60, true, 40, 30, true, "/static/presets/4_0.jpg", 4, "Guitar,Piano,Trompet", 2));
	presets.push(new Preset(50, 180, 30, 20, 60, true, 40, 30, true, "/static/presets/1_0.jpg", 5, "Guitar,Piano,Trompet", 5));
	//$.get("/api/backend/presets/getpresets", function(data) {
	//	obj = JSON.parse(data);
	//	presets = obj.presets;
	//});
	return presets;
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
* Display all the presets.
* @param presets to display.
*/
function displayPresets(presets) {
	$("#preset_area").children().not('#tagsearch').empty();
	generatePresets(presets.map(function(item){
		return item.id;
	}));
	presets.forEach(function(item) {
		item.displayPreview();
	});
}

/**
* Initialize the tag list for type ahead.
*/
var tagnames = new Bloodhound({
	datumTokenizer: Bloodhound.tokenizers.whitespace,
	queryTokenizer: Bloodhound.tokenizers.whitespace,
	local: localTags
});

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
	typeaheadjs:( {
	hint: true,
	highlight: true,
	minLength: 1,
	autoselect: true
},
{
	name: 'tags',
	source: tagnames
})
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
		$.get("/api/backend/presets/createpreset?camera=" + currentcamera + "&tags=" + presetTag , function(data) {console.log(data);}).done(function() {
			$.get("/api/backend/presets/getpresets" , function(data) {loadPresetsOnTag(JSON.parse(data));});
		});
	}	
}

//// Below is for the edit preset window

/**
* What to display in the type ahead of the tags input of edit preset.
*/
$('#preset_edit_div .tags_input').tagsinput({
	typeaheadjs:( {
	hint: true,
	highlight: true,
	minLength: 1,
	autoselect: true
},
{
	name: 'tags',
	source: tagnames
})
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