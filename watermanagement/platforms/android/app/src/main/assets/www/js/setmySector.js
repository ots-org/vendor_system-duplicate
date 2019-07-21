function saveAutoSwitch(){
   // alert(deviceId,sectorId)
    var autoSwitch = $('#autoSwitch').val();
    if(autoSwitch == "on") {
        /**
         * Call API to update DB for auto true
         */
        var data = JSON.stringify(
            {
                "RequestHeader": {
                "AppInstanceID": "1.0.0.1.B",
                "SourceID": "INSTAG"
            },
                "RequestData": {
                "MobileNumber": GlobMobileNum,
                "Command": GlobMannualCmdOff,
                "IsAutomatic":true,
                "UserID": 1,
                "SectorId":GlobSectorId,
                "DeviceId":GlobDeviceId
            }
          });
        var urlCmdOn = 'http://192.168.0.18'+GlobMannualCmdOff;
        if(globWifiData){
            $.ajax({
                      type:'GET',
                      url:urlCmdOn,
                      data:"",
                      success: function(data){
                        alert('successful');
                      }
                   }
                );
        }else{
            $.ajax({
                    type: "POST",
                    url: "http://13.233.104.153:8080/leat_demo_registration-0.0.1-SNAPSHOT/ExecuteCommand",
                    data: data,// now data come in this
                    contentType: "application/json; charset=utf-8",
                    crossDomain: true,
                    dataType: "json",
                    success: function (data, status, jqXHR) {
                        alert(data)
                    },

                    error: function (jqXHR, status) {
                        // error handler
                        console.log(jqXHR);
                        alert('fail' + status.code);
                    }
                });
        }

        $('#motorSwitch').slider({ disabled: true });
    }else{
        /**
         * Call API to update DB for auto false
         */
        var data = JSON.stringify(
            {
                "RequestHeader": {
                "AppInstanceID": "1.0.0.1.B",
                "SourceID": "INSTAG"
            },
                "RequestData": {
                "MobileNumber": GlobMobileNum,
                "Command": GlobMannualCmdOn,
                "IsAutomatic":false,
                "UserID": 1,
                "SectorId":GlobSectorId,
                "DeviceId":GlobDeviceId
            }
          });

        var urlCmdOn = 'http://192.168.0.18'+GlobMannualCmdOn;
        if(globWifiData){
         $.ajax({
                   type:'GET',
                   url:urlCmdOn,
                   data:"",
                   success: function(data){
                     alert('successful');
                   }
                }
             );
        }else{
            $.ajax({
                    type: "POST",
                    url: "http://13.233.104.153:8080/leat_demo_registration-0.0.1-SNAPSHOT/ExecuteCommand",
                    data: data,// now data come in this
                    contentType: "application/json; charset=utf-8",
                    crossDomain: true,
                    dataType: "json",
                    success: function (data, status, jqXHR) {
                        alert(success)
                    },

                    error: function (jqXHR, status) {
                        // error handler
                        console.log(jqXHR);
                        alert('fail' + status.code);
                    }
                });
        }
        $('#motorSwitch').slider({ disabled: false });
    }
}

function switchMotorOn(){
    var motorSwitch = $('#motorSwitch').val();
    if(motorSwitch == "on"){
        /**
         * call API for switch on the motor 
         */
        var data = JSON.stringify(
            {
                "RequestHeader": {
                "AppInstanceID": "1.0.0.1.B",
                "SourceID": "INSTAG"
            },
                "RequestData": {
                "MobileNumber": GlobMobileNum,
                "Command": GlobMannualCmdOn,
                "IsAutomatic":false,
                "UserID": 1,
                "SectorId":GlobSectorId,
                "DeviceId":GlobDeviceId
            }
          });
      var urlCmdOn = 'http://192.168.0.18'+GlobMannualCmdOn;
      if(globWifiData){
            $.ajax({
                  type:'GET',
                  url:urlCmdOn,
                  data:"",
                  success: function(data){
                    alert('successful');
                  }
               }
            );
      }else{
            $.ajax({
                    type: "POST",
                    url: "http://13.233.104.153:8080/leat_demo_registration-0.0.1-SNAPSHOT/ExecuteCommand",
                    data: data,// now data come in this
                    contentType: "application/json; charset=utf-8",
                    crossDomain: true,
                    dataType: "json",
                    success: function (data, status, jqXHR) {
                        //alert(success)
                    },

                    error: function (jqXHR, status) {
                        // error handler
                        console.log(jqXHR);
                        alert('fail' + status.code);
                    }
                }
            );
      }
    }else{
         /**
         * call API for switch off the motor 
         */
        var data = JSON.stringify(
            {
                "RequestHeader": {
                "AppInstanceID": "1.0.0.1.B",
                "SourceID": "INSTAG"
            },
                "RequestData": {
                "MobileNumber": GlobMobileNum,
                "Command": GlobMannualCmdOff,
                "IsAutomatic":false,
                "UserID": 1,
                "SectorId":GlobSectorId,
                "DeviceId":GlobDeviceId
            }
          });
        var urlCmdOn = 'http://192.168.0.18'+GlobMannualCmdOff;
        if(globWifiData){
            $.ajax({
                  type:'GET',
                  url:urlCmdOn,
                  data:"",
                  success: function(data){
                    alert('successful');
                  }
               }
            );
        }else{
            $.ajax({
                    type: "POST",
                    url: "http://13.233.104.153:8080/leat_demo_registration-0.0.1-SNAPSHOT/ExecuteCommand",
                    data: data,// now data come in this
                    contentType: "application/json; charset=utf-8",
                    crossDomain: true,
                    dataType: "json",
                    success: function (data, status, jqXHR) {
                       // alert(success)
                    },

                    error: function (jqXHR, status) {
                        // error handler
                        console.log(jqXHR);
                        alert('fail' + status.code);
                    }
                });
        }
    }
}

function setAndLoadThreshold(lower,upper,sectorId){
    screenTransition('threshholdLimit.html');
    $('#lowerThreshId').val(lower);
    $('#upperThreshId').val(upper);
}