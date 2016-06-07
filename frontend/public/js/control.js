/**
* This class contains everything concerning the camera control buttons.
*/

//size of joystick in pixels.
var joysticksize = 150;

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

/**
*	Method to auto create presets.
*/
function autoCreatePresets() {
	$.get("/api/backend/presets/autocreatepresets?camera=" + currentcamera);
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
