
 var joysticksize = 200; //size of joystick in pixels.
 
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
	var tilt = (Math.sin(data.angle.radian) * (data.distance/(0.5 * joysticksize)) * 50 ) + 50;
	var pan = (Math.cos(data.angle.radian) * (data.distance/(0.5 * joysticksize)) * 50 ) + 50;
	console.log(tilt + " - " + pan); 
});

joystick.on('end', function(evt, data){
	console.log(50 + " - " + 50); //Joystick released
});

