var columns = 2;
var levels = 2;
var rows = 2;
var maxRows = 3;
var maxColumns = 3;
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
});

/**
 * Resets the modal.
 */
function resetModal() {
  switchTab(1);
  $("#columns-amount").val(columns);
  $("#rows-amount").val(rows);
  $("#levels-amount").val(levels);

  $('#auto_presets_div .close').prop('disabled', false);
  $('#auto_presets_div #autopreset_cancelbutton').prop('disabled', false);
  $('#auto_presets_div #autopreset_startbutton').prop('disabled', false);

  $('#auto_presets_div #autopreset_startbutton').attr('class', 'btn');
  $('#auto_presets_div #autopreset_savebutton').attr('class', 'btn hidden');
}

/**
 * Prepares the generating tab showing the progress bar.
 */
function switchGenerateTab() {
  switchTab(2);

  $('#auto_presets_div #autopreset_startbutton').attr('class', 'btn hidden');
  $('#auto_presets_div #autopreset_savebutton').attr('class', 'btn hidden');
  $('#auto_presets_div #autopreset_cancelbutton').prop('disabled', true);
}

/**
 * Prepares the final tab.
 */
function switchFinalTab(generatedPresets) {
  var presetIDs = generatedPresets['presetIDs'];

  for (key in presetIDs) {
    var preset = findPresetOnID(presetIDs[key]);
    drawGeneratedPreset(preset);
  }

  switchTab(3);
  $('#auto_presets_div #autopreset_savebutton').attr('class', 'btn');
  $('#auto_presets_div #autopreset_savebutton').prop('disabled', false);
}



/**
 * Allows tab switching.
 * @param stepnumber The tab to switch to.
 */
function switchTab(stepnumber) {
  $('#auto_presets_div .close').prop('disabled', true);
  $('#auto_presets_div #autopreset_savebutton').prop('disabled', true);

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
    switchGenerateTab();

    // Update statusbar ever 2sec (2000ms)
    var interval = setInterval(updateProgressbar, 2 * 1000);
    $.get("/api/backend/presets/autocreatepresets?camera="+currentcamera+"&rows="+rows+"&levels="+levels+"&columns="+columns+"&name="+name + "&tags="+presetTag, function(data) {
      clearInterval(interval);
      switchFinalTab(data);
    });

  }
}

/**
* Updates the progressbar
*/
function updateProgressbar() {
  $.get("/api/backend/presets/autocreatepresetsstatus?camera=" + currentcamera, function(data) {
    var dataJSON = JSON.parse(data);
    if ( dataJSON.succes === undefined ) {
      console.log(dataJSON.amount_created);
      var percentage_done = 100*(dataJSON.amount_created / dataJSON.amount_total);
      $("#auto-preset-creation-progressbar").css('width', percentage_done + "%")
                                            .attr("aria-valuenow", percentage_done)
                                            .text(dataJSON.amount_created + "/" +  dataJSON.amount_total);
    }
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
         context.strokeRect(x, y, width, height);
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

/**
 * Creates toggleable checkbox buttons.
 */
$('.button-checkbox').on('click', function () {
  var checkbox = $(this).find('input:checkbox')

});

/**
 * Checks a checkbox button.
 */
function check() {
  var checkbox = $(this).find('input:checkbox');
  var state = checkbox.is(':checked');

  checkbox.prop('checked', !state);
  updateState($(this), !state);
}

/**
 * Updates a checkbox state.
 * @param button  jQuery button
 * @param checked Value representing the current button state.
 */
function updateState(button, checked) {
  var checkicon = button.find('.checkicon');

  if (checked) {
    button.attr('class', 'button-checkbox btn btn-primary active');
    checkicon.attr('class', 'checkicon glyphicon glyphicon-check');
  } else {
    button.attr('class', 'button-checkbox btn btn-primary');
    checkicon.attr('class', 'checkicon glyphicon glyphicon-unchecked');
  }
}

/**
 * Draws a preset to the list of generated presets.
 * @param preset
 */
function drawGeneratedPreset(preset) {
  var list = $('#autopreset-generated');
  var li = $('<li></li>');
  var button = $('<div class="button-checkbox btn btn-primary"></div>');

  button
      .append('<img class="img-rounded" src="api/backend' + preset['image'] + '">')
      .append('<span>' + preset['name'] + '</span>')
      .append('<span class="checkicon glyphicon glyphicon-unchecked"></span>')
      .append('<input presetid="' + preset['id'] + '" type="checkbox" class="hidden" />');

  button.click(check);
  li.append(button);
  list.append(li);
}

/**
 * Deletes unselected presets.
 */
function deleteUnselectedPresets() {
  $('#autopreset-generated').children().each(function() {
    var checkbox = $(this).find('input');
    var presetid = checkbox.attr('presetid');

    if (!checkbox.is(':checked')) {
      findPresetOnID(presetid).delete();
    };
  });
  $('#autopreset-generated').empty();
}


