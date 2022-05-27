import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastService} from '../../../../core/services/toast/toast.service';
import {Theme} from '../../../../shared/enums/theme.enum';
import {FormBase} from '../../../../shared/classes/form-base';
import {ScriptService} from '../../../../core/services/script/script.service';

@Component({
  selector: 'app-script-invite',
  templateUrl: './script-invite.component.html',
  styleUrls: ['./script-invite.component.scss']
})
export class ScriptInviteComponent implements OnInit {

  scriptId: string;

  constructor() {
  }

  ngOnInit(): void {
  }

}
