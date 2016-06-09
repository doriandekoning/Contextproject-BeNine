/**
 * All methods related to the queue table.
 */
var tablesize = 0;

function livetest() {
    performances = [];
    performances.push(new Performance(1, "Test Performance", [1, 2]));

    loadPerformanceDropdown()
}

/**
 * Loads the available performances from the backend.
 */
function loadPerformances() {
}

function loadPerformanceDropdown() {
    var dropdown = $('#selectperformance');
    var list = dropdown.find('.dropdown-menu');

    for (i in performances) {
        var performance = performances[i];

        var li = $("<li></li>");
        var a = $("<a></a>");
        a.text(performance['name']);

        li.append(a);
        list.append(li);
    }

}

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
    var view = $('#select-current');

    if (preset!== undefined) {
        setQueueView(view, preset);
    } else {
        setQueueViewPlaceholder(view, "holder.js/128x77?auto=yes&text=Current&bg=85ca85");
    }
}

/**
 * Sets the setUpNext view.
 * @param preset A preset Object.
 */
function setUpNext(preset) {
    var view = $('#select-upnext');

    if (preset!== undefined) {
        setQueueView(view, preset);
    } else {
        setQueueViewPlaceholder(view, "holder.js/128x77?auto=yes&text=Up Next&bg=f1bf7c");
    }
}

function setQueueViewPlaceholder(view, image) {
    view.find('img').replaceWith($('<img data-src=' + image + ' class="col-xs-9">'));
    view.find('.preset_title').text("-");
    view.find('.preset_camera_number').text("-");
    Holder.run({});
}

function setQueueView(view, preset) {
    view.find('img').attr("src", "/api/backend" + preset['image']);
    view.find('.preset_title').text("Preset " + preset['id']);
    view.find('.preset_camera_number').text(preset['cameraid']);
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