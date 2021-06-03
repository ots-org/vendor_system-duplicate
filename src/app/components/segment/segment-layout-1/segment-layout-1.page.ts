import { Component, Output, EventEmitter, Input, OnChanges, AfterViewInit } from '@angular/core';

@Component({
  selector: 'cs-segment-layout-1',
  templateUrl: 'segment-layout-1.page.html',
  styleUrls: ['segment-layout-1.page.scss'],
})
export class SegmentListLayout1Page implements OnChanges, AfterViewInit {
  @Input() data: any;
  @Output() onItemClick = new EventEmitter();

  selectedItem = 'Page1'
  viewEntered = false;

  constructor() { }

  ngAfterViewInit() {
    this.viewEntered = true;
  }

  onItemClickFunc(item) {
    if (event) {
      event.stopPropagation();
    }
    this.onItemClick.emit(item);
  }

  ngOnChanges(changes: { [propKey: string]: any }) {
    if (changes['data']) {
      this.data = changes['data'].currentValue;
    }
  }

  isEnabled(value: string): boolean {
    return this.selectedItem === value;
  }
}
