import {Component, Input, OnInit} from '@angular/core';
import {SimpleUser} from '../../../../../shared/dtos/user-dtos';

@Component({
  selector: 'app-script-members-list',
  templateUrl: './script-members-list.component.html',
  styleUrls: ['./script-members-list.component.scss']
})
export class ScriptMembersListComponent implements OnInit {
  @Input() members: SimpleUser[];
  @Input() isOwner: boolean;

  constructor() {
  }

  ngOnInit(): void {
  }

}
