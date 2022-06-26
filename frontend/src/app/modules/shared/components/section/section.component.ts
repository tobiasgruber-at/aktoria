import {Component, Input, OnInit} from '@angular/core';
import {SimpleScript} from '../../dtos/script-dtos';

@Component({
  selector: 'app-section',
  templateUrl: './section.component.html',
  styleUrls: ['./section.component.scss']
})
export class SectionComponent implements OnInit {
  @Input() title: string;
  @Input() scriptTitle: string;
  @Input() startLine: number;
  @Input() endLine: number;

  script: SimpleScript = null;

  constructor() {
  }

  ngOnInit(): void {
  }
}
