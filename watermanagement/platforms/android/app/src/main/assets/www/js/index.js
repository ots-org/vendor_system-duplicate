/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    // deviceready Event Handler
    //
    // Bind any cordova events here. Common events are:
    // 'pause', 'resume', etc.
    onDeviceReady: function() {
        this.receivedEvent('deviceready');
    },

    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

app.initialize();

var userDeviceList='';
 var GlobSectorId='';
 var GlobDeviceId='';
 var GlobMobileNum='';
 var GlobLowerThresCmd='';
 var GlobUpperThresCmd='';
 var GlobMannualCmdOn='';
 var GlobMannualCmdOff='';
 var GlobIsAutometic='';
 var globLanguage='';

 function screenTransition(landingPage) {
   $.mobile.pageContainer.pagecontainer("change", landingPage, {
 		transition: "slide",
 		reverse: false
 	});
 }

 function loadLanguagePage(languageSelected){
    globLanguage = languageSelected;
    var cLANGUAGE = languageSelected;
    navigator.globalization.getPreferredLanguage(
         //Get Language from Settings
         function (locale) {
            // cLANGUAGE = locale.value;
             cLANGUAGE = languageSelected;
             languageControls(cLANGUAGE);
         },
         //On Failure set language to english
         function () {cLANGUAGE = "en";}
     );

     var languageSpecificObject = null;
     var languageSpecificURL = "";
     var englishLanguageSpecificURL = '{"languageSpecifications": [{ "username":"User Name", "password":"Password", "headingtext":"Login", "submit":"Login","deviceList":"My Devices","sectorList":"My Sector","changeLanguage":"Change Language","sectorDetail":"Sector Inforamtion","automatic":"Automatic","motor":"Water Motor","humidity":"Current humidity","temperature":"Current Temperature","moisture":"Current Moisture","threshold":"Current Threshold"}]}';
     var hindiLanguageSpecificURL = '{"languageSpecifications": [{ "username":"उपयोगकर्ता नाम", "password":"पारण शब्द", "headingtext":"लॉग इन", "submit":"लॉग इन","deviceList":"मेरे उपकरण","sectorList":"मेरा सेक्टर","changeLanguage":"भाषा बदलो","sectorDetail":"सेक्टर की सूचना","automatic":"स्वचालित","motor":"पानी की मोटर","humidity":"वर्तमान आर्द्रता","temperature":"वर्तमान तापमान","moisture":"वर्तमान नमी","threshold":"वर्तमान थ्रेसहोल्ड"}]}';
     var tamilLanguageSpecificURL = '{"languageSpecifications": [{ "username":"பயனர் பெயர்", "password":"கடவுச்சொல்", "headingtext":"உள் நுழை", "submit":"உள் நுழை","deviceList":"எனது சாதனங்கள்","sectorList":"எனது துறை","changeLanguage":"மொழியை மாற்றுங்கள்","sectorDetail":"துறை தகவல்","automatic":"தானியங்கி","motor":"पவாட்டர் மோட்டார்","humidity":"தற்போதைய ஈரப்பதம்","temperature":"தற்போதைய வெப்பநிலை","moisture":"தற்போதைய ஈரப்பதம்","threshold":"தற்போதைய வாசல்"}]}';
     var kanLanguageSpecificURL = '{"languageSpecifications": [{ "username":"ನಿಮ್ಮ ಹೆಸರು", "password":"ಸಂಕೇತ ಪದ", "headingtext":"ನನ್ನ ಹೆಸರು ಶ್ರೀಧರ್", "submit":"ಲಾಗಿನ್","deviceList":"ನನ್ನ ಸಾಧನಗಳು","sectorList":"ನನ್ನ ವಲಯ","changeLanguage":"ಭಾಷೆ ಬದಲಾಯಿಸಿ","sectorDetail":"ವಲಯ ಮಾಹಿತಿ","automatic":"ಸ್ವಯಂಚಾಲಿತ","motor":"ನೀರಿನ ಮೋಟಾರ್","humidity":"ಪ್ರಸ್ತುತ ಆರ್ದ್ರತೆ","temperature":"ಪ್ರಸ್ತುತ ತಾಪಮಾನ","moisture":"ಪ್ರಸ್ತುತ ತೇವಾಂಶ","threshold":"ಪ್ರಸ್ತುತ ಥ್ರೆಶ್ಹೋಲ್ಡ್"}]}';


     //Function to make network call according to language on load
     var languageControls = function(language){
        if((language.toString() == "kn") || (language.toString().indexOf("kn") != -1)){
           languageSpecificURL = kanLanguageSpecificURL;
     	}else if((language.toString() == "Hn") || (language.toString().indexOf("Hn") != -1)){
           languageSpecificURL = hindiLanguageSpecificURL;
        }else if((language.toString() == "Tn") || (language.toString().indexOf("Tn") != -1)){
           languageSpecificURL = tamilLanguageSpecificURL;
         }else{
           languageSpecificURL = englishLanguageSpecificURL;
     	}
     	//onNetworkCall(languageSpecificURL,function(msg){
        	languageSpecificObject = JSON.parse(languageSpecificURL);
        	$(".languagespecificHTML").each(function(){
        	    $(this).html(languageSpecificObject.languageSpecifications[0][$(this).data("text")]);
     		});
     		$(".languageSpecificPlaceholder").each(function(){
     			$(this).attr("placeholder",languageSpecificObject.languageSpecifications[0][$(this).data("text")]);
     		});
            $(".languageSpecificValue").each(function(){
     			$(this).attr("value",languageSpecificObject.languageSpecifications[0][$(this).data("text")]);
     		});
     	//});*/
     };

     //Function to get specific value with unique key
     /*var getLanguageValue = function(key){
     	value = languageSpecificObject.languageSpecifications[0][key];
     	return value;
     };

     var onNetworkCall = function(urlToHit,successCallback){
         $.ajax({
            type: "POST",
            url: urlToHit,
            timeout: 30000 ,
            }).done(function( msg ) {
                successCallback(msg);
            }).fail(function(jqXHR, textStatus, errorThrown){
                alert("Internal Server Error");
            });
     }
     this.receivedEvent('deviceready');*/

     //screenTransition('../index.html')
 }