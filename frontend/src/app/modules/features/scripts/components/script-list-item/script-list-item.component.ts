import { Component, Input, OnInit } from '@angular/core';
import { ScriptPreview } from '../../../../shared/dtos/script-dtos';

@Component({
  selector: 'app-script-list-item',
  templateUrl: './script-list-item.component.html',
  styleUrls: ['./script-list-item.component.scss']
})
export class ScriptListItemComponent implements OnInit {
  @Input() script: ScriptPreview;

  constructor() {}

  ngOnInit(): void {}
}
