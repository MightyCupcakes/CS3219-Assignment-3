var yearMin = 1980;
var yearMax = 2016;
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
  $('#yValueYear').hide();
  $('#yAttrLabel').hide();
  $("#xAttrLabel").hide();   
  $('#yValueLabel').hide(); 
  $('#nLabel').hide();
  $('#yValueConf').hide();  

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
    $('#yAttrLabel').show();
    $('#yValueLabel').show(); 
    $('#yValue').show();
    $('#nLabel').show();
  }


}
//temprorary populate with pre-defined
function populateYear() {
  var selectStartYear = $('#startYear');
  var selectEndYear = $('#endYear');
  var selectYValueYear = $('#yValueYear');
  for (i = yearMin; i <= yearMax; i++) {
    selectStartYear.append($('<option>', { value : i, text: i}));
    selectEndYear.append($('<option>', { value : i, text: i}));    
    selectYValueYear.append($('<option>', { value : i, text: i}));    
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
  //remove author temp, author query are too big and laggy
  //selectY.append($('<option>', { value : "author", text: "author"})); 

  var selectYConf = $('#yValueConf');
  selectYConf.append($('<option>', { value : "A4", text: "A4"}));  
  $('#yAttr').val("author").attr('selected', 'selected');
 
}
/*
function populateY(yAttr) {
  var selectY = $('#yValue');
  $('#yValue').empty();
  if (yAttr == "conference") {
    selectY.append($('<option>', { value : "A4", text: "A4"}));
  } else if (yAttr == "author") {
    for (i = 0; i < authorArr.length; i++ ) {
      selectY.append($('<option>', { value : authorArr[i], text: authorArr[i]}));
    }
  } else if (yAttr == "venue") {
    for (i = 0; i < venueArr.length; i++ ) {
      var venue = venueArr[1].venue;
      selectY.append($('<option>', { value : venue, text: venue}));
    }
  } else if (yAttr == "yearOfPublication") {
    for (i = yearMin; i <= yearMax; i++) {
      selectY.append($('<option>', { value : i, text: i}));
    }
  }
}*/



$(document).ready (function () {
    //load preset and hide dropdownlist list
    populatePresetsType();
    showDropDown(1);

    sendAjaxRequest("main.html", {getVisualisation:"true"}, "GET", 
        function(data) { 
            $('#viz').attr('src', data.src)
     });

    //d populate dropdownlist with the data
    populateYear();
    populateNnX();

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
        var value;
        if ($("#yAttr").val() == "year") {
          value = $("#yValueYear").val();
        } else if($("#yAttr").val() == "conference"){
          value = $("#yValueConf").val();       
        } else {
          value = $("#yValue").val();
        }
        request = {
          "getVisualisation":"true",
          "vizType" : 3,
          "n": $("#n").val(),
          "xAttr": $("#xAttr").val(),
          "yAttr": $("#yAttr").val(),
          "yValue": value,
        };
        alert(JSON.stringify(request));
        sendAjaxRequest("main.html", request, "GET", function(data) {
          $('#viz').attr('src', data.src);
        });
      }
    });

    $("#yAttr").on('change', function () {
      var attr = $("#yAttr").val();
      if (attr == 'year') {
        $('#yValueConf').hide();        
        $('#yValueYear').show();
        $('#yValue').hide();        
      } else if (attr == 'conference') {
        $('#yValueConf').show();
        $('#yValueYear').hide();
        $('#yValue').hide();          
      } else {
        $('#yValueConf').hide();
        $('#yValueYear').hide();
        $('#yValue').show();   
      }
    });

  $( "#progressbar" ).progressbar({
      value: false
    });
});
