import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ScriptService } from '../../../../core/services/script/script.service';

@Component({
  selector: 'app-script-list',
  templateUrl: './script-list.component.html',
  styleUrls: ['./script-list.component.scss']
})
export class ScriptListComponent implements OnInit {
  @Input() title: string;
  @Input() hasUploadButton = false;

  constructor(public service: ScriptService, private router: Router) {}

  ngOnInit(): void {
    this.service.getAll();
  }
}
