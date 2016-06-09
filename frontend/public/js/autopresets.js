$( ".auto-presets-modal").on("shown.bs.modal", function(e) {
  var canvas = document.getElementById('previewCanvas')
  var context = canvas.getContext('2d');
  var imageObj = new Image();
  imageObj.onload = function () {
    context.drawImage(imageObj, 0, 0);
    context.strokeStyle = "#FF0000";
    $.get("/api/backend/presets/autocreatesubviews?rows=2&levels=3&columns=2", function(data) {
       var subViews = JSON.parse(data);
       for ( var i = 0; i < subViews.SubViews.length; i++) {
         var height = (canvas.height/100) * (subViews.SubViews[i].topLeft.y  - subViews.SubViews[i].bottomRight.y);
         var width = (canvas.width/100) * (subViews.SubViews[i].bottomRight.x  - subViews.SubViews[i].topLeft.x);
         var x = 1 + ((canvas.width/100) * (subViews.SubViews[i].topLeft.x));
         var y = 1 + ((canvas.height/100) *  (100 -subViews.SubViews[i].topLeft.y));
         console.log(canvas.width);
         console.log("x:"+ x + ",y: " + y + ", width:" + width + ",height:"+ height);
         context.strokeRect(x, y, width, height)
       }
     });
  };

  imageObj.src = '/api/backend/camera/1/mjpeg?width='+canvas.width + '&height=' + canvas.height;
})
