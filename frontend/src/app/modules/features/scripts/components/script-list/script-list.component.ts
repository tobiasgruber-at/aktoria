import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SimpleScript } from '../../../../shared/dtos/script-dtos';
import { ScriptService } from '../../../../core/services/script/script.service';

@Component({
  selector: 'app-script-list',
  templateUrl: './script-list.component.html',
  styleUrls: ['./script-list.component.scss']
})
export class ScriptListComponent implements OnInit {
  scripts: SimpleScript[];
  test1: SimpleScript;

  constructor(private service: ScriptService, private router: Router) {}

  ngOnInit(): void {
    this.getAllScripts();
  }

  getAllScripts() {
    /* TODO: this.service.getAll().subscribe({
       next: (data) => {
         this.scripts = data;
       }
     });*/
    this.scripts = [
      this.test1,
      this.test1,
      this.test1,
      this.test1,
      this.test1,
      this.test1,
      this.test1,
      this.test1,
      this.test1
    ];
  }
}
