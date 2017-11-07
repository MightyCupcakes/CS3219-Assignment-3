function populatePresetsType() {
    var select = $('#premade_type');
    select.append($('<option>', { value: 1, text: 'Transition over time' }));
    select.append($('<option>', { value: 2, text: 'Contemporary comparison' }));
    select.append($('<option>', { value: 3, text: 'Top N X of Y' }));
}

function showDropDown(type) {

  $("#startYear").hide();
  $('#startyearLabel').hide();
  $("#endYear").hide();
  $('#endyearLabel').hide();
  $("#n").hide();
  $("#xAttr").hide();  
  $("#yAttr").hide();
  $('#yValue').hide();
  $('#yAttrLabel').hide();
  $("#xAttrLabel").hide();   
  $('#yValueLabel').hide(); 
  $('#nLabel').hide();  

  if (type == 1) {

    $("#startYear").show();
    $("#endYear").show();
    $('#startyearLabel').show();
    $('#endyearLabel').show(); 

  } else if (type == 2) {

    $("#startYear").show();
    $('#startyearLabel').show();

  } else if (type == 3) {

    $("#n").show();
    $("#xAttr").show();       
    $("#xAttrLabel").show();          
    $("#yAttr").show();
    $('#yValue').show();
    $('#yAttrLabel').show();
    $('#yValueLabel').show(); 
    $('#nLabel').show();
  }


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
//Let user choose from a predefined set of N, X 
function populateNnX() {
  var selectN = $('#n');
  for (i = 1; i < 15; i++) {
    selectN.append($('<option>', { value : i, text: i}));
  }
  var selectN = $('#xAttr');
  selectN.append($('<option>', { value : "author", text: "author"}));
  selectN.append($('<option>', { value : "citationtitle", text: "citationtitle"}));
  selectN.append($('<option>', { value : "venue", text: "venue"}));
  selectN.append($('<option>', { value : "title", text: "title"}));
  //base paper may require the query to be processed in another way
  selectN.append($('<option>', { value : "basepaper", text: "basepaper"})); 
  var selectY = $('#yAttr');
  selectY.append($('<option>', { value : "author", text: "author"}));
  selectY.append($('<option>', { value : "conference", text: "conference"}));  
  selectY.append($('<option>', { value : "venue", text: "venue"}));
  selectY.append($('<option>', { value : "year", text: "yearOfPublication"}));   
  $('#yAttr').val("author").attr('selected', 'selected');
 
}
//temprorary populate with pre-defined
function populateY(data, yAttr) {
  var selectY = $('#yValue');
  $('#yValue').empty();
  if (yAttr == "conference") {
    selectY.append($('<option>', { value : "A4", text: "A4"}));
  } else if (yAttr == "author") {
    selectY.append($('<option>', { value : "Joshua Bengio", text: "Joshua Bengio"}));
    selectY.append($('<option>', { value : "Damien Chablat", text: "Damien Chablat"}));
  } else if (yAttr = "venue") {

  } else if (yAttr = "yearOfPublication") {

  }
}

$(document).ready (function () {
    
    populatePresetsType();
    populateYear();
    populateNnX();
    hideAll();

    sendAjaxRequest("main.html", {getVisualisation:"true"}, "GET", 
        function(data) { 
            $('#viz').attr('src', data.src)
     });

    //to be rewritten
    $("#premade_type").on('change', function() {   
      var type = $("#premade_type").val();
      if (type == 1) {
        showDropDown(1);        
        alert("this is transition over time")
      } else if (type == 2) {
        showDropDown(2);        
        alert("contemprory comparison");
      } else if (type == 3) {
        showDropDown(3);               
        alert("top n x of y");
      }
    });
    //to be rewritten
    $('#constructd3').click(function () {
      var type = $("#premade_type").val();
      if (type == 3) {
        request = {
          "getVisualisation":"true",
          "vizType" : 3,
          "n": $("#n").val(),
          "xAttr": $("#xAttr").val(),
          "yAttr": $("#yAttr").val(),
          "yValue": $("#yValue").val(),
        };
        sendAjaxRequest("main.html", request, "GET", function(data) {
          console.log(data.dataMap);
          alert(data.dataMap);
          alert("hertert");
          $('#viz').attr('src', data.src);
        });
      }
    });

    $("#yAttr").on('change', function () {
      var attr = $("#yAttr").val();
      request = {
        "populateDropDown" : "true",
        "attribute" : attr
      };
      sendAjaxRequest("main.html", request, "GET", function(data) {
        var dataMap = data.dataMap;
        alert(dataMap);
        populateY(data, yAttr);
      })
    });

  $( "#progressbar" ).progressbar({
      value: false
    });
});
