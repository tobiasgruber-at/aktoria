import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../core/services/auth/auth-service';

@Component({
  selector: 'app-scripts',
  templateUrl: './scripts.component.html',
  styleUrls: ['./scripts.component.scss']
})
export class ScriptsComponent implements OnInit {
  constructor(public authService: AuthService) {
  }

  ngOnInit(): void {
  }
}
