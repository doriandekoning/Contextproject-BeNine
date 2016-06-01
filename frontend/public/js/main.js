var cameras = [];
var presets = [];
var currentcamera;
var editingpreset;

// Document is ready, we can now manipulate it.
$(document).ready(function() {
	
	// Load the available cameras.
	loadCameras();
	
	// Load the available presets from the backend.
	loadPresets();

    console.log('Page has loaded successfully.');
});

/**
* Load all the cameras from the backend and display them.
*/
function loadCameras() {
	$.get("/api/backend/camera", function(data) {
		var obj = JSON.parse(data)
		for(var c in obj.cameras) {
			var cam = obj.cameras[c];
			cameras.push(new Camera(cam.id, cam.inuse, cam.autofocus, cam.autoiris, cam.zoom, cam.move));
		}
	}).done(function() {
		// Generate the camera block area.
		generateCameraArea(cameras.map(function(item){
			return item.id;
		}));
		cameras.forEach(function(item) {
			item.smallView();
		});
	});
}

/**
* Switch the currentview to the camera with id.
* @param id of the camera to switch to.
*/
function switchCurrentView(id) {
    var camera;
	if(id !== currentcamera) {
		toggleCamSelected(currentcamera, false);
		currentcamera = id;
		toggleCamSelected(currentcamera, true);
		camera = findCameraOnID(id);
		camera.displayControls();
		camera.bigView();
		$('#createPreset').prop('disabled', false);
		$('#preset_create_div .tags_input').tagsinput('removeAll');
	}
}

/**
* Search all camera's to find the camera with ID
* @param id of the camera to search for.
*/
function findCameraOnID(id){
	var res = $.grep(cameras, function(item, n) {
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
	camera_area = $('#camera_area');
	camera = camera_area.find('#camera_' + camid);
	if (inuse === true) {
		camera.find('.camera_status').attr('class', 'camera_status selected');
	} else {
		camera.find('.camera_status').attr('class', 'camera_status available');
	}
}

/**
 * Method used to toggle if the camera is in use.
 * @param camid		The id of the camera to toggle.
 * @param inuse		Boolean, true if in use, false otherwise.
 */
function toggleCamInuse(camid, inuse) {
	camera_area = $('#camera_area');
	camera = camera_area.find('#camera_' + camid);
	
	if (inuse === true) {
		camera.find('.camera_status').attr('class', 'camera_status unavailable');
	} else {
		camera.find('.camera_status').attr('class', 'camera_status available');
	}
}