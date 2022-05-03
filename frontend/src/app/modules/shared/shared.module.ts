import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AlertComponent } from './components/alert/alert.component';
import { PageLayoutComponent } from './components/page-layout/page-layout.component';

const sharedDeclarations = [PageLayoutComponent, AlertComponent];
const sharedImports = [
  CommonModule,
  FormsModule,
  RouterModule,
  ReactiveFormsModule
];

@NgModule({
  declarations: sharedDeclarations,
  imports: sharedImports,
  exports: [...sharedImports, ...sharedDeclarations]
})
export class SharedModule {}
