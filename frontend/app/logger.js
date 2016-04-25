var fs = require('fs');

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
	 * Formats the log string.
	 * @param  {String} level   The level
	 * @param  {String} message The message to be logged.
	 * @return {[type]}         The log string format.
	 */
	formatLog: function (level, message) {
		var date = new Date().toLocaleString();
		var logString = "[" + date + "][" + level + "]>\t" + message;
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
	logMessage: function(level, message) {
		if (!levels.hasOwnProperty(level)) {
			throw new Error("Incorrect Logger usage.");
		} else {
			console.log(self.formatLog(level, message));
		}
	}

};

module.exports = self;