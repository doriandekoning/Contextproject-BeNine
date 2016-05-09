
var joysticksize = 200; //size of joystick in pixels.
var cameras = {}; //Store all available camera's
var currentcamera; //ID of the camera that is selected.

function loadCameras() {
	$.get("http://localhost:3000/api/backend/getCameraInfo", function(data) {
		var obj = JSON.parse(data);
		for(var c in obj.cameras){
			cameras[JSON.parse(obj.cameras[c]).id] = JSON.parse(obj.cameras[c]);
		}
	}).done(function() { 
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
		setCurrentCamera(0);
	});
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
var availablemove = 1
var pan, tilt;

function resetMove(){
	availablemove = 1;
	$.get("http://localhost:3000/api/backend/move?id="+ currentcamera + "&moveType=relative&pan=" + pan + "&tilt=" + tilt + "&panSpeed=0&tiltSpeed=0", function(data) {
			
	});
}

joystick.on('move', function(evt, data){
	tilt = Math.round((Math.sin(data.angle.radian) * (data.distance / (0.5 * joysticksize)) * 50 ) + 50);
	pan = Math.round((Math.cos(data.angle.radian) * (data.distance / (0.5 * joysticksize)) * 50 ) + 50);
	if(availablemove === 1){
		availablemove = 0;
		setTimeout(resetMove, 600)
		$.get("http://localhost:3000/api/backend/move?id="+ currentcamera + "&moveType=relative&pan=" + pan + "&tilt=" + tilt + "&panSpeed=0&tiltSpeed=0", function(data) {
			
		});
		console.log(pan + " - " + tilt); 
	}
});

joystick.on('end', function(evt, data){
	console.log(50 + " - " + 50); //Joystick released
	pan = 50;
	tilt = 50;
	$.get("http://localhost:3000/api/backend/move?id="+ currentcamera + "&moveType=relative&pan=" + 50 + "&tilt=" + 50 + "&panSpeed=0&tiltSpeed=0", function(data) {
		
	});
});

var availablezoom = 1;
var zoompos = 50;

function resetZoom() {
	availablezoom = 1;
	$.get("http://localhost:3000/api/backend/zoom?id="+ currentcamera + "&zoomType=relative&zoom=" + zoompos , function(data) {
			
	});
}

function inputzoomslider(zoom) {
	if(availablezoom === 1){
		availablezoom = 0;
		setTimeout(resetZoom, 500);
		$.get("http://localhost:3000/api/backend/zoom?id="+ currentcamera + "&zoomType=relative&zoom=" + zoom , function(data) {
			
		});
		
	}
	zoompos = zoom;
	console.log("Zoom: " + zoom);
}

function inputfocusslider(focus) {
	console.log("Focus: " + focus);
}

function inputirisslider(iris) {
	console.log("Iris: "+ iris);
}

