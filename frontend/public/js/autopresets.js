$( ".auto-presets-modal").on("shown.bs.modal", function(e) {
  var canvas = document.getElementById('previewCanvas')
  var context = canvas.getContext('2d');
  var imageObj = new Image();
  imageObj.onload = function () {
    context.drawImage(imageObj, 0, 0);
    context.strokeStyle = "#FF0000";
    context.strokeRect(10, 10, 80, 50);
    context.strokeRect(100, 10, 80, 50);
    context.strokeRect(190, 10, 80, 50);
    context.strokeRect(10, 70, 80, 50);
    context.strokeRect(100, 70, 80, 50);
    context.strokeRect(190, 70, 80, 50);
  };

  imageObj.src = '/api/backend/camera/1/mjpeg?width='+canvas.width + '&height=' + canvas.height;
})
