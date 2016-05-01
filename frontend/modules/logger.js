var fs = require('fs');
var path = require('path');

var default_path = path.join(__dirname + '/../events.log')

// Contains all loglevels, ordered on importance. (0 being most important.)
var levels = {
	ERROR 	: 0,
	WARNING	: 1,
	INFO	: 2,
	DEBUG	: 3,
	TRACE	: 4
};

var self = {
	/**
	 * Clears the logfile, this is called once the server starts.
	 * Generates a new logfile if not existent.
	 * @return -
	 */
	resetLog: function (logger_path) {
        if (logger_path === undefined) {
            logger_path = default_path;
        }
        
        try {
            fs.writeFileSync(logger_path, '');
            self.logMessage("DEBUG", 'Succesfully cleared log file.');
        } catch (e) {
            if (err) throw err;
        }
	},

	/**
	 * Formats the log string.
	 * @param  {String} level   The level
	 * @param  {String} message The message to be logged.
	 * @return {String}         The log string format.
	 */
	formatLog: function (level, message) {
		var date = new Date().toLocaleString();
		var logString = "[" + date + "][" + level + "]>\t\t" + message;
		return logString;
	},

	/**
	 * Returns the possible log level keys.
	 * @return {Array[String]} Array of keys.
	 */
	getLevels: function() {
		return Object.keys(levels);
	},

	/**
	 * Validates a log message, and calls log to log the message.
	 * @param  {String} level   The level
	 * @param  {String} message The message to be logged.
	 * @return Calls log if valid, else throws exception.
	 */
	logMessage: function(level, message, logger_path) {
        if (logger_path === undefined) {
            logger_path = default_path;
        }
        
		if (!levels.hasOwnProperty(level)) {
			throw new Error("Incorrect Logger usage.");
		} else {
			fs.appendFile(logger_path, self.formatLog(level, message) + '\n', function (err) {
				if (err) throw err;
			});
		}
	}

};

module.exports = self;