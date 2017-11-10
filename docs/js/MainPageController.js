//Cursor for the elements
var premadeCursor = $('#premade_type');
var premadeHtml = $('#premadeHtml');
var premadeQuery = $('#premade_query');

function populateGraphType() {
    premadeCursor.append($('<option>', { value: 0, text: "" }));

    sendAjaxRequest("main.html", {requestType:"populateForm", formElement:"premade_type"}, "GET", 
        function(data) {
            counter = 1;
            data.forEach( function(obj) {
                premadeCursor.append($('<option>', { value: obj, text: obj }));
            });
     });
}

function generatePremadeQuery(selected) {
    var graphType = $('#graphType');
    var limitlabel = $('#limitlabel');
    var column1show = $('#column1show');
    var column2show = $('#column2show');
    var column1 = $('#column1');
    var column2 = $('#column2');
    var conditions = $('#conditions');

    conditions.html("");

    if (selected == "Top authors for a conference") {
      $('#topauthors').show();
        graphType.val("Bar Chart");
        limitlabel.text("Number of authors to show");
        $("#limit").val(10);

        column1show.attr("data-value", "Display");
        column1show.text("Display");

        column2show.attr("data-value", "count");
        column2show.text("Count");

        column1.val("Journal Authors");
        column1.trigger("change");
        column2.val("Journals");
        column2.trigger("change");

        conditions.append($("#newConditions").html());

        var firstCondition = conditions.children("div.row").first();

        // Get the first condition row
        firstCondition.find(".conditionColumn").first().val("Journal Venue");
        firstCondition.find(".conditionValue").first().val("ArXiV");

    } else if (selected == "Number of citations for a conference over a few years") {
        $("#limit").parent().hide();

        graphType.val("Line Chart");

        column1show.attr("data-value", "Display");
        column1show.text("Display");

        column2show.attr("data-value", "count");
        column2show.text("Count");

        column1.val("Journal Venue");
        column2.val("Citations");

        conditions.append($("#newConditions").html());

        var firstCondition = conditions.children("div.row").first();

        firstCondition.find(".conditionColumn").first().val("Journal Venue");
        firstCondition.find(".conditionValue").first().val("ArXiV");
    } else if (selected == "Base paper = Low-density parity check codes over GF(q)"){
    	graphType.val("Citation Network");
    } else {
      $("#advanced").hide();
    }
}
function getRequestForPremade(premadeType) {
  var requestType =premadeHtml.find("#requestType").val();
  
  var request = {
    requestType: "getVisualisation",
    premadeType: premadeCursor.val(),
    premadeQuery: premadeQuery.val()
  };
  
  premadeHtml.find(".form-control").each( function () {
    if ($(this).val() == "") {
      alert("Please do not leave any field blank");
      return false;
    }
    request[$(this).attr("id")] = $(this).val();
  } );

  return request;
}

$(document).ready (function () {
    //prepopulate "type of visualization" dropdownlist by sending a request to the server
    populateGraphType();

    $('.hidden').hide();

    //loading a default visualization d3 graph
    sendAjaxRequest("main.html", {requestType:"getVisualisation"}, "GET", 
        function(data) { 
            $('#viz').attr('src', data.src)
     });


    //sending a request to the server to run the query and load a new d3 graph
    $('#constructd3').click(function () {
      var type = $("#premade_type").val();

      // Parses the user input into a JSON string
      var query = parseUserQuery();
      var premadeType = premadeCursor.val();
      request = getRequestForPremade(premadeType);
     
      sendAjaxRequest("main.html", request, "GET", function(data) {
            $('#viz').attr('src', data.src);
      });
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

    $("#premade_query").on('change', function() {
      var premadeType = $(this).val();
      if (premadeType == 0) {
        premadeHtml.hide();
      } else {
        premadeHtml.show();
      }
      alert(premadeType);
      request = {
        requestType: "retrievePremadeType",
        premadeType : premadeType
      };
      sendAjaxRequest("main.html", request, "GET", function(data) {
        premadeHtml.html(data.response);
      });
    });
});
