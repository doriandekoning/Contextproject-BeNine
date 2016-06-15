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

	//Check cameras inuse.
	setInterval(checkCamerasInUse, 2000);

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
* And let the backend now which camera you use.
*/
function checkCamerasInUse() {
	$.get("/api/backend/camera", function(data) {
		var obj = JSON.parse(data);
		for(var c in obj.cameras) {
			var cam = obj.cameras[c];
			var camera = findCameraOnID(cam.id);
			if (camera !== undefined) {
				camera.inuse = cam.inuse;
				setCameraStatus(cam.id);
			}
		}
	});
	$.get("/api/backend/camera/" + currentcamera + "/inuse?inuse=true", function(data) {});
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
				cameras.push(new Camera(cam));
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
		console.log("Cannot switch to camera " + id + " does not exist or is already selected");
	} else {
		var oldID = currentcamera;
		currentcamera = id;
		if (oldID !== undefined) {
			var oldCamera = findCameraOnID(oldID);
			setCameraStatus(oldID);
			$.get("/api/backend/camera/" + oldID + "/inuse?inuse=false", function(data) {});
		}
		setCameraStatus(currentcamera);
		camera.displayControls();
		camera.bigView();
		$('#createPreset').prop('disabled', false);
		$('#autoCreatePresets').prop('disabled', false);
		var preset_create_div = $('#preset_create_div');
		preset_create_div.find('.tags_input').tagsinput('removeAll');
		$.get("/api/backend/camera/" + currentcamera + "/inuse?inuse=true", function(data) {});
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
* Display the right camera status below the camera with camid
* @param camid id of the camera to display the status for.
*/
function setCameraStatus(camid) {
	var camera_area, camera;
	camera_area = $('#camera_area');
	camera = camera_area.find('#camera_' + camid);
	if(parseInt(camid) === parseInt(currentcamera)) {
		camera.find('.camera_status').attr('class', 'camera_status selected');
	} else if (findCameraOnID(camid).inuse) {
		camera.find('.camera_status').attr('class', 'camera_status unavailable');
	} else{
		camera.find('.camera_status').attr('class', 'camera_status available');
	}
}
