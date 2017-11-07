$( document ).ready ( function () {

    $('button#addNewCondition').click (function () {
        $('div#conditions').append($('div#newConditions').html())
    });

    $('button#removecondition').click (function () {
        $('div#conditions').children("div.form-group").last().remove();
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