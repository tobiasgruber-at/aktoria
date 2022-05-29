import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { ScriptService } from '../../../../core/services/script/script.service';

@Component({
  selector: 'app-script-invite-accept',
  templateUrl: './script-invite-accept.component.html',
  styleUrls: ['./script-invite-accept.component.scss']
})
export class ScriptInviteAcceptComponent implements OnInit {
  scriptId: string;
  token: string;
  successful: boolean = null;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private scriptService: ScriptService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.scriptId = this.route.snapshot.paramMap.get('id');
    this.token = this.route.snapshot.paramMap.get('token');

    this.scriptService.addParticipant(this.token, this.scriptId).subscribe({
      next: () => {
        this.successful = true;
      },
      error: (err) => {
        this.successful = false;
      }
    });
  }
}
