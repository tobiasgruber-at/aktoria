import {Component, EventEmitter, Input, OnInit, Output, TemplateRef} from '@angular/core';
import { SimpleUser } from '../../../../../../shared/dtos/user-dtos';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Theme } from '../../../../../../shared/enums/theme.enum';
import { AuthService } from '../../../../../../core/services/auth/auth.service';
import { ActivatedRoute } from '@angular/router';
import { ScriptService } from '../../../../../../core/services/script/script.service';

@Component({
  selector: 'app-script-members-item',
  templateUrl: './script-members-item.component.html',
  styleUrls: ['./script-members-item.component.scss']
})
export class ScriptMembersItemComponent implements OnInit {
  @Input() member: SimpleUser;
  @Input() isOwner: boolean;
  @Input() owner: SimpleUser;
  @Output() removedEmitter = new EventEmitter<SimpleUser>();
  scriptId;

  readonly theme = Theme;
  deleteLoading = false;

  constructor(
    private modalService: NgbModal,
    private authService: AuthService,
    private route: ActivatedRoute,
    private scriptService: ScriptService
  ) {}

  ngOnInit(): void {
    this.scriptId = this.route.snapshot.paramMap.get('id');
  }

  openModal(modal: TemplateRef<any>) {
    if (this.isOwner && !this.isMe()) {
      this.modalService.open(modal, { centered: true });
    }
  }

  removeMember(modal: NgbActiveModal) {
    this.scriptService
      .removeParticipant(this.scriptId, this.member.email)
      .subscribe({
        next: () => {
          this.removedEmitter.emit(this.member);
          modal.dismiss();
        },
        error: (err) => {
          this.deleteLoading = false;
        }
      });
  }

  isMe() {
    return this.member.email === this.authService.getEmail();
  }
}
