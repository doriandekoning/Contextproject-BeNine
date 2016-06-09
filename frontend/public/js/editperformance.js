var currentPerformance;

function test() {
    performances.push(new Performance(1, "Test Performance", [1, 2]));

    currentPerformance = performances[0];
    drawSchedule(currentPerformance);
}

/**
 * Draws the schedule for a given performance.
 * @param performance A performance object.
 */
function drawSchedule(performance) {
    var schedule = $("#performance-schedule");
    schedule.empty();

    var performancepresets = performance.presets;
    for (i in performancepresets) {
        addScheduleRow(performancepresets[i]);
    }
}

/**
 * Adds a row to the schedule
 * @param count The number in the schedule.
 * @param preset The preset to add.
 */
function addScheduleRow(preset) {
    var schedule = $("#performance-schedule");

    var item = $("<li></li>");

    var group = $("<div class='btn-group'></div>")
    var count = $("<button type='button' class='btn btn-default schedule-list-number'></button>");
    var presetname = $("<button type='button' class='btn btn-info schedule-preset'>Preset " + preset['id'] + "</button>");

    var buttonUp = $("<button type='button' class='btn btn-default glyphicon glyphicon glyphicon-menu-up'></button>");
    var buttonDown = $("<button type='button' class='btn btn-default glyphicon glyphicon glyphicon-menu-down'></button>");
    var buttonRemove = $("<button type='button' class='btn btn-danger glyphicon glyphicon-remove-sign'></button>");

    buttonUp.click(moveScheduleUp);
    buttonDown.click(moveScheduleDown);

    group.append(count, presetname, buttonUp, buttonDown, buttonRemove);
    item.append(group);
    schedule.append(item);

    updateScheduleOrder();
}

/**
 * Updates the order of the schedule numbers.
 */
function updateScheduleOrder() {
    var li = $('#performance-schedule li');

    li.each(function(index) {
        li.eq(index).find(".schedule-list-number").eq(0).text(index + 1);
    })
}

/**
 * Moves an item up in the schedule.
 */
function moveScheduleUp() {
    var current = $(this).closest('li');
    var previous = current.prev('li');

    if (previous.length !== 0) {
        current.insertBefore(previous);
    }

    updateScheduleOrder();
}

/**
 * Moves an item down in the schedule.
 */
function moveScheduleDown() {
    var current = $(this).closest('li');
    var previous = current.next('li');

    if (previous.length !== 0) {
        current.insertAfter(previous);
    }

    updateScheduleOrder();
}

function drawPresets(presetlist) {
    var list = $("#performance-preset-selector");

    for (key in presetlist) {
        var preset = presetlist[key];

        var li = $("<li class='btn btn-info'></li>");
        var image = $("<img class='img-rounded' src='/api/backend" + preset['image'] + "'>");
        var name = $("<span>Preset " + preset['id'] + "</span>");

        li.append(image, name);
        list.append(li);
    }

}