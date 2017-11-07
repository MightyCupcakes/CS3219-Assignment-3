function populatePresetsType() {
    var select = $('#premade_type');

    select.append($('<option>', { value: 1, text: 'Transition over time' }));
    select.append($('<option>', { value: 2, text: 'Contemporary comparison' }));
    select.append($('<option>', { value: 3, text: 'Top N X of Y' }));
}

function hideAll() {
  $("#startYear").hide();
  $("#endYear").hide();
  $("#n").hide();
  $("#xAttr").hide();  
  $("#yAttr").hide();
  $('#yValue').hide();
  $('#startyearLabel').hide();
  $('#endyearLabel').hide();  
  $('#yAttrLabel').hide();
  $("#xAttrLabel").hide();   
  $('#yValueLabel').hide(); 
  $('#nLabel').hide();

}
//temprorary populate with pre-defined
function populateYear() {
  var selectStartYear = $('#startYear');
  var selectEndYear = $('#endYear');
  for (i = 1997; i <= 2015; i++) {
    selectStartYear.append($('<option>', { value : i, text: i}));
    selectEndYear.append($('<option>', { value : i, text: i}));    
  }

}
//temprorary populate with pre-defined
function populateNnXnY() {
  var selectN = $('#n');
  for (i = 1; i < 20; i++) {
    selectN.append($('<option>', { value : i, text: i}));
  }
  var selectN = $('#xAttr');
  selectN.append($('<option>', { value : "author", text: "author"}));
  selectN.append($('<option>', { value : "citation", text: "citation"}));
  var selectY = $('#yAttr');
  selectY.append($('<option>', { value : "author", text: "author"}));
  selectY.append($('<option>', { value : "conference", text: "conference"}));  
  $('#yAttr').val("author").attr('selected', 'selected');
  populateY();
}
//temprorary populate with pre-defined
function populateY() {
  var yAttr = $("#yAttr").val();
  var selectY = $('#yValue');
  $('#yValue').empty();
  if (yAttr == "conference") {
    selectY.append($('<option>', { value : "A4", text: "A4"}));
    selectY.append($('<option>', { value : "D13", text: "D13"}));  
  } else if (yAttr == "author") {
    selectY.append($('<option>', { value : "Joshua Bengio", text: "Joshua Bengio"}));
    selectY.append($('<option>', { value : "Damien Chablat", text: "Damien Chablat"}));
  }
}

$(document).ready (function () {
    
    populatePresetsType();
    populateYear();
    populateNnXnY();
    hideAll();

    sendAjaxRequest("main.html", {requestType:"getVisualisation"}, "GET", 
        function(data) { 
            $('#viz').attr('src', data.src)
     });

    //to be rewritten
    $("#premade_type").on('change', function() {   
      var type = $("#premade_type").val();
      if (type == 1) {
        hideAll();
        $("#startYear").show();
        $("#endYear").show();
        $('#startyearLabel').show();
        $('#endyearLabel').show();          
        alert("this is transition over time")
      } else if (type == 2) {
        hideAll();
        $("#startYear").show();
        $('#startyearLabel').show();
        alert("contemprory comparison");
      } else if (type == 3) {
        hideAll();
        $("#n").show();
        $("#xAttr").show();       
        $("#xAttrLabel").show();          
        $("#yAttr").show();
        $('#yValue').show();
        $('#yAttrLabel').show();
        $('#yValueLabel').show(); 
        $('#nLabel').show();        
        alert("top n x of y");
      }
    });
    //to be rewritten
    $('#constructd3').click(function () {
      var type = $("#premade_type").val();
      if (type == 3) {
        request = {
          "n": $("#n").val(),
          "xAttr": $("#xAttr").val(),
          "yAttr": $("#yAtt").val(),
          "yvalue": $("#yValue").val(),
        };
        alert('sending request to server' + JSON.stringify(request));
        sendAjaxRequest("main.html", request, "GET", function(data) {
          $('#viz').attr('src', data.src)
        });
      }
    });

    $("#yAttr").on('change', function () {
      populateY();
    });

  $( "#progressbar" ).progressbar({
      value: false
    });
});
