var columns = 3;
var levels = 3;
var rows = 3;

$( ".auto-presets-modal").on("shown.bs.modal", function(e) {
  showSubViews();
})


function autoCreatePresets() {
  var name = $('#auto_preset_name').val();
	var presetTag = $('#auto_preset_tags').val();
  if (currentcamera !== undefined) {
    $.get("/api/backend/presets/autocreatepresets?camera="+currentcamera+"&rows="+rows+"&levels="+levels+"&columns="+columns+"&name="+name + "&tags="+presetTag, function(data) {

    });
  }

}

function updateColumnSlider(newColumns) {
  columns = newColumns;
  showSubViews();
}

function updateRowSlider(newRows) {
  rows = newRows;
  showSubViews();
}

function updateLevelSlider(newLevels) {
  levels = newLevels;
  showSubViews();
}


function showSubViews() {
  var offset = 1;
  var canvas = document.getElementById('previewCanvas')
  var context = canvas.getContext('2d');
  var imageObj = new Image();
  imageObj.onload = function () {
    context.drawImage(imageObj, offset, offset);
    context.strokeStyle = "#FF0000";
    context.lineWidth=0.5;
    $.get("/api/backend/presets/autocreatesubviews?rows="+rows+"&levels="+levels+"&columns="+columns, function(data) {
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
  imageObj.src = '/api/backend/camera/'+currentcamera+'/mjpeg?width='+ imageWidth + '&height=' + imageHeight;
}
