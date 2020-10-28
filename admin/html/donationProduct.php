<!DOCTYPE html>
<html>
   <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <title>Donation</title>
      <link rel="icon" type="image/png" sizes="16x16" href="../plugins/images/etaranalogo.jpeg">
      <!-- Bootstrap Core CSS -->
      <link href="bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
      <!-- Bootstrap CSS CDN -->
      <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
      <!-- Our Custom CSS -->
      <link rel="stylesheet" href="css/donationStyle.css">
      <!-- Font Awesome JS -->
      <script defer src="https://use.fontawesome.com/releases/v5.0.13/js/solid.js" integrity="sha384-tzzSw1/Vo+0N5UhStP3bvwWPq+uvzCMfrN1fEFe+xBmv1C/AtVX5K0uZtmcHitFZ" crossorigin="anonymous"></script>
      <script defer src="https://use.fontawesome.com/releases/v5.0.13/js/fontawesome.js" integrity="sha384-6OIrr52G08NpOFSZdxxz1xdNSndlD4vdcf/q2myIUVO0VsqaGHJsB0RaBE01VTOY" crossorigin="anonymous"></script>
      <link href="css/datepicker.css" rel="stylesheet"/>
  <style>
.slider {
	width: 100%;
	margin: 2em auto;
	
}

.slider-wrapper {
	width: 100%;
	height: 200px;
	position: relative;
}

.slide {
	float: left;
	position: absolute;
	width: 100%;
	height: 100%;
	opacity: 0;
	transition: opacity 3s linear;
}

.slider-wrapper > .slide:first-child {
	opacity: 1;
}

</style>
            <style type="text/css">
                    /* -------------------------------------------------------------- Box Product -------------------------------------------------------------- */
                    .box-product-outer {
                    margin-bottom: 5px;
                    padding-top: 15px;
                    padding-bottom: 15px;
                    padding-left: 15px;
                    padding-right: 15px;
                    }
                    .box-product-outer:hover {
                    outline: 1px solid #aaa;
                    -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, .175);
                    box-shadow: 0 6px 12px rgba(0, 0, 0, .175)
                    }
                    .tab-content .box-product-outer { margin-bottom: 0 }
                    .box-product-slider-outer { padding: 10px }
                    .box-product .img-wrapper {
                    position: relative;
                    overflow: hidden
                    }
                    .box-product .img-wrapper > :first-child {
                    position: relative;
                    display: block
                    }
                    .box-product .img-wrapper > a > img { width: 100% }
                    .box-product .img-wrapper .tags {
                    position: absolute;
                    top: 0;
                    right: 0;
                    display: inline-block;
                    overflow: visible;
                    width: auto;
                    height: auto;
                    margin: 0;
                    padding: 0;
                    vertical-align: inherit;
                    border-width: 0;
                    background-color: transparent;
                    direction: rtl
                    }
                    .box-product .img-wrapper .tags-left {
                    left: 0;
                    direction: ltr
                    }
                    .box-product .img-wrapper .tags > .label-tags {
                    display: table;
                    margin: 1px 0 0 0;
                    text-align: left;
                    opacity: .92;
                    filter: alpha(opacity=92);
                    direction: ltr
                    }
                    .box-product .img-wrapper .tags > .label-tags:hover {
                    opacity: 1;
                    filter: alpha(opacity=100)
                    }
                    .box-product .img-wrapper .tags > .label-tags a:hover { text-decoration: none }
                    .box-product .img-wrapper > .option {
                    position: absolute;
                    top: auto;
                    right: 0;
                    bottom: -30px;
                    left: 0;
                    width: auto;
                    height: 28px;
                    -webkit-transition: all .2s ease;
                    -o-transition: all .2s ease;
                    transition: all .2s ease;
                    text-align: center;
                    vertical-align: middle;
                    background-color: rgba(0, 0, 0, .55)
                    }
                    .box-product .img-wrapper .option > a {
                    font-size: 18px;
                    font-weight: normal;
                    display: inline-block;
                    padding: 0 4px;
                    color: #fff
                    }
                    .box-product .img-wrapper:hover > .option {
                    top: auto;
                    bottom: 0
                    }
                    .box-product h6 a { line-height: 1.4 }
                    .price {
                    margin-bottom: 5px;
                    color: #f44336
                    }
                    /* 20. preloader */
            /* line 611, C:/Users/HP/Desktop/jun-2020/277.Charity _Non-profit/assets/scss/_common.scss */
            .preloader {
            background-color: #f7f7f7;
            width: 100%;
            height: 100%;
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            z-index: 999999;
            -webkit-transition: .6s;
            -o-transition: .6s;
            transition: .6s;
            margin: 0 auto;
            }

            /* line 627, C:/Users/HP/Desktop/jun-2020/277.Charity _Non-profit/assets/scss/_common.scss */
            .preloader .preloader-circle {
            width: 100px;
            height: 100px;
            position: relative;
            border-style: solid;
            border-width: 1px;
            border-top-color: #09cc7f;
            border-bottom-color: transparent;
            border-left-color: transparent;
            border-right-color: transparent;
            z-index: 10;
            border-radius: 50%;
            -webkit-box-shadow: 0 1px 5px 0 rgba(35, 181, 185, 0.15);
            box-shadow: 0 1px 5px 0 rgba(35, 181, 185, 0.15);
            background-color: #fff;
            -webkit-animation: zoom 2000ms infinite ease;
            animation: zoom 2000ms infinite ease;
            -webkit-transition: .6s;
            -o-transition: .6s;
            transition: .6s;
            }

            /* line 649, C:/Users/HP/Desktop/jun-2020/277.Charity _Non-profit/assets/scss/_common.scss */
            .preloader .preloader-circle2 {
            border-top-color: #0078ff;
            }

            /* line 652, C:/Users/HP/Desktop/jun-2020/277.Charity _Non-profit/assets/scss/_common.scss */
            .preloader .preloader-img {
            position: absolute;
            top: 50%;
            z-index: 200;
            left: 0;
            right: 0;
            margin: 0 auto;
            text-align: center;
            display: inline-block;
            -webkit-transform: translateY(-50%);
            -ms-transform: translateY(-50%);
            transform: translateY(-50%);
            padding-top: 6px;
            -webkit-transition: .6s;
            -o-transition: .6s;
            transition: .6s;
            }

            /* line 670, C:/Users/HP/Desktop/jun-2020/277.Charity _Non-profit/assets/scss/_common.scss */
            .preloader .preloader-img img {
            max-width: 55px;
            }

            /* line 673, C:/Users/HP/Desktop/jun-2020/277.Charity _Non-profit/assets/scss/_common.scss */
            .preloader .pere-text strong {
            font-weight: 800;
            color: #dca73a;
            text-transform: uppercase;
            }
                    /* -------------------------------------------------------------- End of Box Product -------------------------------------------------------------- */
                
                </style>
