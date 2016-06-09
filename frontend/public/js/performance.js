/**
* Performance object containing all information about a performance Queue.
*/
function Performance(id, name, queue) {
	this.id = id;
	this.name = name;
	this.queue = queue;
	this.presets = this.setPresets(queue);
}

Performance.prototype = {
	setPresets: function(queue) {
		var presets = [];

		for (i in queue) {
			var preset = findPresetOnID(queue[i]);
			if (preset !== undefined) {
				presets.push(preset);
			}
		}

		return presets;
	},

	update: function(newperformance) {
		this.name = newperformance.name;
		this.queue = newperformance.queue;
	}
};

function loadPerformances() {
	$.get("/api/backend/presetqueues", function(data) {
		var obj = JSON.parse(data);
		for (var p in obj['presetqueues']) {
			var performance = obj['presetqueues'][p];
			checkPerformance(performance);
		}
	});
}

function checkPerformance(object) {
	var exists = findPerformanceOnID(object.id);
	if (exists === undefined) {
		performances.push(new Performance(object['id'], object['name'], object['queue']));
	} else {
		exists.update(object);
	}
}

function findPerformanceOnID(id){
	var res = $.grep(performances, function(item, n) {
		return parseInt(item.id) === parseInt(id);
	});
	return res[0];
}