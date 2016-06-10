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

	delete: function (index) {
		$.get("/api/backend/presetqueues/" + this.id + "/delete", function (data) {
			loadPerformances();
		});
	},

	updateName: function (name) {
		this.name = name;
		$.get("/api/backend/presetqueues/" + this.id + "/edit?name=" + name, function (data) {
			loadPerformances();
		});
	},

	addpreset: function(preset) {
		this.presets.push(preset);
		var position = this.presets.length;
		$.get("/api/backend/presetqueues/" + this.id + "/addpreset?position=" + position + "&presetid=" + preset.id, function (data) {
		});
	},

	moveUp: function(position, preset, callback) {
		var self = this;
		console.log(self);

		$.get("/api/backend/presetqueues/" + self.id + "/deletepreset?position=" + position, function (data) {
			self.presets.splice(position, 1);
			position = position - 1;
			$.get("/api/backend/presetqueues/" + self.id + "/addpreset?position=" + position + "&presetid=" + preset.id, function (data) {
				self.presets.splice(position, 0, preset);
				callback();
			});
		});
	},

	moveDown: function(position, preset, callback) {
		var self = this;

		$.get("/api/backend/presetqueues/" + self.id + "/deletepreset?position=" + position, function (data) {
			self.presets.splice(position, 1);
			position = position + 1;
			$.get("/api/backend/presetqueues/" + self.id + "/addpreset?position=" + position + "&presetid=" + preset.id, function (data) {
				self.presets.splice(position, 0, preset);
				callback();
			});
		});
	},

	deletepreset: function (position) {
		this.presets.splice(position, 1);
		$.get("/api/backend/presetqueues/" + this.id + "/deletepreset?position=" + position, function (data) {
		});
	}
}

/**
 * Loads the performances from the backend server.
 */
function loadPerformances() {
	$.get("/api/backend/presetqueues", function(data) {
		performances = [];

		var obj = JSON.parse(data);
		for (var p in obj['presetqueues']) {
			var performance = obj['presetqueues'][p];
			performances.push(new Performance(performance['id'], performance['name'], performance['queue']));
		}

		loadEditPerformance();
	});
}