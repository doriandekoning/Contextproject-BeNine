$( ".auto-presets-modal").on("shown.bs.modal", function(e) {
  var canvasContext = document.getElementById('previewCanvas').getContext('2d');
  var imageObj = new Image();
  imageObj.onload = function () {
    canvasContext.drawImage(imageObj, -40, -50);
  };
  imageObj.src = '/api/backend/camera/1/mjpeg?width=320&height=180';
})
