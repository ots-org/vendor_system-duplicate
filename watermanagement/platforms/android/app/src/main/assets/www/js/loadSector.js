function loadSectors(deviceId){// pass your data in method

    $.ajax({
            type: "GET",
            url: "http://13.233.104.153:8080/leat_demo_registration-0.0.1-SNAPSHOT/WeatherReport",
            success: function (data, status, jqXHR) {
                        $("#todayweather").text(parseInt(data[0].Temprature)+'°');
                        $("#todaydescription").text(data[0].description);

                        $("#DayOneTempId").text(parseInt(data[2].Temprature)+'°');
                        $("#DayTwoTempId").text(parseInt(data[10].Temprature)+'°');
                        $("#DayThreeTempId").text(parseInt(data[15].Temprature)+'°');
                        $("#DayFourTempId").text(parseInt(data[35].Temprature)+'°');

            },

            error: function (jqXHR, status) {
                // error handler
                console.log(jqXHR);
                alert('fail' + JSON.stringify(jqXHR));
            }
        });


	var sectorListItem = '<li data-role="list-divider" class="slideMenuMainDivider orderRefillConfirmHeaders languagespecificHTML" data-text="sectorList" role="heading"> My Sectors</li>';
	$("#sectorListContentList").empty();
	jQuery.each( userDeviceList.ResponseData.DeviceDetails, function(j,valDevice) {
		if(valDevice.DeviceId == deviceId){
			jQuery.each( userDeviceList.ResponseData.DeviceDetails[j].Sectors, function(i,val) {
				sectorListItem +='<li><a href="#" onclick="loadSectorDetails('+deviceId+','+val.SectorId+','+valDevice.MobileNumber+')">'
				sectorListItem += '<div class="floatLeft paymentInfoList"><div class="floatLeft halfWidthDiv boldText">'+val.SectorName+'</div><div class="floatLeft maxWidth paymentInfoList">Location : Jayanagar  </div><div class="floatLeft maxWidth paymentInfoList">Region : Ortusolis  </div><div class="floatLeft maxWidth paymentInfoList">Automatic : <b>'+val.IsAutomatic+'</b>  </div></div><br></a></li>'
			});
		}
	});
	$("#sectorListContentList").append(sectorListItem).listview("refresh");
	loadLanguagePage(globLanguage);
	screenTransition('mySectors.html');
}