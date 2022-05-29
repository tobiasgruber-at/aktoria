import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AlertComponent } from './components/alert/alert.component';
import { PageLayoutComponent } from './components/page-layout/page-layout.component';
import { CardComponent } from './components/card/card.component';
import { ButtonComponent } from './components/button/button.component';
import {
  CaretLeftFill,
  CaretRightFill,
  PersonCircle,
  PlayFill,
  ThreeDots,
  XLg
} from 'ng-bootstrap-icons/icons';
import { BootstrapIconsModule } from 'ng-bootstrap-icons';
import { FormErrorComponent } from './components/form-errors/form-error/form-error.component';
import { FormErrorsComponent } from './components/form-errors/form-errors.component';
import { ModalComponent } from './components/modal/modal.component';
import {
  NgbCollapseModule,
  NgbDropdownModule,
  NgbTooltipModule
} from '@ng-bootstrap/ng-bootstrap';
import { BackButtonComponent } from './components/back-button/back-button.component';
import { ControlsComponent } from './components/controls/controls.component';
import { ControlsItemComponent } from './components/controls/controls-item/controls-item.component';

const icons = {
  // eslint-disable-next-line @typescript-eslint/naming-convention
  PersonCircle,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  ThreeDots,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  PlayFill,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  CaretLeftFill,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  CaretRightFill,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  XLg
};
const sharedDeclarations = [
  PageLayoutComponent,
  AlertComponent,
  CardComponent,
  ButtonComponent,
  FormErrorComponent,
  FormErrorsComponent,
  ModalComponent,
  BackButtonComponent,
  ControlsComponent,
  ControlsItemComponent
];
const sharedImports = [
  CommonModule,
  FormsModule,
  ReactiveFormsModule,
  RouterModule,
  NgbCollapseModule,
  NgbDropdownModule,
  NgbTooltipModule
];

@NgModule({
  declarations: [...sharedDeclarations],
  imports: [...sharedImports, BootstrapIconsModule.pick(icons)],
  exports: [...sharedImports, ...sharedDeclarations, BootstrapIconsModule]
})
export class SharedModule {}
