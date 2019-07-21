function loadSectors(deviceId){// pass your data in method
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