import { Component, HostBinding, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-rehearsal-section',
  templateUrl: './rehearsal-section.component.html',
  styleUrls: ['./rehearsal-section.component.scss']
})
export class RehearsalSectionComponent implements OnInit {
  @Input() active = false;
  @HostBinding('class') private readonly classes = 'mb-2';

  constructor() {}

  ngOnInit(): void {}
}
