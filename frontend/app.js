var path = require('path');

var logger = require('./modules/logger');
var config = require('./modules/config');

var express = require('express');
var app = express();

console.log(config);

app.use('/public', express.static(__dirname + '/public'));
app.use('/bower_components', express.static(__dirname + '/bower_components'));

// If a GET / request comes in,
// return the index.html page.
app.get('/', function (req, res) {
    res.sendFile(path.join(__dirname + '/public/index.html'));
});

app.listen(config.get('server_port'), function () {
    logger.logMessage(logger.levels.INFO, "Server listening on port " + config.get('server_port') + "!");
});

module.exports = app;
