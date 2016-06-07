var path = require('path');

var logger = require('./modules/logger');
var config = require('./modules/config');
var api = require('./modules/api');

var express = require('express');
var app = express();

app.set('view engine', 'ejs');

app.use('/public', express.static(__dirname + '/public'));
app.use('/bower_components', express.static(__dirname + '/bower_components'));

// If a GET / request comes in,
// return the index.html page.
app.get('/', function (req, res) {
    res.render('pages/camera-view');
});

// Use the api module for all /api calls.
app.use('/api', api);

app.listen(config.get('server_port'), function () {
    logger.logMessage(logger.levels.INFO, "Server listening on port " + config.get('server_port') + "!");
});

module.exports = app;
