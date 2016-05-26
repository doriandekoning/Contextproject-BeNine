function getSettings() {
    var settings = Cookies.getJSON();

    // Check if there are settings set, if not generate defaults.
    if ($.isEmptyObject(settings)) {
		setSetting("zoom", 1);
		setSetting("iris", 5);
		setSetting("focus", 3);
		setSetting("joystick", 5);
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

$('#zoomsense').on('input', function () {
    $('#zoomsense_lbl').text($(this).val());
});

$('#irissense').on('input', function () {
    $('#irissense_lbl').text($(this).val());
});

$('#focussense').on('input', function () {
    $('#focussense_lbl').text($(this).val());
});

$('#joysticksense').on('input', function () {
    $('#joysticksense_lbl').text($(this).val());
});
