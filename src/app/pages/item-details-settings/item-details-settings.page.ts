import { Component } from '@angular/core';

@Component({
    templateUrl: 'item-details-settings.html',
    styleUrls: ['item-details-settings.page.scss']
})
export class ItemDetailsSettingsPage {

    isRTLEnabled:Boolean = false;

    constructor() {
        this.isRTLEnabled = localStorage.getItem('isEnabledRTL') == "true"
    }

    changeTheme(name) {
        if (name) {
          document.body.removeAttribute("class");
          document.body.classList.add(name);
        }
    }

    ionChangeRTL(e){
        localStorage.setItem('isEnabledRTL', "" + this.isRTLEnabled)

        document.getElementsByTagName('ion-menu')[0]
                .setAttribute('side', this.isRTLEnabled  ? 'end': 'start');
        document.getElementsByTagName('html')[0]
                .setAttribute('dir', this.isRTLEnabled  ? 'rtl': 'ltr');

    }
}
