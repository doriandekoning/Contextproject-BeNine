var config = require('../modules/config');
var assert = require('chai').assert;
var path = require('path');
var fs = require('fs');

var config_path = path.join(__dirname + '/testconfig.json');

function remove_file() {
    try {
        fs.unlinkSync(config_path);
    } catch (e) {}
}

suite("Config", function() {
    teardown(function() {
        remove_file()
    });
    suite("exists", function () {
        test('should return true if a configfile exists.', function(done) {
            fs.writeFileSync(config_path, 'testdata');

            assert.equal(config.exists(config_path), true);
            done();
        });
        test('should return false if a configfile does not exist.', function(done) {
            assert.equal(config.exists(config_path), false);
            done();
        });
    });
    suite("load", function () {
        test('should return the default config if it does not yet exist.', function(done) {
            assert.equal(config.load(config_path), config.getDefaultConfig());
            done();
        });
        test('should return the contents of the config file if it exists.', function(done) {
            var testdata = JSON.stringify({a: 1});
            fs.writeFileSync(config_path, testdata);

            assert.equal(JSON.stringify(config.load(config_path)), testdata);
            done();
        });
    });
});