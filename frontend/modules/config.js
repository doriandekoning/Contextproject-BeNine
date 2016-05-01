var fs = require("fs");
var path = require("path");
var logger = require("logger");

var default_path = path.join(__dirname + '/../config.json')
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
        // If no path specified, use the default.
        if (config_path === undefined) {
            config_path = default_path;
        }

        // First check if the file exists, if it doesn't insert the default config.
        if (self.exists(config_path)) {
            try {
                logger.logMessage("INFO", "Reading config file from " + config_path);
                return JSON.parse(fs.readFileSync(config_path));
            } catch (e) {
                logger.logMessage("WARNING", "Config file could not be read from " + config_path);
                throw new Error("Config file at " + config_path + " could not be read.");
            }
        } else {
            try {
                fs.writeFileSync(config_path, JSON.stringify(default_config));
                logger.logMessage("INFO", "Successfully created default log file in " + config_path);
                return default_config;
            } catch (e) {
                logger.logMessage("WARNING", "Could not default write config file file at " + config_path);
                throw new Error("Could not write config file file at " + config_path);
            }
        }
    }
}

module.exports = self;