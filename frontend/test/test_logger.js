var logger = require('../app/logger');
var assert = require('chai').assert;

suite("Logger", function() {
	// Test suite for log function.
	suite("formatLog", function () {
            // Check if all loglevels are formatted correctly.
            logger.getLevels().forEach(function(level) {
                  test('should format ' + level + ' correctly', function(done) {
                        var time = new Date().toLocaleString();
                        var message = "Test";
                        assert.equal("[" + time + "][" + level + "]>\t" + message, logger.formatLog(level, message));
                        done();
                  });
            });
	});

      suite("logMessage", function () {
            // Check if valid loglevels do not throw an exception.
            logger.getLevels().forEach(function(level) {
                  test('should not throw an exception on ' + level, function(done) {
                        var message = "Test";
                        assert.doesNotThrow(logger.logMessage.bind(logger, level, message), Error);
                        done();
                  });
            });
            
      test('should throw an exception on an invalid level', function(done) {
                  var message = "Test";
                  var level = "INVALIDLEVEL";
                  assert.throws(logger.logMessage.bind(logger, level, message), Error);
                  done();
            });     
      });
});