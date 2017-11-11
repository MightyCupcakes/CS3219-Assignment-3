$( document ).ready ( function () {

    $('#premade_type').change( function() {
        $('#premadeHtml').hide();
        var select = $('#premade_query');
        var selected = $(this).find(":selected").val();
        if (selected == 0 ){
            select.parent().hide();

        } else {
            select.parent().show();
        }
        select.html("");

        select.append($('<option>', { value: 0, text: "" }));

        sendAjaxRequest("main.html", {requestType:"populateForm", formElement:"premade_query", type:selected}, "GET", 
        function(data) {
            counter = 1;
            data.forEach( function(obj) {
                select.append($('<option>', { value: obj, text: obj }));
            });
        });
    });

    $("#premade_query").on('change', function() {
      var premadeType = $(this).val();

      if (premadeType == 0) {
        premadeHtml.hide();
      } else {
        premadeHtml.show();
      }

      request = {
        requestType: "retrievePremadeType",
        premadeType : premadeType
      };

      sendAjaxRequest("main.html", request, "GET", function(data) {
        premadeHtml.html(data.response);
      });
    });
    
});

$(document).on('click', '.dropdown-item',  function(e) {
    e.preventDefault();

    var value = $(this).data('value');
    var div = $(this).closest('div.input-group-btn');
    var button = div.children('button').first();

    button.text($(this).text());
    button.attr("data-value", value);
});

$(document).on('change', ".columnName", function() {
    if ($(this).attr('id') == "column1") {
        selectedColumns[0] = $(this).val();

    } else if ($(this).attr('id') == "column2") {
        selectedColumns[1] = $(this).val();
    }
});

$(document).on('keydown.autocomplete', ".columnName", function() {
    $(this).autocomplete({
      source: function(request, response) {
        $.getJSON("main.html", { term: request.term, requestType:"populateForm", formElement:"columnNames" }, 
              response);
    },
      minLength: 2
    });
});
