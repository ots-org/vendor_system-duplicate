import { Component, Output, EventEmitter, Input, OnChanges } from '@angular/core';

@Component({
  selector: 'cs-check-box-layout-1',
  templateUrl: 'check-box-layout-1.page.html',
  styleUrls: ['check-box-layout-1.page.scss'],
})
export class CheckBoxLayout1Page implements OnChanges {
  @Input() data: any;

  @Output() onItemClick = new EventEmitter();

  constructor() { }

  ngOnChanges(changes: { [propKey: string]: any }) {
    this.data = changes['data'].currentValue;
  }

  onItemClickFunc(item): void {
    if (event) {
      event.stopPropagation();
    }
    this.onItemClick.emit(item);
  }
}
