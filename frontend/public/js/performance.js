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
	}
};

