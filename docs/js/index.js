$(document).ready (function () {

    $.ajax({
    
      // The URL for the request
      url: "main.html",
   
      // The data to send (will be converted to a query string)
      data: {hi:"hi"},
   
      // Whether this is a POST or GET request
      type: "GET",
   
      // The type of data we expect back
      dataType : 'text'
      })
    // Code to run if the request succeeds (is done);
    // The response is passed to the function
    .done(function( data ) {
       if (data != 'false') {
          $('#viz').attr('src', data);
       }
    });

});
