var selectedPerformance, editingPerformance;

function loadEditPerformance() {
    drawPresets(presets);
    drawPerformancesList();
}

function drawPerformancesList() {
    var performancelist = $('#performance-list');
    performancelist.empty();

    for (var i in performances) {
        var p = performances[i];

        var li = drawPerformanceListItem(p);
        performancelist.append(li);
    }
}

function drawPerformanceListItem(performance) {
    var li = $("<li class='btn btn-info'></li>");
    li.data(performance);
    li.click(selectPerformance);
    var icon = $("<span class='glyphicon glyphicon-bullhorn'></span>");

    li.append(icon);
    li.text(performance['name']);

    return li;
}

function selectPerformance() {
    if (editingPerformance !== undefined) {
        editingPerformance.replaceWith(selectedPerformance);
    }

    selectedPerformance = $(this);
    setNameEditable(this);

    drawSchedule(selectedPerformance.data());
}

function setNameEditable(element) {
    var li = $('<li><div class="row">' +
        '<div class="col-xs-7"><input type="text" class="form-control" placeholder="Name"></div> ' +
        '<div class="col-xs-5"><div class="btn-group"> <button type="button" class="btn btn-danger glyphicon glyphicon-remove-sign"></button> ' +
        '<button type="button" class="btn btn-success glyphicon glyphicon-ok-sign"></button></div></div></div> ' +
        '</li>');


    editingPerformance = li;
    $(element).replaceWith(li);
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
	list.children().remove();
    for (key in presetlist) {
        var preset = presetlist[key];

        list.append(drawPreset(preset));
    }
}

function drawPreset(preset) {
    var li = $("<li class='btn btn-info'></li>");
    var image = $("<img class='img-rounded' src='/api/backend" + preset['image'] + "'>");
    var name = $("<span>Preset " + preset['id'] + "</span>");

    li.append(image, name);
    return li;
}

function createPerformance() {
    $.get("/api/backend/presetqueues/create?name=New", function(data) {
        console.log("create performance response: " + data);
        loadPerformances();
        loadEditPerformance();
    })
}