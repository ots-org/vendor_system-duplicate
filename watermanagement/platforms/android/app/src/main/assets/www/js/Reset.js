function reset(){
    //alert('Devise');
    $.ajax({
        type: "GET",
        url: "http://13.233.104.153:8080/leat_demo_registration-0.0.1-SNAPSHOT/ResetDevice",
        success: function (data, status, jqXHR) {
            alert('Device reset successful');
            screenTransition('home.html');
        },

        error: function (jqXHR, status) {
            // error handler
            console.log(jqXHR);
            alert('fail' + status.code);
        }
  });
  }