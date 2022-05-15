import {Component, Input, OnInit} from '@angular/core';
import {ScriptService} from '../../../../core/services/script/script.service';

@Component({
  selector: 'app-script-list',
  templateUrl: './script-list.component.html',
  styleUrls: ['./script-list.component.scss']
})
export class ScriptListComponent implements OnInit {
  @Input() title: string;
  @Input() hasUploadButton = false;

  constructor(public scriptService: ScriptService) {
  }

  ngOnInit(): void {
    this.scriptService.getAll().subscribe();
  }
}
