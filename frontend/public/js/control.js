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
			camera_div.find('img').attr("src", cameras[c].streamlink);
			camera_title = camera_div.find('.camera_title');
			camera_title.find('#camera_title').text(cameras[c].id);
			place++;
		}
	});
}

/**
* Method to change the currently selected camera.
* It changes the visible controls and displays the camera stream in the editing view.
*/
function setCurrentCamera(id) {
	var camera_div, camera_title, zoomslider, iris, focus;
	currentcamera = id;
	// Show the current camera in the editing view.
	camera_div = $('#current_camera');
	camera_div.find('img').attr("src", cameras[currentcamera].streamlink);
	camera_title = camera_div.find('.camera_title');
	camera_title.find('#camera_title').text(cameras[currentcamera].id);	
	selectedPreset = undefined;
	
	//determine which elements of the UI to show
	zoom = $('#zoom');
	iris = $('#iris');
	focus = $('#focus');
	if (cameras[id].zoom === undefined) {
		zoom.hide();
	} else {
		zoom.show();
		zoom.val(cameras[id].zoom);
	}
	if  (cameras[id].tilt === undefined) {
		$('.joystick_zone').hide();
	} else {
		$('.joystick_zone').show();
	}
	if  (cameras[id].iris === undefined) {
		iris.hide();
	} else {
		iris.show();
		$('.irisslider').val(cameras[id].iris);
	}
	if  (cameras[id].focus === undefined) {
		focus.hide();
	} else {
		focus.show();
		$('.focusslider').val(cameras[id].focus);
	}
	
	loadPresets(currentcamera);
}

/**
* Function loads the presets of this camera in the preset window.
* @param cameraID the presets of this camera are loaded.
*/
function loadPresets(cameraID) {
	var preset_div, obj, presets, place, preset, preset_area;
	preset_area = $('#preset_area');
	preset_area.find('div').removeAttr("presetID");
	preset_area.find('img').removeAttr("src");
	preset_area.find('h5').removeClass();
	Holder.run({images:"#preset_area img"})
	$.get("/api/backend/camera/" + cameraID + "/preset", function(data) {
		obj = JSON.parse(data);
		console.log(obj);
		presets = obj.presets;
		place = 1;
		for (var p in presets) {
			if ($('#preset_'+ place) !== undefined) {
				preset = JSON.parse(presets[p]);
				preset_div = $('#preset_' + place);
				preset_div.find('img').attr("src", "/api/backend" + preset.image);
				preset_div.attr("presetID", preset.id);
				place++;
			}
		}
	});
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

var joystick = nipplejs.create(joystickoptions);

/**
* When the joystick is moved send a new move command.
*/
joystick.on('move', function(evt, data){
	sendMove(data.distance, data.angle.radian);
});

/**
* When the joystick is released send a move to the current camera.
*/
joystick.on('end', function(){
	sendMove(0, 0);
});

/**
* Method to send a move to the current camera.
* @param distance to determine the speed of the movement.
* @param angle the direction in which to move.
*/
function sendMove(distance, angle){
	var tilt, pan;
	tilt = Math.round((Math.sin(angle) * (distance / (0.5 * joysticksize)) * 50 ) + 50);
	pan = Math.round((Math.cos(angle) * (distance / (0.5 * joysticksize)) * 50 ) + 50);
	$.get("/api/backend/camera/" + currentcamera + "/move?moveType=relative&pan=" + pan + "&tilt=" + tilt + "&panSpeed=0&tiltSpeed=0", function(data) {});
	console.log(pan + " - " + tilt);
}

/**
* Method to send the new input value of the zoom slider to the currently selected camera.
*/
function inputzoomslider(zoom) {
	$.get("/api/backend/camera/" + currentcamera + "/zoom?zoomType=absolute&zoom=" + zoom , function(data) {});
	console.log("Zoom: " + zoom);
}

/**
* Method to send the new input value of the focus slider to the currently selected camera.
* It also change the status of the auto focus.
* @param focus value of the new input.
*/
function inputfocusslider(focus) {
	$('#auto_focus').addClass("btn-danger");
	$('#auto_focus').removeClass("btn-success");
	$.get("/api/backend/camera/" + currentcamera + "/focus?autoFocusOn=false&position=" + focus , function(data) {});
	console.log("Focus: " + focus);
}

/**
* Method to send the new input value of the iris slider to the currently selected camera.
* It also changes the status of the auto iris.
* @param iris value of the new input.
*/
function inputirisslider(iris) {
	$('#auto_iris').addClass("btn-danger");
	$('#auto_iris').removeClass("btn-success");
	$.get("/api/backend/camera/"+ currentcamera + "/iris?autoIrisOn=false&position=" + iris , function(data) {});
	console.log("Iris: "+ iris);
}

/**
* On click of the auto focus button change the color of the button.
* And send the http request to change to auto focus.
*/
$('#auto_focus').click(function() {
	toggleButton($(this));
	var on = true;
	if($(this).hasClass("btn-danger")){
		on = false;
	}
	$.get("/api/backend/camera/"+ currentcamera + "/focus?autoFocusOn=" + on, function(data) {});
});

/**
* On click of the auto iris button change the color of the button.
* And send the http request to change to auto iris.
*/
$('#auto_iris').click(function() {
	toggleButton($(this));
	var on = true;
	if($(this).hasClass("btn-danger")){
		on = false;
	}
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

/**
* Function to handle a click on a preset.
* @param t is the div on which is clicked.
*/
function presetcall(t) {
	var presetID = t.attr("presetid");
	if (presetID !== undefined) {
		var title = t.find('h5');
		if(selectedPreset != undefined){
			$('#' + selectedPreset).find('h5').removeClass("selected");
		}
		selectedPreset = t.attr("id");
		title.addClass("selected");
		$.get("/api/backend/camera/"+ currentcamera + "/recallPreset?presetid=" + t.attr("presetid") , function(data) {console.log(data);});
		console.log(t.attr("presetid"));
	}
}

/**
* Load everyting to create a preset.
*/
function loadCreatePreset() {
	if (currentcamera !== undefined) {
		$('.preset-create-modal').find('img').attr("src", cameras[currentcamera].streamlink);
	}
}

/**
* Create a preset of the current camera view.
*/
function createPreset() {
	if (currentcamera !== undefined) {
		$.get("/api/backend/camera/"+ currentcamera + "/createpreset" , function(data) {console.log(data);});
	}
}