var joysticksize = 150; //size of joystick in pixels.
var cameras = {}; //Store all available camera's
var currentcamera; //ID of the camera that is selected.
var selectedPreset; //Preset that is currently selected.

/**
* Load the camera's from the backend server.
* Put them into the cameras object and set the streams in the carousel.
*/
function loadCameras() {
	var place, camera_area, camera_div, camera_title;
	$.get("/api/backend/camera/", function(data) {
		var obj = JSON.parse(data);
		// put the information of every camera in cameras.
		for (var c in obj.cameras) {
      if ( c !== undefined) {
		cameras[JSON.parse(obj.cameras[c]).id] = JSON.parse(obj.cameras[c]);
      }
		}
	}).done(function() {

		place  = 1;
		camera_area = $("#camera_area");

		// show stream of every camera in the carousel.
		for (var c in cameras) {
			camera_div = camera_area.find('#camera_' + place);
			camera_div.attr("camera_number", cameras[c].id);
			camera_div.find('img').attr("src", "/api/backend/camera/" + cameras[c].id + "/mjpeg");
			camera_title = camera_div.find('.camera_title');
			camera_title.find('#camera_title').text(cameras[c].id);
			camera_div.find('.camera_status').attr('class', 'camera_status available');
			place++;
		}
	});
}

