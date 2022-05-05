import {Component, Input, OnInit} from '@angular/core';
import {Toast} from '../../../../shared/interfaces/toast.interface';
import {ToastService} from '../../../services/toast/toast.service';

/** @author Tobias Gruber */
@Component({
  selector: 'app-toasts-item',
  templateUrl: './toasts-item.component.html',
  styleUrls: ['./toasts-item.component.scss']
})
export class ToastsItemComponent implements OnInit, OnInit {
  @Input() toast: Toast;
  private autoCloseTimeout;

  constructor(private toastService: ToastService) {}

  ngOnInit(): void {
    this.autoCloseTimeout = setTimeout(() => {
      this.close(this.toast);
    }, 5000);
  }

  close(toast: Toast): void {
    clearTimeout(this.autoCloseTimeout);
    this.toastService.close(toast);
  }
}
