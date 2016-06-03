// Local variable to store the available tags locally.
var localTags = [];
var deleteTags = [];
var updatedTags = [];
// true if the client is in preset editing mode.
var editing = false;

/**
* Function loads all the presets from the backend.
*/
function loadPresets() {
	$.get("/api/backend/presets/getpresets", function(data) {
		var obj = JSON.parse(data);
		for (var p in obj.presets) {
			var preset = obj.presets[p];
			checkPreset(preset);
		}
		displayPresets(presets);
		for (var t in obj.tags) {
			if (localTags.indexOf(obj.tags[t]) === -1) {
				newTag(obj.tags[t]);
			}
		}
	});
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
* @param presets to display.
*/
function displayPresets(presets) {
	$("#preset_area").children().not('#tagsearch').remove();
	generatePresets(presets.map(function(item){
		return item.id;
	}));
	presets.forEach(function(item) {
		item.displayPreview();
	});
	setButton($('#editPresets'), false);
	Holder.run({images: "#preset_area img"});
	if (editing) {
		loadEditablePresets();
	}
}

/**
* Function called when clicked on a preset.
*/
function presetClick() {
	var preset = findPresetOnID($(this).attr("id"));
	if ($(this).hasClass("preset-overlay")) {
		preset.loadEdit();
	} else {
		preset.callPreset();
	}
}

/**
* Activates the presets to be editable with the edit preset button is clicked.
*/
function loadEditablePresets() {
	var preset_area = $('#preset_area');
	var activepresets = preset_area.find("div:has(img:not([alt]))");
	var editButton = $('#editPresets');

	if (activepresets.hasClass("preset-overlay")) {
		activepresets.removeClass("preset-overlay");
		setButton(editButton, false);
		editing = false;
	} else {
		activepresets.addClass("preset-overlay");
		setButton(editButton, true);
			editing = true;
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


//// Below is for the create preset modal
var preset_create_div = $("#preset_create_div");
var create_tags_input = preset_create_div.find(".tags_input");

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
create_tags_input.tagsinput({
	typeaheadjs:( {
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
create_tags_input.tagsinput('input').blur(function(){
	if ($(this).val()) {
		newTag($(this).val());
		create_tags_input.tagsinput('add', $(this).val());
		$(this).val('');
	}
});

/**
* Called on enter in the tags input in the create modal.
*/
create_tags_input.tagsinput('input').keypress(function (e) {
	if (e.which == 13 && $(this).val()) {
		newTag($(this).val());
		create_tags_input.tagsinput('add', $(this).val());
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
		$.get("/api/backend/presets/createpreset?camera=" + currentcamera + "&tags=" + presetTag , function(data) {console.log("create preset respone: " + data);})
		.done(loadPresets);
	}	
}

//// Below is for the edit preset window
var preset_edit_div = $("#preset_edit_div");
var edit_tags_input = preset_edit_div.find(".tags_input");

/**
* What to display in the type ahead of the tags input of edit preset.
*/
edit_tags_input.tagsinput({
	typeaheadjs:( {	
			highlight: true,
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
edit_tags_input.tagsinput('input').blur(function(){
	if ($(this).val()) {
		newTag($(this).val());
		edit_tags_input.tagsinput('add', $(this).val());
		$(this).val('');
	}
});

/**
* Called on enter in the tags input in edit.
*/
edit_tags_input.tagsinput('input').keypress(function (e) {
	if (e.which == 13 && $(this).val()) {
		newTag($(this).val());
		edit_tags_input.tagsinput('add', $(this).val());
		$(this).val('');
	}
});

/**
 * Function is called when the save button of the edit window is clicked.
 */
function editPreset() {
	//TODO should be the new values from the edit window
	// And the new preset values should be send to the backend.
	var tags = edit_tags_input.val();
	editingpreset.update(editingpreset.pan, editingpreset.tilt, editingpreset.zoom, editingpreset.focus, editingpreset.iris, editingpreset.autofocus,
		editingpreset.panspeed, editingpreset.tiltspeed, editingpreset.autoiris, tags);
}

/**
 * Load for the specified preset the edit modal.
 * @preset to load the edit window for.
 */
function loadPresetEditModal(preset) {
	editingpreset = preset;
	preset_edit_div.find('#presetID').text(preset.id);
	preset_edit_div.find('img').attr("src", "/api/backend" + preset.image);
	edit_tags_input.tagsinput('removeAll');
	preset.tags.forEach(function(item) {
		edit_tags_input.tagsinput('add', item);
	});
	$('.preset-edit-modal').modal('show');
}

//// Below is for the search input in the preset area.
var tag_search_input = $('#tagsearch_input');
/**
* Handles input on the tag search field.
*/
function tagSearchInput(val) {
	if (val !== '') {
		var matchingpresets = matchingPresets(val);
		displayPresets(matchingpresets);
	} else {
		displayPresets(presets);
	}
}

/**
* When a tags suggestion is selected cal the tagSearchInput function.
*/
tag_search_input.on('typeahead:selected',function (e, val) {
	tagSearchInput(val);
});

/**
* Returns all presets that contain tags which contain val.
*/
function matchingPresets(val) {
	var matchpresets = [];
	for(var p in presets) {
		var preset = presets[p];
		if(preset.tags != undefined) {
			var tags = preset.tags.filter(function(tag) { return tag.indexOf(val) > -1;});
			if (tags.length > 0) {
				matchpresets.push(preset);
			}
		}
	}
	return matchpresets;
}

/**
* Display type ahead in the search input.
*/
tag_search_input.typeahead({
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


//Below everything for the tag modal.
var newId = 0;

/**
* Load the tags modal en fill in the tags.
*/
function loadTags() {
	updatedTags = [];
	deleteTags = [];
	$(".fill-tags").empty();
	$(".fill-tags").append(getTags());
	newId = localTags.length;
}

$(".fill-tags").on('click', '.tag', function(e){
        e.preventDefault();
        var tag = $(this).html();
        $(this).replaceWith(appendEditable(tag, false));
		editTags($(this).attr('id'));
	});

/**
* Edit or delete a clicked on tag.
* @id The id of the tag to edit
*/
function editTags(id) {
	$(".edit").click(function(e){
		e.preventDefault();
		var tag = $('.new').val();
		$(this).parent().replaceWith(appendTag(id, tag));
		updatedTags.push({index: id, name: tag});
	});
	$(".delete").click(function(e){
		e.preventDefault();
		var tag = $('.new').val();
		$(this).parent().remove();
		if(id < newId){
			deleteTags.push(id);
		}
	});
}

/**
* Save the new tags in de array.
*/
function updateTags() {
	for(i = 0; i < updatedTags.length; i++) {
		if(localTags[updatedTags[i].index] != undefined){
			//update tags
			deleteTag(updatedTags[i].index);
		}
		//update/add tags
		newTag(updatedTags[i].name);
	}
	//delete tags
	for(i = 0; i < deleteTags.length; i++) {
		deleteTag(deleteTags[i]);
	}
}


/**
* Function adds a new tag to the tag list.
* @param val value of the tag to add.
*/
function newTag(val) {
	localTags.push(val);
	tagnames.clearPrefetchCache();
 	tagnames.initialize(true);
	$.get("/api/backend/presets/addtag?name=" + val, function(data) {
				console.log("create tag respone: " + data);
	}).done();
}

/**
* Function deletes a new tag from the tag list.
* @param val value of the tag to delete.
*/
function deleteTag(index) {
	var remove = localTags[index];
	localTags.splice(index,1);
	tagnames.clearPrefetchCache();
 	tagnames.initialize(true);
	$.get("/api/backend/presets/removetag?name=" + remove, function(data) {
				console.log("create tag respone: " + data);
	}).done();
}

/**
* Create a new tag.
*/
function addTag() {
	$(".fill-tags").append(appendEditable("new", true));
	editTags(newId);
	newId++;
}

/**
* Get the tags in the html-style.
*/
function getTags() {
	var result = "";
	for(i = 0; i < localTags.length; i++) {
		result += appendTag(i, localTags[i]);
	}
	return result;
}

/**
* Create a html-style for a new tag.
* @id the id of the tag
* @name the name of the tag
*/
function appendTag(id, name) {
	return "<div><button class='tag btn btn-info glyphicon glyphicon-tag' id=" + id + "> " + name + "</button><br></div>"
}

/**
* Create a html-style for a editable tag.
* @input What to prefill in the input box
* @add Is it a new tag or not
*/
function appendEditable(input, add) {
	var result = "";
	if(add){
		result += "<div>"
	}
	result += "<input class='new' value='" + input + "' /><button class='delete btn btn-danger btn-xs glyphicon glyphicon-remove-sign' type='button'></button><button class='edit btn btn-success btn-xs glyphicon glyphicon-ok-sign' type='button'></button>"
	if(add) {
		result += "</div>"
	}
	return result
}