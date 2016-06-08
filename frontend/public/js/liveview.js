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

function createRow(preset) {
    tablesize++;
    var row = $("<tr id=\"queuepreset_" + tablesize + "\"></tr>");
    row.append("<th class='column_id'>" + preset['id'] + "</th>");
    row.append("<td class='column_camid'>" + preset['cameraid'] + "</td>");
    row.append("<td class='column_name'>Preset " + preset['id'] + "</td>");
    row.append("<td>" + preset['tags'] + "</td>");

    return row;
}