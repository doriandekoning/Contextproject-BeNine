/**
 * Returns the settings in the cookie as a JSON object.
 * @returns {JSON}
 */
function getSettings() {
    var settings = Cookies.getJSON();

    // Check if there are settings set, if not generate defaults.
    if ($.isEmptyObject(settings)) {
        setSetting("testvalue", 1);
    }

    return settings;
}

/**
 * Sets a setting in the cookie.
 * @param parameter The parameter to set.
 * @param value     The value of this parameter.
 */
function setSetting(parameter, value) {
    Cookies.set(parameter, value, { expires: 5000 });
}

/**
 * Clears all settings set as a cookie.
 */
function clearCookies() {
    var settings = Cookies.getJSON();
    var keys = Object.keys(settings);

    for(var i = 0; i < keys.length; i++) {
        Cookies.remove(keys[i]);
    }
}