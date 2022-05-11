import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-script',
  templateUrl: './script.component.html',
  styleUrls: ['./script.component.scss']
})
export class ScriptComponent implements OnInit {
  @Input() title: string;

  constructor() {}

  ngOnInit(): void {}
}
