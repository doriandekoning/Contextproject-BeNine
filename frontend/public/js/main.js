var cameras = [];
var presets = [];
var performances = [];
var currentcamera;
var editingpreset;

// Document is ready, we can now manipulate it.
$(document).ready(function() {
	
	// Update server status ever 10 seconds
	setInterval(updateServerStatus, 10 * 1000);
	
	// Reload presets every 5 seconds.
	setInterval(loadPresets, 5 * 1000);
	
	// Load the available cameras.
	loadCameras();
	
	//Refresh cameras inuse.
	setInterval(refreshCameras, 2000);
	
	// Load the available presets from the backend.
	loadPresets();

	// Load the available perforamnces from the backend.
	loadPerformances();
	
	Holder.run({});
	console.log('Page has loaded successfully.');
});

/**
* Check if the server is still online and display this.
*/
function updateServerStatus() {
	var statuslabel = $('#server_status');
	$.get('/api/getinfo', function(data) {
		var status = data.backend.status;
		if (status === "online") {
			statuslabel.attr("class", "label label-success");
		} else {
			statuslabel.attr("class", "label label-danger");
		}
	}).fail(function() { 
		statuslabel.attr("class", "label label-danger");
	});
}

/**
* Check which camera's are in use.
*/
function refreshCameras() {
	$.get("/api/backend/camera", function(data) {
		var obj = JSON.parse(data);
		for(var c in obj.cameras) {
			var cam = obj.cameras[c];
			var camera = findCameraOnID(cam.id);
			if (camera !== undefined) {
				camera.inuse = cam.inuse;
			} 
		}
	}).done(function() {
		for(var c in cameras) {
			toggleCamInuse(cameras[c].id, cameras[c].inuse);
		}
	});
	$.get("/api/backend/camera/" + currentcamera + "/inuse?inuse=true", function(data) {console.log(data);});
}

/**
* Load all the cameras from the backend and display them.
*/
function loadCameras() {
	$.get("/api/backend/camera", function(data) {
		var obj = JSON.parse(data);
		for(var c in obj.cameras) {
			var cam = obj.cameras[c];
			if (cam.unavailable === undefined) {
				cameras.push(new Camera(cam.id, cam.inuse, cam.autofocus, cam.autoiris, cam.zoom, cam.move));
			}
		}
	}).done(function() {
		// Generate the camera block area.
		generateCameraArea(cameras.map(function(item){
			return item.id;
		}));
		cameras.forEach(function(item) {
			item.smallView();
		});
		Holder.run({images:"#camera_area img"});
	});
}

/**
* Switch the currentview to the camera with id.
* @param id of the camera to switch to.
*/
function switchCurrentView(id) {
    var camera = findCameraOnID(id);
	if(id === currentcamera || camera === undefined) {
		console.log("Cannot switch to camera " + id + " does not exist");
	} else {
		if (currentcamera !== undefined) {
			toggleCamSelected(currentcamera, false);
			$.get("/api/backend/camera/" + currentcamera + "/inuse?inuse=false", function(data) {console.log(data);});
		}
		currentcamera = id;
		toggleCamSelected(currentcamera, true);
		camera.displayControls();
		camera.bigView();
		$('#createPreset').prop('disabled', false);
		var preset_create_div = $('#preset_create_div');
		preset_create_div.find('.tags_input').tagsinput('removeAll');
		$.get("/api/backend/camera/" + currentcamera + "/inuse?inuse=true", function(data) {console.log(data);});
	}
}

/**
* Search all camera's to find the camera with ID
* @param id of the camera to search for.
*/
function findCameraOnID(id){
	var res = $.grep(cameras, function(item) {
		return parseInt(item.id) === parseInt(id);
	});
	return res[0];
}

/**
* Change the camera inuse color below an image.
* @param camid the id of the camera to change the status of.
* @param inuse boolean to switch between in use.
*/
function toggleCamSelected(camid, inuse) {
	var camera_area, camera;
	camera_area = $('#camera_area');
	camera = camera_area.find('#camera_' + camid);
	if (inuse === true) {
		if (findCameraOnID(camid).inuse) {
			camera.find('.camera_status').attr('class', 'camera_status unavailable');
		} else {
			camera.find('.camera_status').attr('class', 'camera_status selected');
		}
	} else {
		if (findCameraOnID(camid).inuse) {
			camera.find('.camera_status').attr('class', 'camera_status unavailable');
		} else {
			camera.find('.camera_status').attr('class', 'camera_status available');
		}
	}
}

/**
 * Method used to toggle if the camera is in use.
 * @param camid		The id of the camera to toggle.
 * @param inuse		Boolean, true if in use, false otherwise.
 */
function toggleCamInuse(camid, inuse) {
	var camera_area, camera;
	camera_area = $('#camera_area');
	camera = camera_area.find('#camera_' + camid);
	if (inuse === true) {
		if (parseInt(camid) !== parseInt(currentcamera)) {
			camera.find('.camera_status').attr('class', 'camera_status unavailable');
		}
	} else {
		if (parseInt(camid) === parseInt(currentcamera)) {
			camera.find('.camera_status').attr('class', 'camera_status selected');
		} else {
			camera.find('.camera_status').attr('class', 'camera_status available');
		}
	}
}