function getSettings() {
    var settings = Cookies.getJSON();

    // Check if there are settings set, if not generate defaults.
    if ($.isEmptyObject(settings)) {
        setSetting("testvalue", 1);
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