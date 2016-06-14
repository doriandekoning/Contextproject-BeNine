var columns = 3;
var levels = 3;
var rows = 3;
var maxRows = 5;
var maxColumns = 5;
var maxLevels = 3;

$( ".auto-presets-modal").on("shown.bs.modal", function(e) {
  $("#auto-preset-creation-preview-image").attr('src', '/api/backend/camera/' + currentcamera+ '/mjpeg?height='+$("#auto-preset-creation-preview-image").height + '&width='+$("#auto-preset-creation-preview-image").width );
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

function updateColumnAmount(amount) {
  columns+=amount;
  columns = Math.min(columns, maxColumns);
  columns = Math.max(columns, 1);
  $("#columns-amount").val(columns);
  showSubViews();
}

function updateRowAmount(amount) {
  rows+=amount;
  rows = Math.min(rows, maxRows);
  rows = Math.max(rows, 1);
  $("#rows-amount").val(rows);
  showSubViews();
}

function updateLevelAmount(amount) {
  levels+=amount;
  levels = Math.min(levels, maxLevels);
  levels = Math.max(levels, 1);
  $("#levels-amount").val(levels);
  showSubViews();
}

function showSubViews() {
  var offset = 1;
  var canvas = document.getElementById('previewCanvas');
  var context = canvas.getContext('2d');
  clearCanvas(canvas);
//  var imageObj = new Image();
//  imageObj.onload = function () {
//    context.drawImage(imageObj, offset, offset);
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
//  };
  var imageWidth = canvas.width - (offset * 2);
  var imageHeight = canvas.height - (offset * 2);
//  imageObj.src = '/api/backend/camera/'+currentcamera+'/mjpeg?width='+ imageWidth + '&height=' + imageHeight;
}

function clearCanvas(canvas) {
  var context = canvas.getContext('2d');
  context.clearRect(0, 0, canvas.width, canvas.height);
}
