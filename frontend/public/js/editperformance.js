/**
 * Adds a row to the schedule
 * @param count The number in the schedule.
 * @param preset The preset to add.
 */
function addScheduleRow(count, preset) {
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

function updateScheduleOrder() {
    var li = $('#performance-schedule li');

    console.log(li);

    li.each(function(index) {
        li.eq(index).find(".schedule-list-number").eq(0).text(index + 1);
    })
}

function moveScheduleUp() {
    var current = $(this).closest('li');
    var previous = current.prev('li');

    if (previous.length !== 0) {
        current.insertBefore(previous);
    }

    updateScheduleOrder();
}

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