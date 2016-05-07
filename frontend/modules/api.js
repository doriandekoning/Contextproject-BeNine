var express = require('express');
var config = require('./config');
var request = require('request');
var router = express.Router();

// Address of the backend server.
var address = "http://" + config.get("backend_server") + ":" + config.get("backend_port");

/**
 * First the NodeJS server's own API is defined.
 */
// API call /api/getbackend returns a JSON object with data about the backend server.
router.get('/getbackend', function (req, res) {
    res.json(
        {
            address: config.get('backend_server'),
            port: config.get('backend_port')
        }
    )
});

/**
 * All /backend/... calls are rerouted to the backend server.
 */
router.get('/backend/*', function (req, res) {
    request(address + req.url).pipe(res);
});

module.exports = router;