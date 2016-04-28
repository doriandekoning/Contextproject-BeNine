var supertest = require('supertest');
var app = require('../app');
var assert = require('chai').assert;
var fs = require('fs');
var path = require('path');

server = supertest(app);

suite("Routes", function() {
	// Test suite for / route.
	suite("GET /", function () {
		test('should return index.html', function(done) {
			server
      		.get("/")
      		.end(function (err, res) {
      			// First read index.html file contents.
      			var index = fs.readFileSync(path.join(__dirname + '/../public/index.html'));
      			// Now compare these to the plaintext returned by the GET request.
      			assert.equal(index.toString(), res.text);
      			done();
      		});
		});
	});
});