var selectedPerformance, nameEditingPerformance;

/**
 * Loads the edit performance window.
 */
function loadEditPerformance() {
    drawPresets(presets);
    drawPerformancesList();
    drawSchedule();
}

/**
 * Draws the performance list from which the performance to edit can be selected.
 */
function drawPerformancesList() {
    var performancelist = $('#performance-list');
    performancelist.empty();

    for (var i in performances) {
        var p = performances[i];

        var li = drawPerformanceListItem(p);
        performancelist.append(li);
    }
}

/**
 * Draws one list item from the performance list.
 * @param performance   The performance object to draw.
 * @returns {*|jQuery|HTMLElement} a jQuery DOM element to append.
 */
function drawPerformanceListItem(performance) {
    var li = $("<li class='btn btn-info'></li>");
    li.data(performance);
    li.click(selectPerformance);
    var icon = $("<span class='glyphicon glyphicon-bullhorn'> " + performance['name'] + "</span>");

    li.append(icon);
    return li;
}

/**
 * Selects a performance
 */
function selectPerformance() {
    if (nameEditingPerformance !== undefined) {
        nameEditingPerformance.replaceWith(drawPerformanceListItem($(nameEditingPerformance).data()));
    }

    if (selectedPerformance !== undefined) {
        selectedPerformance.attr('class', 'btn btn-info');
    }

    selectedPerformance = $(this);
    selectedPerformance.attr('class', 'btn btn-info disabled');

    drawSchedule(selectedPerformance.data());
}

function setNameEditable() {
    if (selectedPerformance !== undefined) {
        var element = selectedPerformance;

        var li = $('<li><div class="row">' +
            '<div class="col-xs-7"><input type="text" id="performance-name" class="form-control" value="' + $(element).data()['name'] + '"></div> ' +
            '<div class="col-xs-5"><div class="btn-group editname"><button type="button" onclick="selectPerformance()" class="btn btn-danger glyphicon glyphicon-remove-sign"></button> ' +
            '<button type="button" onclick="saveEditName()" class="btn btn-success glyphicon glyphicon-ok-sign"></button></div></div></div> ' +
            '</li>');

        li.data($(element).data());
        nameEditingPerformance = li;
        $(element).replaceWith(li);
    }
}

/**
 * Saves the edited name.
 */
function saveEditName() {
    var performance = nameEditingPerformance.data();
    performance.updateName($("#performance-name").val());

    loadEditPerformance();
}

/**
 * Deletes a performance.
 */
function deletePerformance() {
    if (selectedPerformance !== undefined) {
        var performance = selectedPerformance.data();

        performance.delete();
        selectedPerformance.remove();

        var deleteindex = performances.indexOf(performance);
        if (deleteindex !== -1) {
            performances.splice(deleteindex, 1);
        }

    }
}

/**
* Called on input of the preset search in the edit performance window.
*/
function presetSearchInput(val) {
	if (val !== '') {
		var matchingpresets = matchingPresets(val);
		drawPresets(matchingpresets);
	} else {
		drawPresets(presets);
	}
}

/**
* When a tags suggestion is selected cal the tagSearchInput function.
*/
$('#presetsearch_input.typeahead').on('typeahead:selected',function (e, val) {
	presetSearchInput(val);
});

/**
* Display the typahead in the search box of the performance window.
*/
$('#presetsearch_input').typeahead({
		highlight: true,
		minLength: 0
	},
	{
		name: 'tags',
		source: tagsWithDefaults,
		limit:25
	}
);

/**
 * Draws the schedule for a given performance.
 * @param performance A performance object.
 */
function drawSchedule(performance) {
    var schedule = $("#performance-schedule");
    schedule.empty();

    if (performance !== undefined) {
        var performancepresets = performance.presets;
        for (i in performancepresets) {
            addScheduleRow(performancepresets[i]);
        }
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
    item.data(preset);

    var group = $("<div class='btn-group'></div>");
    var count = $("<button type='button' class='btn btn-default schedule-list-number'></button>");

    if (preset['name'] !== '') {
        var presetname = $("<button type='button' class='btn btn-info schedule-preset'>" + preset['name'] + "</button>");
    } else {
        var presetname = $("<button type='button' class='btn btn-info schedule-preset'>Preset " + preset['id'] + "</button>");
    }

    var buttonUp = $("<button type='button' class='btn btn-default glyphicon glyphicon glyphicon-menu-up'></button>");
    var buttonDown = $("<button type='button' class='btn btn-default glyphicon glyphicon glyphicon-menu-down'></button>");
    var buttonRemove = $("<button type='button' class='btn btn-danger glyphicon glyphicon-remove-sign'></button>");

    buttonUp.click(moveScheduleUp);
    buttonDown.click(moveScheduleDown);
    buttonRemove.click(deleteFromSchedule);

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
    var preset = current.data();

    var performance = selectedPerformance.data();
    var index = current.index();

    console.log(index)

    if (index > 0) {
        performance.moveUp(index, preset, function() {
            drawSchedule(performance);
        });
    }
}

/**
 * Moves an item down in the schedule.
 */
function moveScheduleDown() {
    var current = $(this).closest('li');
    var preset = current.data();

    var performance = selectedPerformance.data();
    var index = current.index();

    if (index < performance.presets.length) {
        performance.moveDown(index, preset, function() {
            drawSchedule(performance);
        });
    }
}

/**
 * Draws the presets to select.
 * @param presetlist a List of presets.
 */
function drawPresets(presetlist) {
    var list = $("#performance-preset-selector");
	list.children().remove();
    for (key in presetlist) {
        var preset = presetlist[key];

        var presetrow = $(drawPreset(preset));
        presetrow.data(preset);
        presetrow.click(addToSchedule);

        list.append(presetrow);
    }
}

/**
 * Adds a preset to the schedule.
 */
function addToSchedule() {
    var preset = $(this).data();
    var performance = selectedPerformance.data();

    performance.addpreset(preset);
    drawSchedule(performance);
}

/**
 * Deletes a preset from the schedule.
 */
function deleteFromSchedule() {
    var preset = $(this);
    var li = preset.closest('preset');

    var performance = selectedPerformance.data();
    performance.deletepreset(li.index() + 1);
    drawSchedule(performance)
}

/**
 * Draws a preset to the schedule list.
 * @param preset a preset object.
 * @returns {*|jQuery|HTMLElement} the row to draw.
 */
function drawPreset(preset) {
    var li = $("<li class='btn btn-info'></li>");
    var image = $("<img class='img-rounded' src='/api/backend" + preset['image'] + "'>");

    if (preset['name'] !== '') {
        var name = $("<span>" + preset['name'] + "</span>");
    } else {
        var name = $("<span>Preset " + preset['id'] + "</span>");
    }

    li.append(image, name);
    return li;
}

/**
 * Creates a new performance.
 */
function createPerformance() {
    $.get("/api/backend/presetqueues/create?name=New%20Performance", function(data) {
        loadPerformances();
    }).done(loadEditPerformance);
}