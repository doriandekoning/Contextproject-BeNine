$( ".auto-presets-modal").on("shown.bs.modal", function(e) {
  var offset = 1;
  var canvas = document.getElementById('previewCanvas')
  var context = canvas.getContext('2d');
  var imageObj = new Image();
  imageObj.onload = function () {
    context.drawImage(imageObj, offset, offset);
    context.strokeStyle = "#FF0000";
    context.lineWidth=0.5;
    $.get("/api/backend/presets/autocreatesubviews?rows=2&levels=3&columns=2", function(data) {
       var subViews = JSON.parse(data);
       for ( var i = 0; i < subViews.SubViews.length; i++) {
         var height = (canvas.height/100) * (subViews.SubViews[i].topLeft.y  - subViews.SubViews[i].bottomRight.y);
         var width = (canvas.width/100) * (subViews.SubViews[i].bottomRight.x  - subViews.SubViews[i].topLeft.x);
         var x = ((canvas.width/100) * (subViews.SubViews[i].topLeft.x));
         var y = ((canvas.height/100) *  (100 -subViews.SubViews[i].topLeft.y));
         context.strokeRect(x, y, width, height)
       }
     });
  };
  var imageWidth = canvas.width - (offset * 2);
  var imageHeight = canvas.height - (offset * 2);
  imageObj.src = '/api/backend/camera/1/mjpeg?width='+ imageWidth + '&height=' + imageHeight;
})


function autoCreatePresets() {
  var name = $('#auto_preset_name').val();
  if (currentcamera !== undefined) {
    $.get("/api/backend/presets/autocreatepresets?camera="+currentcamera+"&rows=2&levels=3&columns=2&name="+name, function(data) {

    });
  }

}
