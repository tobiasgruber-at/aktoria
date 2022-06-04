import { NgModule } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
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
  imports: [...sharedImports],
  exports: [...sharedImports]
})
export class SharedTestingModule {}
