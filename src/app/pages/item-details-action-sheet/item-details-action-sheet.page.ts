import { Component } from '@angular/core';
import { NavController } from '@ionic/angular';
import { ActivatedRoute } from '@angular/router';

import { ActionSheetService } from './../../services/action-sheet-service';
import { ToastService } from 'src/app/services/toast-service';

@Component({
    templateUrl: 'item-details-action-sheet.page.html',
    styleUrls: ['item-details-action-sheet.page.scss'],
    providers: [ActionSheetService]
})
export class ItemDetailsActionSheetPage {

    data = {};
    type: string;
    isHeaderActive: Boolean = false;

    constructor(
        public navCtrl: NavController,
        private service: ActionSheetService,
        private toastCtrl: ToastService,
        private route: ActivatedRoute) {
        this.type = this.route.snapshot.paramMap.get('type');
        this.service.load(service.getAllThemes()[this.type]).subscribe(d => {
            this.data = d;
        });
    }

    isType(item) {
        return item === parseInt(this.type, 10);
    }

    // events
    onItemClick(params): void {
        this.toastCtrl.presentToast('onItemClick');
    }

    onItemClickActionSheet(params): void {
        this.toastCtrl.presentToast('onItemClickActionSheet');
    }

    onProceed(params): void {
        this.toastCtrl.presentToast('onProceed');
    }

    onChangeScroll(isHeaderActive): void {
        this.isHeaderActive = isHeaderActive;
    }
}
