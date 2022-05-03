import { Component } from '@angular/core';
import { ToastService } from '../../services/toast.service';
import { appearAnimations } from '../../../shared/animations/appear-animations';

/** @author Tobias Gruber */
@Component({
  selector: 'app-toasts',
  templateUrl: './toasts.component.html',
  styleUrls: ['./toasts.component.scss'],
  animations: [appearAnimations]
})
export class ToastsComponent {
  constructor(public toastService: ToastService) {}
}
