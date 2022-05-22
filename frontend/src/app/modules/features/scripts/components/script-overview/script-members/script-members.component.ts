import {Component, Input, OnInit} from '@angular/core';
import {SimpleUser} from '../../../../../shared/dtos/user-dtos';

@Component({
  selector: 'app-script-members',
  templateUrl: './script-members.component.html',
  styleUrls: ['./script-members.component.scss']
})
export class ScriptMembersComponent implements OnInit {
  @Input() members: SimpleUser[];

  constructor() { }

  ngOnInit(): void {
  }

}
