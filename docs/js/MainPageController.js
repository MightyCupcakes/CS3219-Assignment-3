$(document).ready (function () {
  sendAjaxRequest("main.html", {data:"hi"}, "GET", 
    function(data) { 
      $('#viz').attr('src', data.src)
    })
});
