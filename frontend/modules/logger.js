var fs = require('fs');
var path = require('path');

var default_path = path.join(__dirname + '/../events.log');

/**
 * Constructor for a new Logger.
 * @constructor
 */
var Logger = function () {
    // Log levels ordered on importance and their naming.
    this.levels = {
        ERROR: {name: "ERROR", importance: 0},
        WARNING: {name: "WARNING", importance: 1},
        INFO: {name: "INFO", importance: 2},
        DEBUG: {name: "DEBUG", importance: 3},
        TRACE: {name: "TRACE", importance: 4}
    };

    this.resetLog();
};

/**
 * Clears the logfile, this is called once the logger object initiates..
 * Generates a new logfile if not existent.
 */
Logger.prototype.resetLog = function (logger_path) {
    if (logger_path === undefined) {
        logger_path = default_path;
    }

    try {
        fs.writeFileSync(logger_path, '');
        this.logMessage(this.levels.DEBUG, 'Successfully cleared log file.');
    } catch (e) {
        console.log(e)
    }
};

/**
 * Formats the log string.
 * @param  {object} level   The level
 * @param  {String} message The message to be logged.
 * @return {String}         The log string format.
 */
Logger.prototype.formatLog = function (level, message) {
    var date = new Date().toLocaleString();
    return "[" + date + "][" + level["name"] + "]>\t\t" + message;
};

/**
 * Returns the possible log level keys.
 * @return {String[]} Array of keys.
 */
Logger.prototype.getLevels = function () {
    return Object.keys(this.levels);
};

/**
 * Validates a log message, and calls log to log the message.
 * @param  {object} level   The level
 * @param  {String} message The message to be logged.
 * @param  {String=} logger_path to the logger.
 * @return Calls log if valid, else throws exception.
 */
Logger.prototype.logMessage = function (level, message, logger_path) {
    if (logger_path === undefined) {
        logger_path = default_path;
    }
    if (!this.levels.hasOwnProperty(level["name"])) {
        throw new Error("Incorrect Logger usage.");
    } else {
        fs.appendFile(logger_path, this.formatLog(level, message) + '\n', function (err) {
            if (err) throw err;
        });
    }
};

module.exports = new Logger();