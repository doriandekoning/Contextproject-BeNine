function Camera(id, inuse, autoFocus, autoIris) {
	this.id = id;
	this.inuse = inuse;
	this.autoFocus = autoFocus;
	this.autoIris = autoIris;
	this.presets = [];
	
	this.smallView = function() {
		var camera_div, camera_title;
		camera_div = emptySmallView();
		camera_div.attr("camera_number", this.id);
		camera_div.find('.camera_status').attr('class', 'camera_status available');
		
		camera_div.find('img').attr("src", "/api/backend/camera/" + this.id + "/mjpeg");
		camera_title = camera_div.find('.camera_title');
		camera_title.find('#camera_title').text(this.id);

		camera_div.click(switchCurrentView(bigView()));		
		return camera_div;
	}
	
	this.bigView = function() {
		
	}
	
	this.controlsDislay = function () {
		
	}
	
	this.presetDisplay = function () {
		
	}
}
function switchCurrentView(view_div) {
    camera_div = $('#current_camera');
	camera_div = bigView();
}

function emptySmallView() {
	var camera_icon, camera_title, camera_image, camera_title_text, camera_element;
	camera_element = $('<div class="col-xs-6"></div>');

    camera_title = $('<div class="camera_title"></div>');
    camera_icon = $('<span class="glyphicon glyphicon-facetime-video" aria-hidden="true"></span>');
    camera_title_text = $('<span id="camera_title">-</span>');
    camera_title.append(camera_icon, camera_title_text);
	
    camera_image = $('<img data-src="holder.js/246x144?auto=yes&text=Camera -&bg=8b8b8b" >').get(0);

    camera_element.append(camera_image, camera_title, camera_status);
	return camera_element;
}