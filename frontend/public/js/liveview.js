/**
 * All methods related to the queue table.
 */
var tablesize = 0;

/**
 * Loads the table.
 */
function loadTable() {
    var table = $('#queue_table');

    table.children().remove();
    tablesize = 0;


    for (var key in presets) {
        table.append(createRow(presets[key]));
    }
}

/**
 * Sets the setCurrent view.
 * @param preset A preset Object.
 */
function setCurrent(preset) {
    var current = $('#select-current');

    current.find('img').attr("src", "/api/backend" + preset['image']);
    current.find('.preset_title').text("Preset " + preset['id']);
    current.find('.preset_camera_number').text(preset['cameraid']);
}

/**
 * Sets the setUpNext view.
 * @param preset A preset Object.
 */
function setUpNext(preset) {
    var upnext = $('#select-upnext');

    upnext.find('img').attr("src", "/api/backend" + preset['image']);
    upnext.find('.preset_title').text("Preset " + preset['id']);
    upnext.find('.preset_camera_number').text(preset['cameraid']);
}

/**
 * Creates a row in the preset.
 * @param preset a preset Object.
 * @returns {*|jQuery|HTMLElement} a row which can be appended to the table.
 */
function createRow(preset) {
    tablesize++;
    var row = $("<tr id=\"queuepreset_" + tablesize + "\"></tr>");
    row.append("<th class='column_id'>" + preset['id'] + "</th>");
    row.append("<td class='column_camid'>" + preset['cameraid'] + "</td>");
    row.append("<td class='column_name'>Preset " + preset['id'] + "</td>");
    row.append("<td>" + preset['tags'] + "</td>");

    return row;
}