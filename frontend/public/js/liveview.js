/**
 * All methods related to the queue table.
 */
var tablesize = 0;
var position = -1;
var currentperformance;
var current;
var next;

function loadPerformanceDropdown() {
    var dropdown = $('#selectperformance');
    var list = dropdown.find('.dropdown-menu');

    list.empty();
    if (performances.length > 0) {
        for (var i in performances) {
            var performance = performances[i];

            var li = $("<li></li>");
            var a = $("<a></a>");
            a.text(performance['name']);
            a.data(performance);
            a.click(loadPerformance);

            li.append(a);
            list.append(li);
        }
    } else {
        var li = $("<li class='disabled'><a href='#'>No Performances Available</a></li>");
        list.append(li);
    }
}

function loadPerformance() {
    var performance = $(this).data();
    currentperformance = performance;

    var dropdown = $('#selectperformance-button');
    dropdown.text(performance['name'] + ' ');
    dropdown.append($("<span class='caret'></span>"));

    var performance_presets = performance['presets'];

    loadTable(performance_presets);
    updateCurrentUpNext(position, position + 1, performance_presets[-1], performance_presets[0]);
}

/**
 * Loads the table.
 */
function loadTable(presets) {
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

    if (preset !== undefined) {
        setQueueView(view, preset);
        preset.recallPreset();
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

    if (preset !== undefined) {
        setQueueView(view, preset);
    } else {
        setQueueViewPlaceholder(view, "holder.js/128x77?auto=yes&text=Up%20Next&bg=f1bf7c");
    }
}

function setRowUpNext(count) {
    var table = $('#queue_table');

    table.children().eq(0).children().removeClass('upnext');

    if (count > -1) {
        var row = table.children().eq(0).children().eq(count);
        if (row !== undefined) {
            row.addClass('upnext');
        }
    }
}

function setRowCurrent(count) {
    var table = $('#queue_table');

    table.children().eq(0).children().removeClass('current');

    if (count > -1) {
        var row = table.children().eq(0).children().eq(count);
        if (row !== undefined) {
            row.addClass('current');
        }
    }
}


function setQueueViewPlaceholder(view, image) {
    view.find('img').replaceWith($('<img data-src=' + image + ' class="col-xs-9">'));
    view.find('.preset_title').text("-");
    view.find('.preset_camera_number').text("-");
    Holder.run();
}

function setQueueView(view, preset) {
    view.find('img').replaceWith($("<img src='/api/backend" + preset['image'] + "' class='col-xs-9'>"));
    view.find('.preset_title').text(preset['name']);
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
    row.append("<td class='column_name'>" + preset['name'] + "</td>");
    row.append("<td>" + preset['tags'] + "</td>");

    return row;
}

function nextPreset() {
    var performance_presets = currentperformance['presets'];

    if (position < (performance_presets.length - 1)) {
        position = position + 1;
        updateCurrentUpNext(position, position + 1, performance_presets[position], performance_presets[position + 1]);
    }
}

function prevPreset() {
    var performance_presets = currentperformance['presets'];

    if (position > -1) {
        position = position - 1;
        updateCurrentUpNext(position, position + 1, performance_presets[position], performance_presets[position + 1])
    }
}

function updateCurrentUpNext(curpos, nextpos, current, upnext) {
    setRowCurrent(curpos);

    setCurrent(current);

    setRowUpNext(nextpos);
    setUpNext(upnext);
}