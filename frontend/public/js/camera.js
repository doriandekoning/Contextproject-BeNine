/**
* Camera object containing all information the client knows about a camera.
* @param newCam json object from backend to create a camera object.
*/
function Camera(newCam) {
	this.id = newCam.id;
	this.inuse = newCam.inuse;
	this.autoFocus = newCam.autofocus;
	this.autoIris = newCam.autoiris;
	this.zoom = newCam.zoom;
	this.move = newCam.move;
	this.streamlink = newCam.streamaddress;
	this.img = $('<img src="' + this.streamlink + '" data-src="holder.js/246x144?auto=yes&text=Camera ' + this.id + ' unavailable&bg=8b8b8b">');
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
		camera_img.attr('src', this.streamlink );
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
		camera_div.find('img').attr("src", this.streamlink);
		camera_div.find('#create_preset_name').val('');
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
			if (this.autoIris) {
				iris.find("#auto_iris").bootstrapToggle('on');
			} else {
				iris.find("#auto_iris").bootstrapToggle('off');
			}
		}
		if (this.autoFocus === undefined) {
			focus.hide();
		} else {
			focus.show();
			if (this.autoFocus) {
				focus.find("#auto_focus").bootstrapToggle('on');
			} else {
				focus.find("#auto_focus").bootstrapToggle('off');
			}
		}
	}
};

