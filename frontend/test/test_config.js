var config = require('../modules/config');
var assert = require('chai').assert;
var path = require('path');
var fs = require('fs');
var teardown = require("mocha/lib/mocha.js").teardown;
var suite = require("mocha/lib/mocha.js").suite;

var config_path = path.join(__dirname + '/testconfig.json');

function remove_file() {
    try {
        fs.unlinkSync(config_path);
    } catch (e) {
    }
}

suite("Config", function () {
    teardown(function () {
        remove_file()
    });
    suite("exists", function () {
        test('should return true if a configfile exists.', function (done) {
            fs.writeFileSync(config_path, 'testdata');

            assert.equal(config.exists(config_path), true);
            done();
        });
        test('should return false if a config file does not exist.', function (done) {
            assert.equal(config.exists(config_path), false);
            done();
        });
    });
    suite("load", function () {
        test('should return the default config if it does not yet exist.', function (done) {
            assert.equal(config.load(config_path), config.getDefaultConfig());
            done();
        });
        test('should return the contents of the config file if it exists.', function (done) {
            var testdata = JSON.stringify({a: 1});
            fs.writeFileSync(config_path, testdata);

            assert.equal(JSON.stringify(config.load(config_path)), testdata);
            done();
        });
    });
    suite("get", function () {
        test('should return the correct value of a config parameter.', function (done) {
            config.load(config_path);
            assert.equal(config.get('server_port'), 3000);
            done();
        });
    });
    suite("validate", function () {
        test('should return true if the config is valid.', function (done) {
            var test_config = JSON.parse('{"server_port":3000,"backend_server":"localhost","backend_port":8888}');
            assert.equal(config.validate(test_config), true);
            done();
        });
        test('should return false if the config is invalid.', function (done) {
            var test_config = JSON.parse('{"server_port":3000,"backend_server":"localhost"}');
            assert.equal(config.validate(test_config), false);
            done();
        });
        test('should return false if the config contains invalid keys.', function (done) {
            var test_config = JSON.parse('{"server_port":3000,"backend_server":"localhost","backend_port":8888,"invalid":8888}');
            assert.equal(config.validate(test_config), false);
            done();
        });
        test('should return false a JSON syntax error occurs.', function (done) {
            var test_config = JSON.parse('{"server_port":3000,"backend_server","localhost"}');
            assert.equal(config.validate(test_config), false);
            done();
        });
    })
});