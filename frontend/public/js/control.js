
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


joystick.on('move', function(evt, data){
	sendMove(data.distance, data.angle.radian);
});


joystick.on('end', function(){
	sendMove(0, 0);
});

function sendMove(distance, angle){
	var tilt = Math.round((Math.sin(angle) * (distance / (0.5 * joysticksize)) * 50 ) + 50);
	var pan = Math.round((Math.cos(angle) * (distance / (0.5 * joysticksize)) * 50 ) + 50);
	$.get("http://localhost:3000/api/backend/move?id="+ currentcamera + "&moveType=relative&pan=" + pan + "&tilt=" + tilt + "&panSpeed=0&tiltSpeed=0", function(data) {});
	console.log(pan + " - " + tilt); 	
}


function inputzoomslider(zoom) {
	$.get("http://localhost:3000/api/backend/zoom?id="+ currentcamera + "&zoomType=absolute&zoom=" + zoom , function(data) {});
	console.log("Zoom: " + zoom);
}

function inputfocusslider(focus) {
	$('#auto_focus').addClass("btn-danger");
	$('#auto_focus').removeClass("btn-success");
	$.get("http://localhost:3000/api/backend/focus?id="+ currentcamera + "&autoFocusOn=false&position=" + focus , function(data) {});
	console.log("Focus: " + focus);
}

function inputirisslider(iris) {
	$('#auto_iris').addClass("btn-danger");
	$('#auto_iris').removeClass("btn-success");
	$.get("http://localhost:3000/api/backend/iris?id="+ currentcamera + "&autoIrisOn=false&position=" + iris , function(data) {});
	console.log("Iris: "+ iris);
};

$('#auto_focus').click(function() {	
	toggleButton($(this));
	var on = true;
	if($(this).hasClass("btn-danger")){
		on = false;
	}
	$.get("http://localhost:3000/api/backend/focus?id="+ currentcamera + "&autoFocusOn=" + on, function(data) {});
});

$('#auto_iris').click(function() {	
	toggleButton($(this));
	var on = true;
	if($(this).hasClass("btn-danger")){
		on = false;
	}
	$.get("http://localhost:3000/api/backend/iris?id="+ currentcamera + "&autoIrisOn=" + on , function(data) {});
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