var supertest = require('supertest');
var server = require('../app/router');
var assert = require('chai').assert;

server = supertest(server);

suite("Routes", function() {
	// Test suite for / route.
	suite("/", function () {
		test('should return Hello World!', function(done) {
			server
      		.get("/")
      		.end(function (err, res) {
      			assert.equal("Fails!", res.text);
      			done();
      		});
		});
	});
});