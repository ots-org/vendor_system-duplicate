function loadDeviceListData(){// pass your data in method
	var data = JSON.stringify({"RequestHeader": {"AppInstanceID": "1.0","SourceID": "AndriodAPP"},"RequestData": {"UserID": 1}});
	$.ajax({
			type: "POST",
			url: "http://13.233.104.153:8080/leat_demo_registration-0.0.1-SNAPSHOT/GetUserSectors",
			data: data,// now data come in this 
			contentType: "application/json; charset=utf-8",
			crossDomain: true,
			dataType: "json",
			success: function (data, status, jqXHR) {
			    userDeviceList = data;
				var deviceListItem = '<li data-role="list-divider" class="slideMenuMainDivider orderRefillConfirmHeaders languagespecificHTML" data-text="deviceList" role="heading"> My Devices</li>';
				$("#deviceListContentList").empty();
				var countFlag =0;
				jQuery.each( data.ResponseData.DeviceDetails, function( i, val ) {
                    countFlag++;
					//alert("sectorId"+val.SectorId)
					deviceListItem +='<li><a href="#" onclick="loadSectors('+val.DeviceId+')">'
					deviceListItem += '<div class="floatLeft paymentInfoList"><div class="floatLeft halfWidthDiv boldText">'+val.Title+'</div><div class="floatLeft maxWidth paymentInfoList">MobileNumber : <b>'+val.MobileNumber+'</b>  </div></div><br></a></li>'
				});
				$("#deviceListContentList").append(deviceListItem).listview("refresh");
				if(countFlag == data.ResponseData.DeviceDetails.length){
					screenTransition('deviceList.html')
				}
				loadLanguagePage(globLanguage);
			},
			error: function (jqXHR, status,err) {
				// error handler
				console.log(jqXHR);
				alert("readyState: " + jqXHR.readyState);
				alert("responseText: "+ jqXHR.responseText);
				alert("status: " + jqXHR.status);
				alert("text status: " + status);
				alert("error: " + err);
			}
		 });
   }

   function enableWifi(){
        globWifiData = $('#wifiSwitch').val();
   }