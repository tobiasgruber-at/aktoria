import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AlertComponent } from './components/alert/alert.component';
import { PageLayoutComponent } from './components/page-layout/page-layout.component';
import { CardComponent } from './components/card/card.component';
import { ButtonComponent } from './components/button/button.component';
import { PersonCircle } from 'ng-bootstrap-icons/icons';
import { BootstrapIconsModule } from 'ng-bootstrap-icons';
import { FormErrorComponent } from './components/form-errors/form-error/form-error.component';
import { FormErrorsComponent } from './components/form-errors/form-errors.component';
import { ModalComponent } from './components/modal/modal.component';

const icons = {
  // eslint-disable-next-line @typescript-eslint/naming-convention
  PersonCircle
};
const sharedDeclarations = [
  PageLayoutComponent,
  AlertComponent,
  CardComponent,
  ButtonComponent,
  FormErrorComponent,
  FormErrorsComponent,
  ModalComponent
];
const sharedImports = [
  CommonModule,
  FormsModule,
  ReactiveFormsModule,
  RouterModule
];

@NgModule({
  declarations: [...sharedDeclarations],
  imports: [...sharedImports, BootstrapIconsModule.pick(icons)],
  exports: [...sharedImports, ...sharedDeclarations, BootstrapIconsModule]
})
export class SharedModule {}
