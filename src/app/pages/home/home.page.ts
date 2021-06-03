import { AppSettings } from './../../services/app-settings';
import { Component } from '@angular/core';
import { HomeService } from './../../services/home-service';
import { ModalController } from '@ionic/angular';
import { IntroPage } from '../intro-page/intro-page.page';
import { ScrollSegmentService } from '../../services/scroll-segment-service';
import { ToastService } from 'src/app/services/toast-service';
import { ProductService } from '../../services/api/product.service'
import { products } from '../../model/product'
@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  providers: [HomeService]
})
export class HomePage {

  productListItemPage2 = {
    "id": 2,
    "name": "Caleigh Jerde",
    "productName":"saree",
    "time": "18  2018",
    "image":"",
    "description": "could learn any language, what would you choose?",
    "iconsStars": [
      {
        "iconStar": "star",
      },
      {
        "iconStar": "star",
      },
      {
        "iconStar": "star-outline",
      },
      {
        "iconStar": "star-outline",
      },
      {
        "iconStar": "star-outline",
      }
    ],
  };
  productsList : products[];

  type = "";
  constructor(
    private homeService:HomeService, 
    public modalController: ModalController,
    private scrollSegmentService:ScrollSegmentService,
    private toastCtrl:ToastService ,
    private productService: ProductService) {
      this.type = "3";
      this.scrollSegmentService.load(scrollSegmentService.getAllThemes()[this.type]).subscribe(d => {
        this.getProductList();
        this.getALLCategory();
      }); 
      let showWizard = localStorage.getItem("SHOW_START_WIZARD");

      if (AppSettings.SHOW_START_WIZARD && !showWizard) {
        this.openModal()
      }
  }

  async openModal() {
    let modal = await this.modalController.create({component: IntroPage});
     return await modal.present();
  }

  onItemClick(params) {
    this.toastCtrl.presentToast('onItemClick:' + JSON.stringify(params));
  }

  onFollowClick(params) {
    this.toastCtrl.presentToast('onFollowClick');
  }

  onMessageClick(params) {
    this.toastCtrl.presentToast('onMessageClick');
  }
  
  onCategoryClick(params){
    this.toastCtrl.presentToast('onCategoryClick');
    this.getProductListBasedOnCategory(params);
  }

  getProductList(){
    var request = {
      "requestData": {
        "searchKey": "All",
        "status": "active"
      }
    };

    this.
      productService
      .getAllProductList(request)
      .subscribe(res =>{
          if(JSON.stringify(JSON.parse(JSON.stringify(res)).responseCode)=="200"){
          this.productsList = JSON.parse(JSON.stringify(res)).responseData.productDetails;
          this.data.page2.items = [];
          this.data = this.convertProductResponseToModel();
          return this.data;
          }else{
            alert("some thing went wrong");
          }
          (error) => {
            console.log(error.error);
            alert("some thing went wrong");
          }
        })
    }


  getProductListBasedOnCategory(params){
    console.log(params);
    var request = {
      "requestData":{
         "searchKey":"pagination",
         "searchvalue":params.id,
         "status":"active",
         "productLevel":"3",
         "size":"1000",
         "startOn":"0",
         "procedureKey":"category"
      }
   }

    this.
    productService
    .getAllProductList(request)
    .subscribe(res =>{
        if(JSON.stringify(JSON.parse(JSON.stringify(res)).responseCode)=="200"){
        this.productsList = JSON.parse(JSON.stringify(res)).responseData.productDetails;
        console.log(this.productsList);
        this.data = this.convertProductResponseToModel();
        }else{
          alert("some thing went wrong");
        }
        (error) => {
          console.log(error.error);
          alert("some thing went wrong");
        }
      })

  }

