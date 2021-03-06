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

  if (typeof requestType == "undefined") {
    alert ("An empty visualization is no visualization!");
    return false;
  }

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
      request = false;
      return request;
    }

    if (Array.isArray($(this).val())) {
      request[$(this).attr("id")] = $(this).val().toString();
    } else {
      request[$(this).attr("id")] = $(this).val();
    }

  });

  if (!request) return request;

  premadeHtml.find("#exact").each( function () {
      request["isExact"] = $(this).is(":checked");
  });

  return request;
}

$(document).ready (function () {
    //prepopulate "type of visualization" dropdownlist by sending a request to the server
    populateGraphType();

    $('.hidden').hide();

    //sending a request to the server to run the query and load a new d3 graph
    $('#constructd3').click( function () {
      var type = $("#premade_type").val();

      request = getRequestForPremade();

      if (!request) return;

      $('#constructd3').attr("disabled", "true"); // Disable button until the server responses to prevent spam clicking.
     
      sendAjaxRequest("main.html", request, "GET", function(data) {
            $('#constructd3').removeAttr("disabled");
            if (data.isEmpty == "true") {
              alert("Empty result, please try another search conditions");
              return false;
            }
            $('#viz').attr('src', data.src);
      });
    });

    $("#yAttr").on('change', function () {
      showDropDown(3);
    });
});
