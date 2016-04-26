var fs = require('fs');
var logfilename = "events.log";

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
	 * Returns the name of the logfile.
	 * @return {String} 		name of the logfile.
	 */	
	getLogname: function() {
		return logfilename;
	},

	/**
	 * Clears the logfile, this is called once the server starts.
	 * @return -
	 */
	resetLog: function (callback) {
		fs.writeFile(logfilename, '', function(err) {
			if (err) throw err;
			self.logMessage("DEBUG", 'Succesfully cleared log file.');
			callback();
		});
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
	logMessage: function(level, message) {
		if (!levels.hasOwnProperty(level)) {
			throw new Error("Incorrect Logger usage.");
		} else {
			fs.appendFile(logfilename, self.formatLog(level, message) + '\n', function (err) {
				if (err) throw err;
			});
		}
	}

};

module.exports = self;