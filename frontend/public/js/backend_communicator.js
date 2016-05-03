var address;

// Hold the ready until all data about the backend server is fetched.
$.holdReady(true);

$.get('http://localhost:3000/api/getserver', function(data) {
    address = 'http://' + data.address + ':' + data.port;
    $.holdReady(false);
});

$(document).ready(function() {
    
    console.log('Page has loaded successfully.');

});