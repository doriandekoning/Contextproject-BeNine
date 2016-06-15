var columns = 3;
var levels = 3;
var rows = 3;
var maxRows = 5;
var maxColumns = 5;
var maxLevels = 3;

/**
 * Disables default tab click behavior.
 */
$(".auto-presets-modal .disabled").click(function (e) {
  e.preventDefault();
  return false;
});

/**
* Executed when the modal for auto preset creation loads. Adds the mjpeg stream to the image behind the canvas.
*/
$( ".auto-presets-modal").on("shown.bs.modal", function(e) {
  resetModal();
  var image = $("#auto-preset-creation-preview-image");
  var liveimage = $("#auto-preset-creation-live-image");

  var streamURL = '/api/backend/camera/' + currentcamera+ '/mjpeg?height=360&width=640';
  image.attr('src', streamURL);
  liveimage.attr('src', streamURL);

  showSubViews();
})

function resetModal() {
  switchTab(1);

  $('#auto_presets_div .close').prop('disabled', false);
  $('#auto_presets_div #cancelbutton').prop('disabled', false);
  $('#auto_presets_div #startbutton').prop('disabled', false);

}

/**
 * Allows tab switching.
 * @param stepnumber The tab to switch to.
 */
function switchTab(stepnumber) {
  $('#auto_presets_div .close').prop('disabled', true);
  $('#auto_presets_div #cancelbutton').prop('disabled', true);
  $('#auto_presets_div #startbutton').prop('disabled', true);


  var tabs = $('#autopreset-tabs');
  tabs.children().attr('class', 'disabled');
  tabs.children().click(function (e) {
    e.preventDefault();
    return false;
  });

  var newTab = tabs.find('a[href="#autopreset_step' + stepnumber + '"]');
  newTab.attr('class', 'active');
  newTab.tab('show');

}

/**
* Executed when the auto create presets button is pressed.
* Sends a command to the server to start auto creating presets with the current amount of columns,rows and levels.
*/
function autoCreatePresets() {
  var name = $('#auto_preset_name').val();
  var presetTag = $('#auto_preset_tags').val();
  if (currentcamera !== undefined) {
    // Switch to generating view.
    switchTab(2);

    $.get("/api/backend/presets/autocreatepresets?camera="+currentcamera+"&rows="+rows+"&levels="+levels+"&columns="+columns+"&name="+name + "&tags="+presetTag, function(data) {
      // If done switch to final screen.
      switchTab(3);
    });
  }
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
}

/**
* Clears the canvas from rectangles.
*/
function clearCanvas(canvas) {
  var context = canvas.getContext('2d');
  context.clearRect(0, 0, canvas.width, canvas.height);
}

