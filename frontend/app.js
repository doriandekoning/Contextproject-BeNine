var server = require('./modules/router');
var logger = require('./modules/logger');

server.listen(3000, function () {
	// Once the server starts, reset the logfile.
	logger.resetLog(function () {
		logger.logMessage("INFO", "Server listening on port 3000!");
	});
});