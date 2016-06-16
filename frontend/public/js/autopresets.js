var columns = 3;
var levels = 3;
var rows = 3;
var maxRows = 5;
var maxColumns = 5;
var maxLevels = 3;


/**
* Executed when the modal for auto preset creation loads. Adds the mjpeg stream to the image behind the canvas.
*/
$( ".auto-presets-modal").on("shown.bs.modal", function(e) {
  $("#columns-amount").val(columns);
  $("#rows-amount").val(rows);
  $("#levels-amount").val(levels);
  var image = $("#auto-preset-creation-preview-image");
  var streamURL = '/api/backend/camera/' + currentcamera+ '/mjpeg?height='+image.height() + '&width='+image.width();
  $("#auto-preset-creation-preview-image").attr('src', streamURL);
  showSubViews();
})

/**
* Executed when the auto create presets button is pressed.
* Sends a command to the server to start auto creating presets with the current amount of columns,rows and levels.
*/
function autoCreatePresets() {
  var name = $('#auto_preset_name').val();
	var presetTag = $('#auto_preset_tags').val();
  if (currentcamera !== undefined) {
    var done = false;
    $.get("/api/backend/presets/autocreatepresets?camera="+currentcamera+"&rows="+rows+"&levels="+levels+"&columns="+columns+"&name="+name + "&tags="+presetTag, function(data) {
      done = true;
    });
    // Update statusbar ever 2sec (2000ms)
    setInterval(updateProgressbar, 2*1000);

  }
}

/**
* Updates the progressbar
*/
function updateProgressbar() {
  $.get("/api/backend/presets/autocreatepresetsstatus?camera=" + currentcamera, function(data) {
    console.log(data);
  });
}

/**
* Increases the amount of columns with the provided value (negative values allowed for decreasing).
*/
function increaseColumnAmount(amount) {
  columns+=amount;
  columns = Math.min(columns, maxColumns);
  columns = Math.max(columns, 1);
  $("#columns-amount").val(columns);
  showSubViews();
}

/**
* Increases the amount of rows with the provided value (negative values allowed for decreasing).
*/
function increaseRowAmount(amount) {
  rows+=amount;
  rows = Math.min(rows, maxRows);
  rows = Math.max(rows, 1);
  $("#rows-amount").val(rows);
  showSubViews();
}

/**
* Increases the amount of levels with the provided value  (negative values allowed for decreasing).
*/
function increaseLevelAmount(amount) {
  levels+=amount;
  levels = Math.min(levels, maxLevels);
  levels = Math.max(levels, 1);
  $("#levels-amount").val(levels);
  showSubViews();
}

/**
* Draws the subview rectangles on the canvas with the rectangles provided by the backend.
*/
function showSubViews() {
  var offset = 1;
  var canvas = document.getElementById('previewCanvas');
  var context = canvas.getContext('2d');
  clearCanvas(canvas);
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
  var imageWidth = canvas.width - (offset * 2);
  var imageHeight = canvas.height - (offset * 2);
}

/**
* Clears the canvas from rectangles.
*/
function clearCanvas(canvas) {
  var context = canvas.getContext('2d');
  context.clearRect(0, 0, canvas.width, canvas.height);
}
