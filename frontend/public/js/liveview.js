/**
 * All methods related to the queue table.
 */

var tablesize = 0;

/**
 * Loads the table.
 */
function loadTable() {
    var table = $('#queue_table');

    table.append(createRow("Test", "2", "tags"));
    table.append(createRow("Test", "2", "tags"));
    table.append(createRow("Test", "2", "tags"));
    table.append(createRow("Test", "2", "tags"));
    table.append(createRow("Test", "2", "tags"));
    table.append(createRow("Test", "2", "tags"));

}

function createRow(name, camnumber, tags) {
    tablesize = tablesize + 1;
    var row = $("<tr id=\"queuepreset_" + tablesize + "\"></tr>");
    row.append("<th>" + tablesize + "</th>");
    row.append("<td>" + name + "</td>");
    row.append("<td>" + camnumber + "</td>");
    row.append("<td>" + tags + "</td>");

    return row;
}

loadTable();