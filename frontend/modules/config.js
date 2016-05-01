var fs = require("fs");
var path = require("path");

var default_config = {};

default_config.server = "localhost";

var self = {
    /**
     * Getter method for the default config.
     * @returns {JSON Object}
     */
    getDefaultConfig: function () {
        return default_config;
    },
    /**
     * Checks if a file exists.
     * @param config_path   Path to the file to check.
     * @returns {boolean}
     */
    exists: function (config_path) {
        try {
            fs.statSync(config_path);
            return true;
        } catch (e) {
            return false;
        }
    },
    /**
     * Loads the config from file, if not existent a new file is created and the default is returned.
     * @param config_path       Path to the config file.
     * @returns {JSON Object}
     */
    load: function (config_path) {
        // First check if the file exists, if it doesn't insert the default config.
        if (self.exists(config_path)) {
            try {
                return JSON.parse(fs.readFileSync(config_path));
            } catch (e) {
                throw new Error("Config file at " + config_path + " could not be read.");
            }
        } else {
            try {
                fs.writeFileSync(config_path, JSON.stringify(default_config));
                return default_config;
            } catch (e) {
                throw new Error("Could not write config file file at " + config_path);
            }
        }
    }
}

module.exports = self;