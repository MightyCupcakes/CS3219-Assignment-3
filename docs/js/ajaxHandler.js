function sendAjaxRequest(page, data, method, processingFunction) {
    $.ajax({
    
      // The URL for the request
      url: page,
      
      // The data to send (will be converted to a query string)
      data: data,
      
      // Whether this is a POST or GET request
      type: method,
      
      // The type of data we expect back
      dataType : 'json'
    })
    // Code to run if the request succeeds (is done);
    // The response is passed to the function
    .done(processingFunction);
}