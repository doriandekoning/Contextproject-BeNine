function Camera(id, inuse, autoFocus, autoIris, zoom, move) {
	this.id = id;
	this.inuse = inuse;
	this.autoFocus = autoFocus;
	this.autoIris = autoIris;
	this.zoom = zoom;
	this.move = move;
	this.img = $('<img src="/api/backend/camera/' + this.id + '/mjpeg" data-src="holder.js/246x144?auto=yes&text=Camera ' + this.id + ' unavailable&bg=8b8b8b">');
}
Camera.prototype = {
	
	smallView: function() {
		var camera_div, camera_title;
		camera_div = $('#camera_area #camera_' + this.id);
		camera_div.attr("camera_number", this.id);
		camera_div.find('.camera_status').attr('class', 'camera_status available');
		camera_img = camera_div.find('img');
		camera_img.attr('src', '/api/backend/camera/' + this.id + '/mjpeg');
		camera_img.load(function() {
			if ($(this).attr("alt") !== undefined) {
				camera_div.find('.camera_status').attr('class', 'camera_status unavailable');
			} else {
				camera_div.attr("onclick", 'switchCurrentView($(this).attr("camera_number"))');	
			}
		});
		camera_title = camera_div.find('.camera_title');
		camera_title.find('#camera_title').text(this.id);
	},
	createView: function() {
		camera_div = $('#preset_create_div');
		camera_div.find('img').attr("src", '/api/backend/camera/' + this.id + '/mjpeg');
	},
	
	bigView: function() {
		camera_div = $('#current_camera');
		camera_div.find('img').replaceWith(this.img);
		camera_title = camera_div.find('.camera_title');
		camera_title.find('#camera_title').text(this.id);
	},
	displayControls: function () {
		zoom = $('#zoom');
		iris = $('#iris');
		focus = $('#focus');
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
			setButton(iris.find("#auto_iris"), this.autoiris);
		}
		if  (this.autoFocus === undefined) {
			focus.hide();
		} else {
			focus.show();
			setButton(focus.find("#auto_focus"), this.autofocus);
		}
	}
}

