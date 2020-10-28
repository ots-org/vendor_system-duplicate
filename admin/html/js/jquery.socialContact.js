/*!
 * jquery.c-share.js v1.1.0
 * https://github.com/ycs77/jquery-plugin-c-share
 *
 * Copyright 2019 Lucas, Yang
 * Released under the MIT license
 *
 * Date: 2019-04-01T15:15:25.982Z
 */

(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory() :
    typeof define === 'function' && define.amd ? define(factory) :
    (factory());
  }(this, (function () { 'use strict';
  
    if ($.fn) {
  
      $.fn.socialContact = function (options) {
        var _this = this;
  
        var defaults = {
          description: '',
          showButtons: ['email', 'watsapp'],
          data: {
            email: {
              fa: 'fas fa-envelope',
              name: 'E-mail',
              href: function href(url,description) {
                return 'https://mail.google.com/mail/u/0/?view=cm&fs=1&tf=1&to=+etaarana_support@ortusolis.in&su=+Feedback&body='+ description + ':'+'&fs=1&tf=1' ;
              },
              show: false
            },
            watsapp: {
              fa: 'fab fa-whatsapp',
              name: 'whatsApp',
              href: function href(url, description) {
                return 'whatsapp://send?phone=' + 919008011333  + '&text=' + description ;
            },
              show: false
            }
          },
          spacing: 6,
          shareToText: 'Share to'
        };
  
        var href = location.href.replace(/#\w/, '');
        var mobile = navigator.userAgent.match(/(mobile|android|pad)/i);
  
        var settings = $.extend({}, defaults, options);
        if (options) {
          settings.data = $.extend({}, defaults.data, options.data);
        }
  
        settings.showButtons.forEach(function (shareName) {
  
          var item = settings.data[shareName];
  
          // Create button element
          _this.append('\n        <a href="' + item.href.call(null, href, settings.description) + '" title="' + settings.shareToText + ' ' + item.name + '" target="_blank" data-icon="' + shareName + '">\n          <span class="fa-stack">\n            ' + (!item.hideWrapper ? '<i class="fas fa-circle fa-stack-2x"></i>' : '') + '\n            <i class="' + item.fa + ' fa-stack-1x"></i>\n          </span>\n        </a>\n      ');
        });
  
        this.find('.fa-plurk').text('P');
  
        // Bind link click event
        this.find('a').click(function (e) {
          if (!mobile) {
            e.preventDefault();
            window.open($(this).attr('href'), '_blank', 'height=600,width=500');
          }
        });
  
        // Add CSS
        this.children('a').css({
          'display': 'inline-block',
          'margin': 'auto ' + Number(settings.spacing) / 2 + 'px',
          'margin-left': '2em',
          'text-decoration': 'none',
          '-webkit-transition': 'all .2s',
          '-moz-transition': 'all .2s',
          'transition': 'all .2s'
        });
        if (!mobile) {
          this.children('a').hover(function () {
            $(this).css({
              '-webkit-transform': 'translateY(-4px)',
              '-ms-transform': 'translateY(-4px)',
              'transform': 'translateY(-4px)'
            });
          }, function () {
            $(this).css({
              '-webkit-transform': 'translateY(0px)',
              '-ms-transform': 'translateY(0px)',
              'transform': 'translateY(0px)'
            });
          });
        }
  
        // Set color
        this.find('.fa-stack-1x').css('color', '#ffffff');
        this.find('[data-icon=fb] .fa-stack-2x').css('color', '#3B5998');
        this.find('[data-icon=gPlus] .fa-stack-2x').css('color', '#d73d32');
        this.find('[data-icon=line] .fa-stack-1x').css('color', '#00c300');
        this.find('[data-icon=plurk] .fa-stack-2x').css('color', '#cf682f');
        this.find('[data-icon=plurk] .fa-plurk').css({
          'font-family': 'arial',
          'font-style': 'normal',
          'font-weight': 'bold'
        });
        this.find('[data-icon=weibo] .fa-stack-2x').css('color', '#F5CA59');
        this.find('[data-icon=twitter] .fa-stack-2x').css('color', '#2ba9e1');
        this.find('[data-icon=tumblr] .fa-stack-2x').css('color', '#35465d');
        this.find('[data-icon=pinterest] .fa-stack-2x').css('color', '#EA1514');
        this.find('[data-icon=email] .fa-stack-2x').css('color', '#FF0000');
        this.find('[data-icon=watsapp] .fa-stack-2x').css('color', '#25D366');
        this.find('[data-icon=watsapp1] .fa-stack-2x').css('color', '#25D366');
  
        return this;
      };
    }
  
  })));
  