var selectedColumns = [];

$( document ).ready ( function () {

    $('button#addNewCondition').click (function () {
        $('div#conditions').append($('div#newConditions').html())
    });

    $('button#removecondition').click (function () {
        $('div#conditions').children("div.form-group").last().remove();
    });

    $('#premade_type').change( function() {
        var select = $('#premade_query');
        var selected = $(this).find(":selected").val();

        select.html("");

        select.append($('<option>', { value: 0, text: "" }));

        premadeVisuals[selected - 1].queries.forEach(function(obj) {
            select.append($('<option>', { value: obj, text: obj }));
        });
    });

    $('#premade_query').change( function () {
        var selected = $(this).find(":selected").val();

        generatePremadeQuery(selected);
    });

    $('#columnsort').autocomplete( {
        source: selectedColumns
    })
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
