/**
* Camera object containing all information the client knows about a camera.
*/
function Camera(id, inuse, autoFocus, autoIris, zoom, move) {
	this.id = id;
	this.inuse = inuse;
	this.autoFocus = autoFocus;
	this.autoIris = autoIris;
	this.zoom = zoom;
	this.move = move;
	this.img = $('<img src="/api/backend/camera/' + this.id + '/mjpeg?width=640&height=360" data-src="holder.js/246x144?auto=yes&text=Camera ' + this.id + ' unavailable&bg=8b8b8b">');
}
Camera.prototype = {
	
	/**
	* Show this camera in the small display on the screen.
	*/
	smallView: function() {
		var camera_area = $('#camera_area');
		var camera_div = camera_area.find('#camera_' + this.id);
		camera_div.attr("camera_number", this.id);
		camera_div.find('.camera_status').attr('class', 'camera_status available');
		camera_img = camera_div.find('img');
		camera_img.attr('src', '/api/backend/camera/' + this.id + '/mjpeg?width=320&height=180');
		camera_img.load(function() {
			if ($(this).attr("alt") === undefined) {
				camera_div.attr("onclick", 'switchCurrentView($(this).attr("camera_number"))');
			} else {
				camera_div.find('.camera_status').attr('class', 'camera_status unavailable');
			}
		});
		var camera_title = camera_div.find('.camera_title');
		camera_title.find('#camera_title').text(this.id);
	},
	
	/**
	* Display the camera view in the create window.
	*/
	createView: function() {
		camera_div = $('#preset_create_div');
		camera_div.find('img').attr("src", '/api/backend/camera/' + this.id + '/mjpeg?width=320&height=180');
	},
	
	/**
	* Display the camera view in the current view window.
	*/
	bigView: function() {
		camera_div = $('#current_camera');
		camera_div.find('img').replaceWith(this.img);
		camera_title = camera_div.find('.camera_title');
		camera_title.find('#camera_title').text(this.id);
	},
	
	/**
	* Show the controls of this camera.
	*/
	displayControls: function () {
		var zoom = $('#zoom');
		var iris = $('#iris');
		var focus = $('#focus');
		if (this.zoom === undefined) {
			zoom.hide();
		} else {
			zoom.show();
		}
		if  (this.move === undefined) {
			$('.joystick_zone').hide();
		} else {
			$('.joystick_zone').show();
		}
		if  (this.autoIris === undefined) {
			iris.hide();
		} else {
			iris.show();
			setButton(iris.find("#auto_iris"), this.autoIris);
		}
		if (this.autoFocus === undefined) {
			focus.hide();
		} else {
			focus.show();
			setButton(focus.find("#auto_focus"), this.autofocus);
		}
	}
};

