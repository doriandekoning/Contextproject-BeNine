var express = require('express');
var path = require('path');
var app = express();

// If a GET / request comes in,
// return the index.html page.
app.get('/', function (req, res) {
  res.sendFile(path.join(__dirname + '/web/index.html'));
});

module.exports = app;