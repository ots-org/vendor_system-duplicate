import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { HomeTestPage } from './home-test.page';

describe('HomeTestPage', () => {
  let component: HomeTestPage;
  let fixture: ComponentFixture<HomeTestPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeTestPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(HomeTestPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
