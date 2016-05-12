
var joysticksize = 200; //size of joystick in pixels.
var cameras = {}; //Store all available camera's
var currentcamera; //ID of the camera that is selected.

/**
* Load the camera's from the backend server.
* Put them into the cameras object and set the streams in the carousel.
*/
function loadCameras() {
	var place, camera_area, camera_div, camera_title;
	$.get("http://localhost:3000/api/backend/getCameraInfo", function(data) {
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
	
	//determine which elements of the UI to show
	zoomslider = $('.zoomslider');
	iris = $('.iris');
	focus = $('.focus');
	if (cameras[id].zoom === undefined) {
		zoomslider.hide();
	} else {
		zoomslider.show();
		zoomslider.val(cameras[id].zoom);
	}
	if  (cameras[id].tilt === undefined) {
		$('.zone').hide();
	} else {
		$('.zone').show();
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
	$.get("http://localhost:3000/api/backend/preset?cameraId=" + cameraID, function(data) {
		obj = JSON.parse(data);
		presets = obj.presets;
		place = 1;
		for (var p in presets) {
			if ($('#preset_'+ place) !== undefined) {
				preset = JSON.parse(presets[p]);
				preset_div = $('#preset_' + place);
				preset_div.find('img').attr("src", "http://localhost:3000/api/backend" + preset.image);
				preset_div.attr("presetID", "1-1"); //TODO should be the real ID of the preset
				place++;
			}
		}
	});
}

/**
* Options of the displayed joystick.
*/ 
var joystickoptions = {
	zone: document.querySelector('.zone'),
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
	$.get("http://localhost:3000/api/backend/move?id="+ currentcamera + "&moveType=relative&pan=" + pan + "&tilt=" + tilt + "&panSpeed=0&tiltSpeed=0", function(data) {});
	console.log(pan + " - " + tilt); 	
}

/**
* Method to send the new input value of the zoom slider to the currently selected camera.
*/
function inputzoomslider(zoom) {
	$.get("http://localhost:3000/api/backend/zoom?id="+ currentcamera + "&zoomType=absolute&zoom=" + zoom , function(data) {console.log(data)});
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
	$.get("http://localhost:3000/api/backend/focus?id="+ currentcamera + "&autoFocusOn=false&position=" + focus , function(data) {});
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
	$.get("http://localhost:3000/api/backend/iris?id="+ currentcamera + "&autoIrisOn=false&position=" + iris , function(data) {});
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
	$.get("http://localhost:3000/api/backend/focus?id="+ currentcamera + "&autoFocusOn=" + on, function(data) {});
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
	$.get("http://localhost:3000/api/backend/iris?id="+ currentcamera + "&autoIrisOn=" + on , function(data) {});
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
function presetcall(t){
	var presetID = t.attr("presetid");
	if (presetID !== undefined) {
		var title = t.find('h5');
		if(title.hasClass("selected")) {
			title.removeClass("selected");
		} else {
			title.addClass("selected");
		}
		//todo call the right call to recall this preset on the camera.
		console.log(t.attr("presetid"));
	}
}