<style type="text/css">
         .card {
         padding: 1rem;
         }
         .cards {
         margin: 0 auto;
         display: grid;
         grid-gap: 3rem;
         }
         /* Screen larger than 600px? 2 column */
         @media (min-width: 600px) {
         .cards { grid-template-columns: repeat(2, 1fr); }
         }
         /* Screen larger than 900px? 3 columns */
         @media (min-width: 900px) {
         .cards { grid-template-columns: repeat(3, 1fr); }
         }
      </style>
      
   </head>
   <body onload="displayDonationStatus();">
      <div class="wrapper">
         <!-- Sidebar  -->
         <nav id="sidebar">
            <div class="sidebar-header">
               <img src="../plugins/images/etaranalogo.jpeg" alt="home" style="width: 50%; height: 50%;" class="light-logo" />
            </div>
            <hr>
            <ul  class="list-unstyled components" style="margin-top: -2em;" >
               <li >
                  <a href="" class="waves-effect"><i class="glyphicon glyphicon-heart-empty" aria-hidden="true"></i><label style="margin-left: 1em;">Category</label></a>
               </li>
               <li>
                  <a  class="waves-effect"><i class="glyphicon glyphicon-heart-empty" aria-hidden="true"></i><label style="margin-left: 1em;">Sub Category</label></a>
               </li>
               <li>
               </li>
               <li>
               </li>
               <li>
               </li>
               <li>
               </li>
               <li>
               </li>
            </ul>
         </nav>
         <!-- Page Content  -->
         <div id="content">
            <nav class="navbar navbar-expand-lg navbar-light bg-light">
               <div class="container-fluid" style="float:right;">
                  <button type="button" id="sidebarCollapse" class="btn btn-info">
                  <i class="fas fa-align-left"></i>
                  </button>
               </div>
            </nav>
           
            <div class="slider" id="main-slider" ><!-- outermost container element -->
	<div class="slider-wrapper"><!-- innermost wrapper element -->
		<img src="https://images.unsplash.com/photo-1505740420928-5e560c06d30e?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9" alt="First" class="slide" style="height:200px;" /><!-- slides -->
		<img src="https://www.gs1india.org/media/Juice_pack.jpg" alt="Second" class="slide" style="height:200px;" />
		<img src="https://img.freepik.com/free-vector/beauty-skin-care-background_52683-728.jpg?size=626&ext=jpg" alt="Third" class="slide" style="height:200px;" />
        </div>
    </div>	

    <div  id="displayProductList">
            </div>
         <!-- </div> -->
      </div>
      <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
      <!-- All Jquery -->
      <!-- ============================================================== -->
      <script src="../plugins/bower_components/jquery/dist/jquery.min.js"></script>
      <!-- Bootstrap Core JavaScript -->
      <script src="bootstrap/dist/js/bootstrap.min.js"></script>
      <!-- Menu Plugin JavaScript -->
      <script src="../plugins/bower_components/sidebar-nav/dist/sidebar-nav.min.js"></script>
      <!--slimscroll JavaScript -->
      <script src="js/jquery.slimscroll.js"></script>
      <!--Wave Effects -->
      <script src="js/waves.js"></script>
      <!--Counter js -->
      <script src="../plugins/bower_components/waypoints/lib/jquery.waypoints.js"></script>
      <script src="../plugins/bower_components/counterup/jquery.counterup.min.js"></script>
      <!-- chartist chart -->
      <script src="../plugins/bower_components/chartist-js/dist/chartist.min.js"></script>
      <script src="../plugins/bower_components/chartist-plugin-tooltip-master/dist/chartist-plugin-tooltip.min.js"></script>
      <!-- Sparkline chart JavaScript -->
      <script src="../plugins/bower_components/jquery-sparkline/jquery.sparkline.min.js"></script>
      <!-- Custom Theme JavaScript -->
      <script src="js/custom.min.js"></script>
      <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.js"></script>

      <script type="text/javascript">
         $(document).ready(function () {
             $('#sidebarCollapse').on('click', function () {
                 $('#sidebar').toggleClass('active');
             });
         });

         (function() {
	
	function Slideshow( element ) {
		this.el = document.querySelector( element );
		this.init();
	}
	
	Slideshow.prototype = {
		init: function() {
			this.wrapper = this.el.querySelector( ".slider-wrapper" );
			this.slides = this.el.querySelectorAll( ".slide" );
			this.previous = this.el.querySelector( ".slider-previous" );
			this.next = this.el.querySelector( ".slider-next" );
			this.index = 0;
			this.total = this.slides.length;
			this.timer = null;
			
			this.action();
			this.stopStart();	
		},
		_slideTo: function( slide ) {
			var currentSlide = this.slides[slide];
			currentSlide.style.opacity = 1;
			
			for( var i = 0; i < this.slides.length; i++ ) {
				var slide = this.slides[i];
				if( slide !== currentSlide ) {
					slide.style.opacity = 0;
				}
			}
		},
		action: function() {
			var self = this;
			self.timer = setInterval(function() {
				self.index++;
				if( self.index == self.slides.length ) {
					self.index = 0;
				}
				self._slideTo( self.index );
				
			}, 3000);
            },
            stopStart: function() {
                var self = this;
                self.el.addEventListener( "mouseover", function() {
                    clearInterval( self.timer );
                    self.timer = null;
                    
                }, false);
                self.el.addEventListener( "mouseout", function() {
                    self.action();
                    
                }, false);
            }
            
            
        };
        
        document.addEventListener( "DOMContentLoaded", function() {
            
            var slider = new Slideshow( "#main-slider" );
            
        });
        
        
    })();

      </script>
     <script>
          window.sessionStorage.setItem('baseUrl', "http://etaaranaservices.ortusolis.in:8081/ots/api/v18_1/");
          function displayDonationStatus() {
          
          var data = JSON.stringify({
            "requestData": {
                "searchKey": "category",
                "searchvalue": "1"
            }
          });
          
          $.ajax({
             type: "POST",
             url: window.sessionStorage.getItem('baseUrl') + "product/getProductList",
              data: data, // now data come in this
             contentType: "application/json; charset=utf-8",
             crossDomain: true,
             dataType: "json",
             success: function(data) {
                   if (data.responseCode === 200) {
                     if(data.responseData.productDetails.length !=0){
                         globeProdeList = data.responseData.productDetails;
                var productAppend = "<div class='cards isotope-grid' style='width:80%' >";
                         for(var i=0;i<globeProdeList.length;i++){
                            debugger
                        productAppend += ' <div class="grid-item">';
                        productAppend += '<div class="card box-product-outer">';
         productAppend += '<img class="card-img-top" src="'+globeProdeList[i].productImage+'"  width="80" height="300" id="productImage" ><br>';
                           productAppend+='<h6 class="card-title" id="displayProductName" style="color:blue;">'+globeProdeList[i].productName+'</h6>';
         productAppend+='<h6>Price:<span id="displayProductPrice" class="price product-card" data-price="'+globeProdeList[i].productPrice+' "> ₹'+globeProdeList[i].productPrice+'</span></h6>';
                           productAppend+='<h6> Base Price:<span id="displayRequestQuantity" class="price"> '+globeProdeList[i].productBasePrice+'</span></h6><br>';
                           productAppend+='<button type="button" class="btn btn-success btn-sm" onclick="editDonationProduct('+i+')">View</button>';
                           productAppend+='</div></div>';
         }
         productAppend += "</div>";
         $("#displayProductList").append(productAppend);
         
         
         
                     }
                 }
         
         
                     
             },
             error: function(jqXHR, status) {
                   // error handler
                   console.log(jqXHR);
                   $("body").addClass("enableDiv");
                   //swal({text: "Unable to connect ,please check your network",icon: "warning",button: "OK"});
             }
          });
          }
         </script>
      
   </body>
</html>