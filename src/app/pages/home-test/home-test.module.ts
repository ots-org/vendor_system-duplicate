import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RouterModule } from '@angular/router';
import { HomeTestPage } from './home-test.page';
import { SharedModule } from './../../components/shared.module';
import { IntroPage } from './../intro-page/intro-page.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    // IntroPageModule,
    SharedModule,
    RouterModule.forChild([
      {
        path: '',
        component: HomeTestPage
      }
    ])
  ],
  declarations: [IntroPage], 
  entryComponents: [IntroPage],
  exports: [IntroPage],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HomeTestPageModule {}
