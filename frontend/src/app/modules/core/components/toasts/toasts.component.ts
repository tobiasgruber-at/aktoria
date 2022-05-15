import { Component } from '@angular/core';
import { ToastService } from '../../services/toast/toast.service';
import { lightAppearAnimations } from '../../../shared/animations/light-appear.animations';

/** @author Tobias Gruber */
@Component({
  selector: 'app-toasts',
  templateUrl: './toasts.component.html',
  styleUrls: ['./toasts.component.scss'],
  animations: [lightAppearAnimations]
})
export class ToastsComponent {
  constructor(public toastService: ToastService) {}
}
