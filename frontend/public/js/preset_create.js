var localTags = [{name: "test"}];

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