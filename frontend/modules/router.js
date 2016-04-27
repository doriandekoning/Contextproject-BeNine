var express = require('express');
var path = require('path');
var app = express();

// If a GET / request comes in,
// return the index.html page.
app.get('/', function (req, res) {
  res.sendFile(path.join(__dirname + '/../public/index.html'));
});

// Make bower components, and the public directory available.
app.use('/bower_components',  express.static(path.join(__dirname + '/../bower_components')));
app.use('/public',  express.static(path.join(__dirname + '/../public')));

module.exports = app;