var express = require('express');
var config = require('./config');
var router = express.Router();

// API call /api/getserver returns a JSON object with data about the backend server.
router.get('/getserver', function (req, res) {
    res.json(
        {
            address: config.get('backend_server'),
            port: config.get('backend_port')
        }
    )
});

module.exports = router;