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

function getRequestForPremade() {
  var requestType = premadeHtml.find("#requestType").val();

  var request = {
    requestType: "getVisualisation",
    premadeType: premadeCursor.val(),
    premadeQuery: premadeQuery.val()
  };

  if (premadeHtml.find("#requestType").attr('data-custom') == 'true') {
    custom_request = functions['parse']();

    for (var key in custom_request) {
        request[key] = custom_request[key];
    }

    return request;
  }

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
    $('#constructd3').click( function () {
      var type = $("#premade_type").val();

      request = getRequestForPremade();
     
      sendAjaxRequest("main.html", request, "GET", function(data) {
            $('#viz').attr('src', data.src);
      });
    });

    $("#yAttr").on('change', function () {
      showDropDown(3);
    });
});
