var express = require('express');
var config = require('./config');
var logger = require('./logger');
var request = require('request');
var router = express.Router();

// Address of the backend server.
var address = "http://" + config.get("backend_server") + ":" + config.get("backend_port");
logger.logMessage(logger.levels.INFO, "Backend server address: " + address);

/**
 * Middleware to log every API request.
 */
router.use(function (req, res, next) {
    logger.logMessage(logger.levels.INFO, "Received request: " + req.url);
    next();
});

/**
 * Reports the status of the backend server.
 * @param callback  function accepting a string. (status)
 */
function backend_status(callback) {
    request(address + '/')
        .on('error', function() {
            callback('offline');
        })
        .on('response', function () {
            callback('online');
        });
}

/**
 * First the NodeJS server's own API is defined.
 */
// API call /api/getinfo returns a JSON object with info about the server.
router.get('/getinfo', function (req, res) {
    backend_status(function (status) {
        res.json(
            {
                backend: {
                    address: config.get('backend_server'),
                    port: config.get('backend_port'),
                    status: status
                }
            }
        )
    })
});

/**
 * All /backend/... calls are rerouted to the backend server.
 */
router.get('/backend/*', function (req, res) {
    request(address + req.url)
        .on('error', function (err) {
            // An error has occurred, most likely the server has not been started.
            if (err.code === 'ECONNREFUSED') {
                logger.logMessage(logger.levels.ERROR, "Connection with back-end server failed, is the back-end server online?");
            }
        })
        .pipe(res);

});

module.exports = router;