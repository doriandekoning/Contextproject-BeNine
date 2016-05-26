function getSettings() {
    var settings = Cookies.getJSON();

    // Check if there are settings set, if not generate defaults.
    if ($.isEmptyObject(settings)) {
		setSetting("zoom", 5);
		setSetting("iris", 5);
		setSetting("focus", 5);
		setSetting("joystick", 7);
    }

    return settings;
}

function setSetting(parameter, value) {
    Cookies.set(parameter, value, { expires: 5000 });
}

function clearCookies() {
    var settings = Cookies.getJSON();
    var keys = Object.keys(settings);

    for(var i = 0; i < keys.length; i++) {
        Cookies.remove(keys[i]);
    }
}

//Below is the javascript for the settings modal
function loadSettings() {
	var settings = getSettings();
	var zoom = settings.zoom;
	var iris = settings.iris;
	var focus = settings.focus;
	var joystick = settings.joystick;
	$('#zoomsense_lbl').text(zoom);
	$('#zoomsense').val(zoom);
	$('#irissense_lbl').text(iris);
	$('#irissense').val(iris);
	$('#focussense_lbl').text(focus);
	$('#focussense').val(focus);
	$('#joysticksense_lbl').text(joystick);
	$('#joysticksense').val(joystick);
}

function changeValue(input, name){
	var newValue = input.val();
    $('#' + name + 'sense_lbl').text(newValue);
	setSetting(name, newValue);
}

$('#zoomsense').on('input', function () {
	changeValue($(this), "zoom");
});

$('#irissense').on('input', function () {
	changeValue($(this), "iris");
});

$('#focussense').on('input', function () {
	changeValue($(this), "focus");
});

$('#joysticksense').on('input', function () {
	changeValue($(this), "joystick");
});