/**
* Reload the camera info and update the controls.
*/
function getCameraInfo() {
	$.get("/api/backend/camera/", function(data) {
		var obj = JSON.parse(data);
		// put the information of every camera in cameras.
		for (var c in obj.cameras) {
      if ( c !== undefined) {
		cameras[JSON.parse(obj.cameras[c]).id] = JSON.parse(obj.cameras[c]);
      }
		}
	}).done(loadControls);
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


/**
* Method to change the currently selected camera.
* It changes the visible controls and displays the camera stream in the editing view.
*/
function setCurrentCamera(id) {
	if (id !== currentcamera) {
		var camera_div, camera_title, zoomslider, iris, focus;
		toggleCamSelected(currentcamera, false);
		currentcamera = id;

		toggleCamSelected(currentcamera, true);
		// Show the current camera in the editing view.
		camera_div = $('#current_camera');
		camera_div.find('img').attr("src", "/api/backend/camera/" + currentcamera + "/mjpeg");
		camera_title = camera_div.find('.camera_title');
		camera_title.find('#camera_title').text(cameras[currentcamera].id);
		selectedPreset = undefined;
		$('#createPreset').prop('disabled', false);
		$('#preset_create_div .tags_input').tagsinput('removeAll');

		getCameraInfo();
		loadPresets(currentcamera);
	}
}

/**
* Load all the right controls
*/
function loadControls() {
	//determine which elements of the UI to show
	zoom = $('#zoom');
	iris = $('#iris');
	focus = $('#focus');
	if (cameras[currentcamera].zoom === undefined) {
		zoom.hide();
	} else {
		zoom.show();
	}
	if  (cameras[currentcamera].tilt === undefined) {
		$('.joystick_zone').hide();
	} else {
		$('.joystick_zone').show();
	}
	if  (cameras[currentcamera].iris === undefined) {
		iris.hide();
	} else {
		iris.show();
		setButton(iris.find("#auto_iris"), cameras[currentcamera].autoiris);
	}
	if  (cameras[currentcamera].focus === undefined) {
		focus.hide();
	} else {
		focus.show();
		setButton(focus.find("#auto_focus"), cameras[currentcamera].autofocus);
	}
}

/**
* Set the button specified to the boolean value bool.
*/
function setButton(btn, bool) {
	if (bool === true) {
		btn.removeClass( "btn-danger" ).addClass("btn-success");
	} else {
		btn.removeClass( "btn-success" ).addClass("btn-danger");
	}
}

/**
* Options of the displayed joystick.
*/
var joystickoptions = {
	zone: document.querySelector('.joystick_zone'),
    mode: 'static',
	position: {
        left: '10%',
        top: '10%'
    },
    color: 'black',
	size: joysticksize
};

/* variables used for the joystick movements */
var joystick = nipplejs.create(joystickoptions);
var distance = 0;
var angle = 0;
var moveSend = false;
var lastSend = {distance: 0, angle:0};

/**
* When the joystick is moved send a new move command.
*/
joystick.on('move', function(evt, data){
	angle = data.angle.radian;
	distance = data.distance;
	if (moveSend === false) {
		moveSend = true;
		setTimeout(function(){ sendMove(); moveSend = false;  }, 130);
		sendMove();
	} 
});

/**
* When the joystick is released send a move to the current camera.
*/
joystick.on('end', function(){
	distance = 0;
	sendMove();
});

/**
* Method to send a move to the current camera.
*/
function sendMove(){
	if (lastSend.distance !== distance || lastSend.angle !== angle) {
		var tilt, pan;
		var settings = getSettings();
		tilt = Math.round((Math.sin(angle) * (distance / (0.5 * joysticksize)) * settings.joystick * 5 ) + 50);
		pan = Math.round((Math.cos(angle) * (distance / (0.5 * joysticksize)) * settings.joystick * 5 ) + 50);
		$.get("/api/backend/camera/" + currentcamera + "/move?moveType=relative&pan=" + pan + "&tilt=" + tilt + "&panSpeed=0&tiltSpeed=0", function(data) {});
		lastSend.distance = distance;
		lastSend.angle = angle;
		console.log(pan + " - " + tilt);
	}
}

/* Variable used for the zoom slider */
var zoomInput = {value:0, send:false, lastSend: 0};

/**
* Method is called when the inputslider value changes.
*/
function inputzoomslider(z) {
	inputRecieved(sendZoom, zoomInput, z);
}

/**
* Method to send a command to the backend to change the zoom.
* values send to the backend are between 1 and 99
*/
function sendZoom() {
	if (zoomInput.value !== zoomInput.lastSend) {
		var settings = getSettings();
		var zoom = parseInt(50  + (settings.zoom - 0.1) * parseInt(zoomInput.value));
		$.get("/api/backend/camera/" + currentcamera + "/zoom?zoomType=relative&zoom=" + zoom, function(data) {});
		zoomInput.lastSend = zoomInput.value;
		console.log("Zoom: " + zoom);
	}
}

/* Variable used for the focus slider */
var focusInput = {value:0, send:false, lastSend: 0};

/**
* Method to send the new input value of the focus slider to the currently selected camera.
* It also change the status of the auto focus.
* @param focus value of the new input.
*/
function inputfocusslider(f) {
	$('#auto_focus').addClass("btn-danger");
	$('#auto_focus').removeClass("btn-success");
	inputRecieved(sendFocus, focusInput, f);
}

/**
* Method to send a command to the backend to change the focus.
* values send to the backend are between 1 and 99
*/
function sendFocus() {
	if (focusInput.value != focusInput.lastSend) {
		var settings = getSettings();
		var focus = parseInt(50  + (settings.focus - 0.1) * parseInt(focusInput.value));
		$.get("/api/backend/camera/" + currentcamera + "/focus?autoFocusOn=false&speed=" + focus, function(data) {});
		focusInput.lastSend = focusInput.value;
		console.log("Focus: " + focus);
	}
}

/* Variables used for the iris slider */
var irisInput = {value:0, send:false, lastSend: 0};


/**
* Method to send the new input value of the iris slider to the currently selected camera.
* It also changes the status of the auto iris.
* @param iris value of the new input.
*/
function inputirisslider(i) {
	$('#auto_iris').addClass("btn-danger");
	$('#auto_iris').removeClass("btn-success");
	inputRecieved(sendIris, irisInput, i);
}

/**
* Function to send a command to the backend to change the iris.
* values send to the backend are between 1 and 99
*/
function sendIris() {
	if (irisInput.value !== irisInput.lastSend) {
		var settings = getSettings();
		var iris = parseInt(50 + (settings.iris - 0.1) * parseInt(irisInput.value));
		$.get("/api/backend/camera/"+ currentcamera + "/iris?autoIrisOn=false&speed=" + iris, function(data) {});
		irisInput.lastSend = irisInput.value;
		console.log("Iris: " + iris);
	}
}

/**
* function to process the input of a slider.
*/
function inputRecieved(fun, input, newvalue) {
	if (input.send === false) {
		input.value = newvalue;
		input.send = true;
		setTimeout(function(){fun(); input.send = false;}, 130);
		fun();
	} else {
		input.value = (parseInt(newvalue) + parseInt(input.value)) / 2;
	}
}

function releaseSlider(fun, input) {
	input.value = 0;
	fun();
}

/**
* On click of the auto focus button change the color of the button.
* And send the http request to change to auto focus.
*/
$('#auto_focus').click(function() {
	var on = false;
	if($(this).hasClass("btn-danger")){
		on = true;
	}
	setButton($(this), on);
	$.get("/api/backend/camera/"+ currentcamera + "/focus?autoFocusOn=" + on, function(data) {});
});

/**
* On click of the auto iris button change the color of the button.
* And send the http request to change to auto iris.
*/
$('#auto_iris').click(function() {
	var on = false;
	if($(this).hasClass("btn-danger")){
		on = true;
	}
	setButton($(this), on);
	$.get("/api/backend/camera/"+ currentcamera + "/iris?autoIrisOn=" + on , function(data) {});
});

/**
* Toggle the color of the button between green and red.
* @param btn the button to change.
*/
function toggleButton(btn){
	if(btn.attr("class") === "btn btn-success") {
		btn.addClass("btn-danger");
		btn.removeClass("btn-success");
	} else {
		btn.addClass("btn-success");
		btn.removeClass("btn-danger");
	}
}
