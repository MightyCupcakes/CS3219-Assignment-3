//Cursor for the elements
var premadeCursor = $('#premade_type');
var premadeHtml = $('#premadeHtml');
var premadeQuery = $('#premade_query');
/*
function populatePresetsType() {
    var select = $('#premade_type');

    select.append($('<option>', { value: 0, text: "" }));

    counter = 1;

    premadeVisuals.forEach(function(obj) {
        select.append($('<option>', { value: counter++, text: obj.type }));
    });
}*/

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

function parseUserQuery() {
    var query = {};

    var typeOfGraph = $('#graphType').val();
    query["typeOfGraph"] = typeOfGraph;

    var column1ShowType = $('#column1show').attr('data-value');
    var column2ShowType = $('#column2show').attr('data-value');

    query["column1ShowType"] = column1ShowType;
    query["column2ShowType"] = column2ShowType;

    var column1Name = $('#column1').val();
    var column2Name = $('#column2').val();

    query["column1Name"] = column1Name;
    query["column2Name"] = column2Name;

    var counter = 0;
    var conditionDiv = $('div#conditions');

    conditionDiv.find('input.conditionColumn').each( function () {
        counter ++;

        query["conditionColumn" + counter] = $(this).val();
    });

    counter = 0;

    conditionDiv.find('button.conditionComparator').each ( function() {
        counter ++;

        query["conditionComparator" + counter] = $(this).attr('data-value');
    });

    counter = 0;

    conditionDiv.find('input.conditionValue').each (function () {
        counter ++;

        query["conditionValue" + counter] = $(this).val();
    });

    counter = 0;

    conditionDiv.find('button.conditionCombine').each (function () {
        counter ++;

        query["conditionCombine" + counter] = $(this).attr('data-value');
    });

    query["numOfConditions"] = counter;

    query["columnsort"] = $("#columnsort").val();
    query["columnsortorder"] = $("#columnsortorder").attr('data-value');

    return query;
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
  var request =premadeHtml.find("#requestType").val();

  var request = {
    requestType: "getVisualisation",
    premadeType: premadeCursor.val(),
    premadeQuery: premadeQuery.val()
  };
  
  premadeHtml.find(".form-control").each( function () {
    request[$(this).attr("id")] = $(this).val();
  } );
  console.log(request);
  return request;
}



$(document).ready (function () {
    //load preset and hide dropdownlist list
  //  populatePresetsType();
    populateGraphType();

    $('.hidden').hide();

    sendAjaxRequest("main.html", {requestType:"getVisualisation"}, "GET", 
        function(data) { 
            $('#viz').attr('src', data.src)
     });


    //to be rewritten
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
