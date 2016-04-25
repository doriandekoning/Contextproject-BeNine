var server = require('./router');
var logger = require('./logger');

server.listen(3000, function () {
	logger.logMessage("INFO", "Server listening on port 3000!");
});