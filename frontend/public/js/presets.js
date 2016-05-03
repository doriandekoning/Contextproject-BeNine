var address;

// Hold the ready until all data about the backend server is fetched.
$.holdReady(true);

$.get('http://localhost:3000/api/getserver', function(data) {
    address = 'http://' + data.address + ':' + data.port;
    $.holdReady(false);
});

// Document is ready, we can now manipulate it.
$(document).ready(function() {

    $("#preset_area").remove();
    console.log('Page has loaded successfully.');

});