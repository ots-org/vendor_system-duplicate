import { Component, Output, EventEmitter, Input, OnChanges } from '@angular/core';

@Component({
  selector: 'cs-animation-layout-10',
  templateUrl: 'animation-layout-10.page.html',
  styleUrls: ['animation-layout-10.page.scss'],
})
export class AnimationLayout10Page implements OnChanges {
  @Input() data: any;

  @Output() onItemClick = new EventEmitter();
  @Output() onFavorite = new EventEmitter();
  @Output() onLike = new EventEmitter();
  @Output() onShare = new EventEmitter();

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
  onLikeFunc(item) {
    if (event) {
      event.stopPropagation();
    }
    this.onLike.emit(item);
  }

  onFavoriteFunc(item) {
    if (event) {
      event.stopPropagation();
    }
    this.onFavorite.emit(item);
  }

  onShareFunc(item) {
    if (event) {
      event.stopPropagation();
    }
    this.onShare.emit(item);
  }
}
