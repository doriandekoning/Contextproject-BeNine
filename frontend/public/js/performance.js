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
	setPresets: function (queue) {
		var presets = [];

		for (i in queue) {
			var preset = findPresetOnID(queue[i]);
			if (preset !== undefined) {
				presets.push(preset);
			}
		}

		return presets;
	},

	update: function (newperformance) {
		this.name = newperformance.name;
		this.queue = newperformance.queue;
	},

	delete: function () {
		$.get("/api/backend/presetqueues/" + this.id + "/delete", function (data) {});
	},

	addpreset: function (preset) {
		this.presets.push(preset);
		var position = this.presets.length;
		$.get("/api/backend/presetqueues/" + this.id + "/addpreset?position=" + position + "&presetid=" + preset.id, function (data) {});
	}
}

function loadPerformances() {
	$.get("/api/backend/presetqueues", function(data) {
		performances = [];

		var obj = JSON.parse(data);
		for (var p in obj['presetqueues']) {
			var performance = obj['presetqueues'][p];
			performances.push(new Performance(performance['id'], performance['name'], performance['queue']));
		}
	});
}