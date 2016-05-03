var address;

// Hold the ready until all data about the backend server is fetched.
$.holdReady(true);

$.get('http://localhost:3000/api/getserver', function(data) {
    address = 'http://' + data.address + ':' + data.port;
    $.holdReady(false);
});

function addPresetRow() {
    var presetrow = $('<div class="row"></div>');
    var rowcontainer = $('<div class="col-xs-6"></div>');

    for (var i = 0; i < 4; i++) {
        rowcontainer.append($('<div class="col-xs-3"></div>'));
    }

    rowcontainer.children().each( function(index, elem) {
        index++;
        $(elem).append($('<div class = "available"><img src="public/test2.jpg"><h5>Preset ' + index + '</h5></div>'));
    });

    presetrow.append(rowcontainer);
    $("#preset_area").append(presetrow);
}


// Document is ready, we can now manipulate it.
$(document).ready(function() {

    for (var i = 0; i < 4; i++) {
        addPresetRow();
    }

    console.log('Page has loaded successfully.');

});