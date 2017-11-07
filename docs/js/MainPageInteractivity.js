$( document ).ready ( function () {

    $('button#addNewCondition').click (function () {
        $('div#conditions').append($('div#newConditions').html())
    });

    $('button#removecondition').click (function () {
        $('div#conditions').children("div.form-group").last().remove();
    });

    $('#premade_type').change( function() {
        var select = $('#premade_query');
        var selected = $(this).find(":selected").text();

        select.append($('<option>', { value: 0, text: "" }));
        
        sendAjaxRequest("main.html", { requestType: "populateForm", formElement: "PremadeQueries", premadeVisuals: selected}, "GET", function(data) {
            counter = 0;
            data.forEach( function(obj) { select.append($('<option>', { value: counter++, text: obj })); } );
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