  data = {
    "button":"add to cart",
    'toolbarTitle': 'E-taarana',
    "category":[],
    "segmentHeader": [
      { "text": "Products", "icon": "reader" },
      { "text": "Commnets", "icon": "chatbox-ellipses" },
      { "text": "Profile", "icon": "person-circle" }
    ],
    // Data Page 1
    "page1": {
      'toolbarTitle': 'Profile With Big Avatar',
      'image': 'assets/imgs/avatar/24.jpg',
      'title': 'Claire Stewart',
      'subtitle': 'Extreme coffee lover. Twitter maven. Internet practitioner. Beeraholic.',
      'buttonFollow': 'Follow',
      'buttonMessage': 'Message',
      'category': 'Favorite',
      'image1': 'assets/imgs/background/21.jpg',
      'image2': 'assets/imgs/background/3.jpg',
      'image3': 'assets/imgs/background/29.jpg',
    },

    // Data Page 2
    "page2": {
      'allComments': 'List of Products',
      'items': [
          
      ]
    },
    // Data Page 3
    "page3": {
      "rating": "4.5",
      "iconsStars": [
        {
          "iconStar": "star",
        },
        {
          "iconStar": "star",
        },
        {
          "iconStar": "star",
        },
        {
          "iconStar": "star",
        },
        {
          "iconStar": "star-outline",
        }
      ],
      "items": [
        {
          "id": 1,
          "title": "Erica Romaguera",
          "time": "18 August 2018",
          "description": "If you could have any kind of pet, what would you choose?",
          "iconsStars": [
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star-outline",
            },
            {
              "iconStar": "star-outline",
            }
          ],
        },
        {
          "id": 2,
          "title": "Caleigh Jerde",
          "time": "18 August 2018",
          "description": "If you could learn any language, what would you choose?",
          "iconsStars": [
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star-outline",
            },
            {
              "iconStar": "star-outline",
            },
            {
              "iconStar": "star-outline",
            }
          ],
        },
        {
          "id": 3,
          "title": "Lucas Schultz",
          "time": "18 August 2018",
          "description": "Life is about making an impact, not making an income.",
          "iconsStars": [
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star-outline",
            }
          ],
        },
        {
          "id": 4,
          "title": "Carole Marvin",
          "time": "18 August 2018",
          "description": "I’ve learned that people will forget what you said, people will forget what you did, but people will never forget how you made them feel",
          "iconsStars": [
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            }
          ],
        },
        {
          "id": 5,
          "title": "Doriana Feeney",
          "time": "18 August 2018",
          "description": "Definiteness of purpose is the starting point of all achievement.",
          "iconsStars": [
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star-outline",
            }
          ],
        },
        {
          "id": 6,
          "title": "Nia Gutkowski",
          "time": "18 August 2018",
          "description": "Life is what happens to you while you’re busy making other plans",
          "iconsStars": [
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star",
            },
            {
              "iconStar": "star-outline",
            }
          ],
        }
      ]
    },
  };

  convertProductResponseToModel(){
    this.data.page2.items = [];
    for(var i=0;i<this.productsList.length;i++){

      var localProductListItemPage2 = {
        "id": 0,
        "name": "",
        "productName":"",
        "time": "",
        "image":"",
        "description": "",
        "iconsStars": [
          {
            "iconStar": "star",
          }
        ],
      };
      localProductListItemPage2.id = this.productsList[i].productId;
      localProductListItemPage2.productName = this.productsList[i].productName;
      localProductListItemPage2.image = this.productsList[i].productImage;
      localProductListItemPage2.description = this.productsList[i].productDescription;
      this.data.page2.items.push(localProductListItemPage2);
    }
    console.table(this.data); 
    return this.data;
  }
  
  //----------------------------GET CATEGORY DETAILS-----------------------------------------
  getALLCategory(){
    var request = {
      "requestData" : {
        "searchKey" : 'category',
        "searchvalue" : '1'
      }
   }
   
   this.
      productService
      .getAllProductList(request)
      .subscribe(res =>{
          if(JSON.stringify(JSON.parse(JSON.stringify(res)).responseCode)=="200"){
          this.productsList = JSON.parse(JSON.stringify(res)).responseData.productDetails;
          this.data.category = this.convertProductListResponseToModelList();
          console.log(this.productsList);
          }else{
            alert("some thing went wrong");
          }
          (error) => {
            console.log(error.error);
            alert("some thing went wrong");
          }
        })
  }


  items = [];
  convertProductListResponseToModelList(){
    for(var i=0;i<10;i++){

      var localProductListItemPage2 = {
        "id": 0,
        "name": "",
        "productName":"",
        "time": "",
        "image":"",
        "description": "",
        "iconsStars": [
          {
            "iconStar": "star",
          }
        ],
      };

      localProductListItemPage2.id = this.productsList[i].productId;
      localProductListItemPage2.productName = this.productsList[i].productName;
      localProductListItemPage2.image = this.productsList[i].productImage;
      localProductListItemPage2.description = this.productsList[i].productDescription;
      this.items.push(localProductListItemPage2);
    }
    return this.items;
  }

}
