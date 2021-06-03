import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { HometestPage } from './hometest.page';

describe('HometestPage', () => {
  let component: HometestPage;
  let fixture: ComponentFixture<HometestPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HometestPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(HometestPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
