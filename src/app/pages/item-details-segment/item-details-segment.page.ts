import { Component } from '@angular/core';
import { NavController } from '@ionic/angular';
import { ActivatedRoute } from '@angular/router';

import { SegmentService } from './../../services/segment-service';
import { ToastService } from 'src/app/services/toast-service';

@Component({
    templateUrl: 'item-details-segment.page.html',
    styleUrls: ['item-details-segment.page.scss'],
    providers: [SegmentService]

})
export class ItemDetailsSegmentPage {

    data = {};
    type: string;

    constructor(
        public navCtrl: NavController,
        private service: SegmentService,
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
        this.toastCtrl.presentToast('onItemClick:' + JSON.stringify(params));
    }

    onLike(item): void {

      if (item && item.like) {
          if (item.like.isActive) {
              item.like.isActive = false;
              item.like.number--;
          } else {
              item.like.isActive = true;
              item.like.number++;
          }
      }
        this.toastCtrl.presentToast('onLike:' + JSON.stringify(item));
    }

    onComment(item): void {
        if (item && item.comment) {
            if (item.comment.isActive) {
                item.comment.isActive = false;
                item.comment.number--;
            } else {
                item.comment.isActive = true;
                item.comment.number++;
            }
        }
        this.toastCtrl.presentToast('onComment:' + JSON.stringify(item));
    }
}
