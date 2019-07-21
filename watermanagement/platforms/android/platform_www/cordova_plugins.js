cordova.define('cordova/plugin_list', function(require, exports, module) {
  module.exports = [
    {
      "id": "cordova-plugin-globalization.GlobalizationError",
      "file": "plugins/cordova-plugin-globalization/www/GlobalizationError.js",
      "pluginId": "cordova-plugin-globalization",
      "clobbers": [
        "window.GlobalizationError"
      ]
    },
    {
      "id": "cordova-plugin-globalization.globalization",
      "file": "plugins/cordova-plugin-globalization/www/globalization.js",
      "pluginId": "cordova-plugin-globalization",
      "clobbers": [
        "navigator.globalization"
      ]
    }
  ];
  module.exports.metadata = {
    "cordova-plugin-whitelist": "1.3.3",
    "cordova-plugin-globalization": "1.11.0"
  };
});