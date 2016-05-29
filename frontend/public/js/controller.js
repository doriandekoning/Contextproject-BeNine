var cameras = [];
var presets = [];
var currentcamera;

// Document is ready, we can now manipulate it.
$(document).ready(function() {

    // Update server status every 10 seconds.
    // setInterval(setServerStatus, 10 * 1000);
	
	// Load the available cameras.
	cameras = loadCameras();
	
	// Generate the camera block area.
    generateCameraArea(cameras.map(function(item){
		return item.id;
	}));
		
	cameras.forEach(function(item) {
		item.smallView();
	});
	
	// Load the available presets from the backend.
	presets = loadPresets();
	displayPresets(presets);

    // Generate the presets area.
    generatePresets();

    console.log('Page has loaded successfully.');
});

function loadCameras() {
	var cameras = [];
	cameras.push(new Camera(1, true, undefined, undefined, undefined, undefined));
	cameras.push(new Camera(2, true, true, true, true, true));
	cameras.push(new Camera(3, true, true, true, true, true));
	cameras.push(new Camera(4, true, true, true, true, true));
	cameras.push(new Camera(5, true, true, true, true, true));
	return cameras;
}

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

function findCameraOnID(id){
	var res = $.grep(cameras, function(item, n) {
		return parseInt(item.id) === parseInt(id);
	});
	return res[0];
}


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