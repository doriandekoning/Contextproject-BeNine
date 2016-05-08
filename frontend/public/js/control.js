
var joysticksize = 200; //size of joystick in pixels.
var cameras = {}; //Store all available camera's
var currentcamera; //ID of the camera that is selected.

function loadCameras() {
	/////Temporary adding 15 camera's for testing
	cameras[55] = {
			id: 55,
			streamlink: "http://83.128.144.84:88/cgi-bin/CGIStream.cgi?cmd=GetMJStream&usr=user&pwd=geheim"
	};
	var cameraDak = {
			id: 2,
			streamlink: "http://131.180.123.51/cgi-bin/nph-zms?mode=jpeg&monitor=4&scale=100&maxfps=6&buffer=100"
	};
	var cameraTuin = {
			id: 3,
			streamlink: "http://tuincam.bt.tudelft.nl/mjpg/video.mjpg"
	};
	cameras[2] = cameraDak;
	cameras[3] = cameraTuin;
	
	for (var i = 4; i < 8; i++) {
		cameras[i] = {
			id: i,
			streamlink: "http://131.180.123.51/cgi-bin/nph-zms?mode=jpeg&monitor=5&scale=100&maxfps=6&buffer=100"
		};
	}
	for (var i = 8; i < 12; i++) {
		cameras[i] = {
			id: i,
			streamlink: "http://131.180.123.51/cgi-bin/nph-zms?mode=jpeg&monitor=4&scale=100&maxfps=6&buffer=100"
		};
	}
	for (var i = 12; i < 16; i++) {
		cameras[i] = {
			id: i,
			streamlink: "http://tuincam.bt.tudelft.nl/mjpg/video.mjpg"
		};
	}
	////
	var place  = 1;
	var camera_area = $("#camera_area");
	for (var c in cameras) {
		camera_div = camera_area.find('#camera_' + place);
		camera_div.attr("camera_number", cameras[c].id);
		camera_div.find('img').attr("src", cameras[c].streamlink);
		camera_title = camera_div.find('.camera_title');

		camera_title.find('#camera_title').text(cameras[c].id);
		place++;
	}
}

function setCurrentCamera(id){
	currentcamera = id;
	$('#current_camera').find('img').attr("src", cameras[currentcamera].streamlink);
	camera_title = $('#current_camera').find('.camera_title');
	camera_title.find('#camera_title').text(cameras[currentcamera].id);
}
 
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

joystick.on('move', function(evt, data){
	var tilt = (Math.sin(data.angle.radian) * (data.distance / (0.5 * joysticksize)) * 50 ) + 50;
	var pan = (Math.cos(data.angle.radian) * (data.distance / (0.5 * joysticksize)) * 50 ) + 50;
	console.log(tilt + " - " + pan); 
});

joystick.on('end', function(evt, data){
	console.log(50 + " - " + 50); //Joystick released
});

function inputzoomslider(zoom) {
	console.log("zoom: " + zoom);
}

function inputfocusslider(focus) {
	console.log("Focus: " + focus);
}

function inputirisslider(iris) {
	console.log("Iris: "+ iris);
}

