
var joysticksize = 200; //size of joystick in pixels.
var cameras = {}; //Store all available camera's
var currentcamera; //ID of the camera that is selected.

function loadCameras() {
	$.get("http://localhost:3000/api/backend/getCameraInfo", function(data) {
		var obj = JSON.parse(data);
		// put the information of every camera in cameras.
		for(var c in obj.cameras){
			cameras[JSON.parse(obj.cameras[c]).id] = JSON.parse(obj.cameras[c]);
		}
	}).done(function() { 
		
		var place  = 1;
		var camera_area = $("#camera_area");
		
		// show stream of every camera in the carousel.
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
	// Show the current camera in the editing view.
	$('#current_camera').find('img').attr("src", cameras[currentcamera].streamlink);
	camera_title = $('#current_camera').find('.camera_title');
	camera_title.find('#camera_title').text(cameras[currentcamera].id);
	
	//determine which elements of the UI to show
	if (cameras[id].zoom == undefined) {
		$('.zoomslider').hide();
	} else {
		$('.zoomslider').show();
		$('.zoomslider').val(cameras[id].zoom);
	}
	if  (cameras[id].tilt == undefined) {
		$('.zone').hide();
	} else {
		$('.zone').show();
	}
	if  (cameras[id].iris == undefined) {
		$('.iris').hide();
	} else {
		$('.iris').show();
		$('.irisslider').val(cameras[id].iris);
	}
	if  (cameras[id].focus == undefined) {
		$('.focus').hide();
	} else {
		$('.focus').show();
		$('.focusslider').val(cameras[id].focus);
	}
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



var distance = 0;
var angle = 0;

joystick.on('move', function(evt, data){
	distance = data.distance;
	angle = data.angle.radian;
});

joystick.on('move', activateMove);

joystick.on('end', function(){
	distance = 0;
	angle = 0;
	sendMove();
});


function activateMove(evt, data){
	joystick.off('move', activateMove);
	sendMove();
	setTimeout(function(){ joystick.on('move', activateMove); sendMove();}, 500);
}

function sendMove(){
	var tilt = Math.round((Math.sin(angle) * (distance / (0.5 * joysticksize)) * 50 ) + 50);
	var pan = Math.round((Math.cos(angle) * (distance / (0.5 * joysticksize)) * 50 ) + 50);
	$.get("http://localhost:3000/api/backend/move?id="+ currentcamera + "&moveType=relative&pan=" + pan + "&tilt=" + tilt + "&panSpeed=0&tiltSpeed=0", function(data) {});
	console.log(pan + " - " + tilt); 	
}

var availablezoom = 1;
var zoompos = 50;

function resetZoom() {
	availablezoom = 1;
	$.get("http://localhost:3000/api/backend/zoom?id="+ currentcamera + "&zoomType=relative&zoom=" + zoompos , function(data) {});
}

function inputzoomslider(zoom) {
	if(availablezoom === 1){
		availablezoom = 0;
		setTimeout(resetZoom, 500);
		$.get("http://localhost:3000/api/backend/zoom?id="+ currentcamera + "&zoomType=relative&zoom=" + zoom , function(data) {});
	}
	zoompos = zoom;
	console.log("Zoom: " + zoom);
}

function inputfocusslider(focus) {
	console.log("Focus: " + focus);
}

function inputirisslider(iris) {
	console.log("Iris: "+ iris);
};

$('#auto_focus').click(function() {	
	toggleButton($(this));
});

$('#auto_iris').click(function() {	
	toggleButton($(this));
});

function toggleButton(btn){
	if(btn.attr("class") === "btn btn-success") {
		btn.addClass("btn-danger");
		btn.removeClass("btn-success");
	} else {
		btn.addClass("btn-success");
		btn.removeClass("btn-danger");
	}
}