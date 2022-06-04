import { NgModule } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { UserService } from '../core/services/user/user-service';
import { UserMockService } from '../core/services/user/user-mock.service';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

const sharedImports = [
  HttpClientTestingModule,
  RouterTestingModule,
  ReactiveFormsModule,
  BrowserAnimationsModule
];

@NgModule({
  declarations: [],
  providers: [{ provide: UserService, useClass: UserMockService }],
  imports: [...sharedImports],
  exports: [...sharedImports]
})
export class SharedTestingModule {}
