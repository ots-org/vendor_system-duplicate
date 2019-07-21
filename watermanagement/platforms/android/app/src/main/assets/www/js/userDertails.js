function userLogin(){// pass your data in method
	//screenTransition('userDetails.html');
	var data = JSON.stringify( {"RequestHeader": {"AppInstanceID": "1.0","SourceID": "AndriodAPP"},"RequestData": {"UserName" : "manoj.vg@ortusolis.com","Password" : "Password123"}});
	
	$.ajax({
			type: "POST",
			url: "http://13.233.104.153:8080/leat_demo_registration-0.0.1-SNAPSHOT/validate",
			data: data,// now data come in this 
			contentType: "application/json; charset=utf-8",
			crossDomain: true,
			dataType: "json",
			success: function(data){
					
					$("#FirstName").text(data.ResponseData.UserInfo.FirstName);
					$("#LastName").text(data.ResponseData.UserInfo.LastName);
					$("#PhoneNumber").text(data.ResponseData.Customer.Phone1);
					$("#Email").text(data.ResponseData.UserInfo.UserName);
					$("#Address").text(data.ResponseData.UserInfo.Address);
					screenTransition('userDetails.html');j
			},
			error: function (jqXHR, status) {
				// error handler
				console.log(jqXHR);
				alert('fail' + status.code);
			}
		 });
	 }