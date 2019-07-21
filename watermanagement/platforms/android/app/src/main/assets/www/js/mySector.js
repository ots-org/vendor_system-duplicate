function loadSectorDetails(deviceId,sectorId,mobileNumber){
    GlobMobileNum = mobileNumber;
    jQuery.each(userDeviceList.ResponseData.DeviceDetails, function( j, val ) {
        if(deviceId == val.DeviceId){
            jQuery.each(userDeviceList.ResponseData.DeviceDetails[j].Sectors, function( i, val ) {
                if(sectorId == val.SectorId){
                   // var autoSelectHtml = '<select name="autoSwitch" id="autoSwitch"   onchange="saveAutoSwitch('+deviceId+','+sectorId+')" data-role="slider" data-mini="true"><option value="off">Off</option><option value="on">On</option></select>';
                    $("#threshHoldItem").empty();
                    $("#CurrentHumidity").empty();
                    $("#CurrentTemp").empty();
                    $("#CurrentMoisture").empty();
                    $("#CurrentThresh").empty();
                   // $('#automaticId').empty();
                   GlobDeviceId = deviceId;
                   GlobSectorId = sectorId;
                   GlobMannualCmdOn = val.ManualCommandOn;
                   GlobMannualCmdOff = val.ManualCommandOff;
                   $('#deviceId').val(deviceId);
                    if(val.IsAutomatic==true){
                            $('#autoSwitch').val('on').slider("refresh");
                            $('#motorSwitch').slider({ disabled: true });
                    }else{
                            $('#autoSwitch').val('off').slider("refresh");
                            $('#motorSwitch').slider({ disabled: false });
                    }
                    //$('#automaticId').html(autoSelectHtml);
                    var threshHoldSet = '<a href="#" onclick="setAndLoadThreshold('+val.LowerThreshold+','+val.UpperThreshold+','+val.SectorId+')"><div class="floatLeft paymentInfoList"><div class="floatLeft maxWidth paymentInfoList" id="threshHoldId"><font color="red"> Change Your Sector Threshhold Limit </font>:<b> Low-'+val.LowerThreshold+',High-'+val.UpperThreshold+' </b></div></div><br></a>';
                    $('#threshHoldItem').html(threshHoldSet);
                    $('#CurrentHumidity').html(val.CurrentHumidity);
                    $('#CurrentTemp').html(val.CurrentTemprature);
                    $('#CurrentMoisture').html(val.CurrentMoisture);
                    $('#CurrentThresh').html(val.Threshold);
                    $('#SectorNameId').html("Change Setting for  :"+val.SectorName);
                }
            });
        }
    });
    screenTransition('setmySector.html');
    loadLanguagePage(globLanguage);
}

function setWifi(){
    if(globWifiData == 'on'){
        $('#wifiSwitch').val('on').slider("refresh");
    }else{
        $('#wifiSwitch').val('off').slider("refresh");
    }
}