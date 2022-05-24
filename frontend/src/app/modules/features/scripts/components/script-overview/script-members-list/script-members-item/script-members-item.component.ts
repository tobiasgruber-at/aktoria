import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {SimpleUser} from '../../../../../../shared/dtos/user-dtos';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Theme} from '../../../../../../shared/enums/theme.enum';
import {AuthService} from '../../../../../../core/services/auth/auth-service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-script-members-item',
  templateUrl: './script-members-item.component.html',
  styleUrls: ['./script-members-item.component.scss']
})
export class ScriptMembersItemComponent implements OnInit {
  @Input() member: SimpleUser;
  @Input() isOwner: boolean;
  scriptId;

  readonly theme = Theme;
  deleteLoading = false;

  constructor(private modalService: NgbModal,
              private authService: AuthService,
              private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    this.scriptId = this.route.snapshot.paramMap.get('id');
  }

  openModal(modal: TemplateRef<any>) {
    if (this.isOwner) {
      this.modalService.open(modal, { centered: true });
    }
  }

  removeMember(modal: NgbActiveModal) {
    console.log('removing member with id: ' + this.member.id);
    modal.dismiss();
  }
}
