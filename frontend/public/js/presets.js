var address;
var presetcounter = 0;

// Hold the ready until all data about the backend server is fetched.
$.holdReady(true);

$.get('http://localhost:3000/api/getserver', function(data) {
    address = 'http://' + data.address + ':' + data.port;
    $.holdReady(false);
});

// Document is ready, we can now manipulate it.
$(document).ready(function() {

    // Generate the presets.
    generatePresets();
    console.log('Page has loaded successfully.');

});

function generatePresets() {
    for (var i = 0; i < 4; i++) {
        addPresetRow();
    }
}

function addPresetRow() {
    var preset_row, preset_column, row_container

    preset_row = $('<div class="row"></div>');
    row_container = $('<div class="col-xs-6"></div>');

    // Generate four columns.
    for (var i = 0; i < 4; i++) {
        preset_column = $('<div class="col-xs-3"></div>');
        row_container.append(preset_column);
    }

    // Now for each column add the preset block.
    row_container.children().each( function(index, elem) {
        presetcounter++;
        addPreset(elem);
    });

    // Now append the preset row.
    preset_row.append(row_container);
    $("#preset_area").append(preset_row);
}

function addPreset(elem) {
    var preset_image, preset_caption, preset_image_div;

    preset_image_div = $('<div class = "none"></div>');
    preset_image = $('<img data-src="holder.js/128x77?auto=yes&text=Preset ' + presetcounter + '&bg=8b8b8b" >').get(0);
    preset_caption = $('<h5>Preset ' + presetcounter + '</h5>');

    preset_image_div.attr("id", "preset_" + presetcounter);

    // Run the placeholder creator.
    Holder.run({
        images: preset_image
    });

    preset_image_div.append(preset_image, preset_caption);
    $(elem).append(preset_image_div);
}