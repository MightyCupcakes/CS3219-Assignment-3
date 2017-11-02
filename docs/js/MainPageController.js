function populatePresetsType() {
    var select = $('#premade_type');

    select.append($('<option>', { value: 1, text: 'Transition over time' }));
    select.append($('<option>', { value: 2, text: 'Contemporary comparison' }));
    select.append($('<option>', { value: 3, text: 'Top N X of Y' }));
}

$(document).ready (function () {
    
    populatePresetsType();

    sendAjaxRequest("main.html", {data:"hi", data2:"hihi"}, "GET", 
        function(data) { 
            $('#viz').attr('src', data.src)
     });


  $( "#progressbar" ).progressbar({
      value: false
    });
});
