import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AlertComponent } from './components/alert/alert.component';
import { PageLayoutComponent } from './components/page-layout/page-layout.component';
import { CardComponent } from './components/card/card.component';
import { ButtonComponent } from './components/button/button.component';
import {
  ArrowLeft,
  ArrowRight,
  CaretLeftFill,
  CaretRightFill,
  MicFill,
  MicMuteFill,
  PersonCircle,
  PlayFill,
  ThreeDots,
  VolumeMuteFill,
  VolumeOffFill,
  VolumeUpFill,
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
import { SectionComponent } from './components/section/section.component';

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
  XLg,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  MicFill,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  ArrowLeft,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  ArrowRight,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  MicMuteFill,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  VolumeUpFill,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  VolumeOffFill,
  // eslint-disable-next-line @typescript-eslint/naming-convention
  VolumeMuteFill
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
  ControlsItemComponent,
  ModalComponent,
  SectionComponent
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

/**
 * Shared module that exports all kinds of shared parts.
 *
 * @see https://reset.inso.tuwien.ac.at/repo/2022ss-sepm-pr-group/22ss-sepm-pr-qse-14/-/wikis/Frontend-Architektur
 */
@NgModule({
  declarations: [...sharedDeclarations],
  imports: [...sharedImports, BootstrapIconsModule.pick(icons)],
  exports: [...sharedImports, ...sharedDeclarations, BootstrapIconsModule]
})
export class SharedModule {}
