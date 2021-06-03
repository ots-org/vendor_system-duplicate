import { Component, OnInit } from '@angular/core';
import { AppSettings } from './../../services/app-settings';
import { HomeService } from './../../services/home-service';
import { ModalController } from '@ionic/angular';
import { IntroPage } from '../intro-page/intro-page.page';


@Component({
  selector: 'app-home-test',
  templateUrl: './home-test.page.html',
  styleUrls: ['./home-test.page.scss'],
  providers: [HomeService],
})
export class HomeTestPage implements OnInit {
  
  ngOnInit() {
  
  }
  
  item = {
    toolbarTitle :"",
    background :"",
    title:"",
    subtitle:"",
    subtitle2:"",
    link:"",
    description:""
  }
  
  constructor(
    private homeService:HomeService, 
    public modalController: ModalController) { 
      this.item = this.homeService.getData();
      let showWizard = localStorage.getItem("SHOW_START_WIZARD");

      if (AppSettings.SHOW_START_WIZARD && !showWizard) {
        this.openModal()
      }
  }

  async openModal() {
    let modal = await this.modalController.create({component: IntroPage});
     return await modal.present();
  }

}
