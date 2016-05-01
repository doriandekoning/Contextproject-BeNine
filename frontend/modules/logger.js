var fs = require('fs');
var path = require('path');

var default_path = path.join(__dirname + '/../events.log');

var self = {
    // Contains all loglevels ordered on importance and their naming. (0 being most important.)
    levels: {
        ERROR 	: { name: "ERROR", importance: 0},
        WARNING	: { name: "WARNING", importance: 1},
        INFO	: { name: "INFO", importance: 2},
        DEBUG	: { name: "DEBUG", importance: 3},
        TRACE	: { name: "TRACE", importance: 4}
    },
	/**
	 * Clears the logfile, this is called once the server starts.
	 * Generates a new logfile if not existent.
	 */
	resetLog: function (logger_path) {
        if (logger_path === undefined) {
            logger_path = default_path;
        }
        
        try {
            fs.writeFileSync(logger_path, '');
            self.logMessage(self.levels.DEBUG, 'Successfully cleared log file.');
        } catch (e) {
            console.log(e)
        }
	},

	/**
	 * Formats the log string.
	 * @param  {object} level   The level
	 * @param  {String} message The message to be logged.
	 * @return {String}         The log string format.
	 */
	formatLog: function (level, message) {
		var date = new Date().toLocaleString();
		var logString = "[" + date + "][" + level["name"] + "]>\t\t" + message;
		return logString;
	},

	/**
	 * Returns the possible log level keys.
	 * @return {Array.String} Array of keys.
	 */
	getLevels: function() {
		return Object.keys(self.levels);
	},

	/**
	 * Validates a log message, and calls log to log the message.
	 * @param  {object} level   The level
	 * @param  {String} message The message to be logged.
     * @param  {String=} logger_path to the logger.
	 * @return Calls log if valid, else throws exception.
	 */
	logMessage: function(level, message, logger_path) {
        if (logger_path === undefined) {
            logger_path = default_path;
        }
		if (!self.levels.hasOwnProperty(level["name"])) {
			throw new Error("Incorrect Logger usage.");
		} else {
			fs.appendFile(logger_path, self.formatLog(level, message) + '\n', function (err) {
				if (err) throw err;
			});
		}
	}

};

module.exports = self;