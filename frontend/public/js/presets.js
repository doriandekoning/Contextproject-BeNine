var localTags = [{name: "test"}];

/**
* Function loads the presets of this camera in the preset window.
* @param presets object
*/
function loadPresetsOnTag(obj) {
	var preset_div, presets, place, preset, preset_area;
	preset_area = $('#preset_area');
	preset_area.find('div').removeAttr("presetID");
	preset_area.find('img').removeAttr("src");
	preset_area.find('h5').removeClass();
	Holder.run({images:"#preset_area img"});
	presets = obj.presets;
	place = 1;
	for (var p in presets) {
		if ($('#preset_'+ place) !== undefined) {
			preset = JSON.parse(presets[p]);
			preset_div = $('#preset_' + place);
			preset_div.find('img').attr("src", "/api/backend" + preset.image);
			preset_div.attr("presetID", preset.id);
			place++;
		}
	}
}

/**
* Load everyting to create a preset.
*/
function loadCreatePreset() {
	if (currentcamera !== undefined) {
		$('.preset-create-modal').find('img').attr("src", cameras[currentcamera].streamlink);
		tagnames.clearPrefetchCache();
		tagnames.initialize(true);
	}
}

/**
* Create a preset of the current camera view.
*/
function createPreset() {
	var preset_create_div = $('#preset_create_div');
	var presetName = preset_create_div.find('#preset_name').val();
	var presetTag = preset_create_div.find('#preset_tag').val();
	console.log(presetTag + " " + presetName);
	if (currentcamera !== undefined) {
		$.get("/api/backend/presets/createpreset?camera=" + currentcamera , function(data) {console.log(data);});
		loadPresets(currentcamera);
	}
}

/**
* Initialize the tag list for type ahead.
*/
var tagnames = new Bloodhound({
	datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
	queryTokenizer: Bloodhound.tokenizers.whitespace,
	prefetch: {
		url: 'public/tagnames.json',
		transform: function(list) {
			return $.map(list, function(tagname) {
				return { name: tagname }; 
			});
		}
	},
	local: localTags
});

tagnames.clearPrefetchCache();
tagnames.initialize();

/**
* What to display in the type ahead of the tags input
*/
$('#preset_create_div .tags_input').tagsinput({
	itemValue: function(item) {
		if (item['name'] !== undefined) {
			return item['name']; 
		} else {
			return item;
		}
	},
	typeaheadjs: {
		displayKey: 'name',
		valueKey: 'name',
		source: tagnames.ttAdapter()
	}
});

/**
* When the tags input changes.
*/
$('#preset_create_div .tags_input').change(function(){
	console.log($(this).val());
});

/**
* Function adds a new tag to the tag list.
*/
function newTag(val) {
	localTags.push({name: val});
	$('#preset_create_div .tags_input').tagsinput('add', {name: val});
	tagnames.clearPrefetchCache();
	tagnames.initialize(true);
}

/**
* Called when the tags input losses focus.
*/
$('#preset_create_div .tags_input').tagsinput('input').blur(function(){
	if ($(this).val()) {
		newTag($(this).val());
		$(this).val('');
	}
});

/**
* Called on enter in the tags input.
*/
$('#preset_create_div .tags_input').tagsinput('input').keypress(function (e) {
	if (e.which == 13 && $(this).val()) {
		newTag($(this).val());
		$(this).val('');
	}
});

/**
* Handles input on the tag search field.
*/
function tagSearchInput(t) {
	if(currentcamera !== undefined) {
		if (!t.val()) {
			$.get("/api/backend/presets/getpresets" , function(data) {loadPresetsOnTag(JSON.parse(data));});
		} else {
			$.get("/api/backend/presets?tag=" + t.val() , function(data) {loadPresetsOnTag(JSON.parse(data));});
		}
	}
}

/**
* Function loads the presets of this camera in the preset window.
* @param cameraID the presets of this camera are loaded.
*/
function loadPresets(cameraID) {
	var preset_div, obj, presets, place, preset, preset_area;
	preset_area = $('#preset_area');
	preset_area.find('div').removeAttr("presetID");
	preset_area.find('img').removeAttr("src");
	preset_area.find('h5').removeClass();
	Holder.run({images:"#preset_area img"});
	$.get("/api/backend/presets/getpresets", function(data) {
		obj = JSON.parse(data);
		console.log(obj);
		presets = obj.presets;
		place = 1;
		for (var p in presets) {
			if ($('#preset_'+ place) !== undefined) {
				preset = JSON.parse(presets[p]);
				preset_div = $('#preset_' + place);
				preset_div.find('img').attr("src", "/api/backend" + preset.image);
				preset_div.attr("presetID", preset.id);
				place++;
			}
		}
	});
}

/**
* Display type ahead in the search input.
*/
$('#tagsearch input').typeahead({
	hint: true,
	highlight: true,
	minLength: 1,
	autoselect: true
},
{
	name: 'tags',
	source: tagnames,
	displayKey: 'name',
	valueKey: 'name'
});

/**
* When a suggestion is selected
*/
$('#tagsearch_input').on('typeahead:selected', function(e, datum) {
	$.get("/api/backend/presets?tag=" + datum.name , function(data) {loadPresetsOnTag(JSON.parse(data));});
});

/**
* Function to handle a click on a preset.
* @param t is the div on which is clicked.
*/
function presetcall(t) {
	var presetID = t.attr("presetid");
	if (presetID !== undefined) {
		var title = t.find('h5');
		if(selectedPreset != undefined){
			$('#' + selectedPreset).find('h5').removeClass("selected");
		}
		selectedPreset = t.attr("id");
		title.addClass("selected");
		$.get("/api/backend/presets/recallpreset?presetid=" + t.attr("presetid") + "&currentcamera=" + currentcamera  , function(data) {});
		console.log(t.attr("presetid"));
		getCameraInfo();
	}
}