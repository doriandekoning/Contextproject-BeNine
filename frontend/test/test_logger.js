var logger = require('../app/logger');
var assert = require('chai').assert;
var path = require('path');
var fs = require('fs');

suite("Logger", function() {
      suite("resetLog", function () {
            test('should clear the logfile.', function(done) {
                  logger.resetLog();

                  var logcontent = fs.readFileSync(path.join(__dirname + '/../' + logger.getLogname()));
                  assert.equal(logcontent.toString(), '');
                  done();
            });
      });      

	// Test suite for log function.
	suite("formatLog", function () {
            // Check if all loglevels are formatted correctly.
            logger.getLevels().forEach(function(level) {
                  test('should format ' + level + ' correctly', function(done) {
                        var time = new Date().toLocaleString();
                        var message = "Test";
                        assert.equal("" + message, logger.formatLog(level, message));
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
            test('should write correctly to the logfile', function(done) {
                  var level = "INFO";
                  var message = "Test";
                  var logformat = logger.formatLog(level, message);
                  logger.logMessage(level, message);

                  fs.readFile(logger.getLogname(), 'utf-8', function(err, data) {
                        if (err) throw err;
                        var lastLine = data.trim().split('\n').slice(-1)[0];
                        assert.equal(logformat, lastLine);
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