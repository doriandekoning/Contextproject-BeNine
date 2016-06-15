// Local variable to store the available tags locally.
var localTags = [];
// true if the client is in preset editing mode.
var editing = false;
var searchTerm;

/**
 * Initialize editing toggle event.
 */
$(function() {
	$('#editPresets').change(loadEditablePresets);
});

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
		tagSearchInput(searchTerm);
		for (var t in obj.tags) {
			if (localTags.indexOf(obj.tags[t]) === -1) {
				newTag(obj.tags[t]);
			}
		}
	});
}

/**
* Checks if the preset already exists if true the preset is updated otherwise it is added.
* @param object array with the presets to add.
*/
function checkPreset(object) {
	var exists = findPresetOnID(object.id);
	if (exists === undefined) {
		presets.push(new Preset(object));
	} else {
		exists.update(object);
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

	if (editButton.prop('checked') === true) {
		activepresets.addClass("preset-overlay");
		editing = true;
	} else {
		activepresets.removeClass("preset-overlay");
		editing = false;
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
	), 
	maxChars: 10,
	maxTags: 35
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
	var presetName = preset_create_div.find('#create_preset_name').val();
	var presetTag = $('#preset_create_div .tags_input').val();
	if (currentcamera !== undefined) {
		$.get("/api/backend/presets/createpreset?camera=" + currentcamera + "&tags=" + presetTag + "&name=" + presetName, function(data) {console.log("create preset respone: " + data);})
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
	), 
	maxChars: 10,
	maxTags: 35
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
	var tags = edit_tags_input.val();
	var presetName = preset_edit_div.find('#edit_preset_name').val();
	editingpreset.name = presetName;
	editingpreset.tags = tags;
	if (tags === []) {
		$.get("/api/backend/presets/edit?presetid=" + editingpreset.id + "&name=" + presetName + "&overwritetag=false&overwriteposition=false",
																									function(data) {console.log("edit preset: " + data)});
	} else {
		$.get("/api/backend/presets/edit?presetid=" + editingpreset.id + "&name=" + presetName + "&overwritetag=true&overwriteposition=false&tags=" + editingpreset.tags.join(","),
																									function(data) {console.log("edit preset: " + data)});
	}
	editingpreset.displayPreview();
}

/**
 * Function is called when the deletion button of the edit window is clicked.
 */
function deletePreset() {
	editingpreset.delete();

	var editingindex = presets.indexOf(editingpreset);
	if (editingindex !== -1) {
		presets.splice(editingindex, 1);
	};
	loadPresets();
}


/**
 * Load for the specified preset the edit modal.
 * @preset to load the edit window for.
 */
function loadPresetEditModal(preset) {
	editingpreset = preset;
	preset_edit_div.find('#presetID').text(preset.id);
	preset_edit_div.find('img').attr("src", "/api/backend" + preset.image);
	preset_edit_div.find('#edit_preset_name').val(preset.name);
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
	searchTerm = val;
	if (val === undefined || val === '') {
		displayPresets(presets);
	}else {
		var matchingpresets = matchingPresets(searchTerm);
		displayPresets(matchingpresets);
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
			var tags = preset.tags.filter(function(tag) { return tag.toLowerCase().indexOf(val.toLowerCase()) > -1;});
			if (tags.length > 0) {
				matchpresets.push(preset);
			} else if(preset.name != undefined) {
				if (preset.name.toLowerCase().indexOf(val.toLowerCase()) > -1) {
					matchpresets.push(preset);
				}
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
var editable = true;

/**
* Load the tags modal en fill in the tags.
*/
function loadTags() {
	$(".fill-tags").empty();
	$(".fill-tags").append(getTags());
	newId = localTags.length;
	editable = true;
}

$(".fill-tags").on('click', '.tag', function(e){
	if(editable) {
        e.preventDefault();
        var tag = $(this).html();
        $(this).replaceWith(appendEditable(tag, false));
		editable = false;
		editTags($(this).attr('id'), false);
	}
});

/**
* Edit or delete a clicked on tag.
* @id The id of the tag to edit
*/
function editTags(id, isNew) {
	$(".edit").click(function(e){
		e.preventDefault();
		var tag = $('.new').val();
		var updated = updateTag(id, tag);
		if (updated) {
			$(this).closest('.edittagrow').replaceWith(appendTag(id, tag));
		}
		editable = true;
	});
	$(".delete").click(function(e){
		e.preventDefault();
		var tag = $('.new').val();
		$(this).closest('.edittagrow').remove();
		deleteTag(id);
		editable = true;
	});
}

/**
* Function adds a new tag to the tag list.
* @param val value of the tag to add.
*/
function newTag(val) {
	if (localTags.indexOf(val) < 0 && val != "" && val != undefined) {
		localTags.push(val);
		tagnames.clearPrefetchCache();
		tagnames.initialize(true);
		$.get("/api/backend/presets/addtag?name=" + val, function(data) {
			console.log("create tag response: " + data);
		});
		return true;
	}
	return false;
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
	});
}

/**
* Function updates a tag in the tag list.
* @param index the index of the updated tag
* @val the updated version of the tag
*/
function updateTag(index, val) {
	if ((localTags.indexOf(val) < 0 && val != "" && val != undefined) || localTags.indexOf(val) == index)  {
		var remove = localTags[index];
		localTags[index] = val;
		tagnames.clearPrefetchCache();
		tagnames.initialize(true);
		updateTagInPresets(remove, val, function() {
			$.get("/api/backend/presets/removetag?name=" + remove, function(data) {
				$.get("/api/backend/presets/addtag?name=" + val, function(data) {});
			});
		});
		return true;
	}
	return false;
}

/**
* Updates a the tags in all the presets containing the old tag.
* @param old The old tag
* @param fresh The new tag
* @param done callback
*/
function updateTagInPresets(old, fresh, done) {
	for(i = 0; i < presets.length; i++) {
		var tagIndex = presets[i].tags.indexOf(old);
		if (tagIndex > -1) {
			presets[i].tags[tagIndex] = fresh;
			$.get("/api/backend/presets/edit?presetid=" + presets[i].id + "&overwritetag=true&overwriteposition=false&tags=" + presets[i].tags.join(","),
																									function(data) {console.log("edit preset: " + data); done();});
		}
	}
}

/**
* Create a new tag.
*/
function addTag() {
	if(editable) {
		var add = "tag " + localTags.length;
		$(".fill-tags").prepend(appendEditable(add, true));
		newTag(add);
		editable = false;
		editTags(newId, true);
		newId++;
	}
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
	return "<li class='tag btn btn-primary glyphicon glyphicon-tag' id=" + id + "> " + name + "</li>"
}

/**
* Create a html-style for a editable tag.
* @input What to prefill in the input box
* @add Is it a new tag or not
*/
function appendEditable(input, add) {
	var result = "";

	result += "<div class='row edittagrow'>";
	result += "<div class='col-xs-8'><input class='new form-control' value='" + input + "' maxlength=15/></div>";
	result += "<div class='col-xs-4'><div class='btn-group editname'><button class='delete btn btn-danger glyphicon glyphicon-remove-sign' type='button'></button><button class='edit btn btn-success glyphicon glyphicon-ok-sign' type='button'></button></div></div>";
	result += "</div>";

	return result
}