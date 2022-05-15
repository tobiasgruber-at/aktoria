import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ScriptService } from '../../../../core/services/script/script.service';
import { DetailedScript } from '../../../../shared/dtos/script-dtos';

@Component({
  selector: 'app-script-read',
  templateUrl: './script-read.component.html',
  styleUrls: ['./script-read.component.scss']
})
export class ScriptReadComponent implements OnInit {
  loading = true;
  script: DetailedScript = null;
  error = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private scriptService: ScriptService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.reset();
      const id = +params.get('id');
      const handleNotFound = () => {
        this.error = 'Skript konnte nicht gefunden werden.';
        this.loading = false;
      };
      if (isNaN(id)) {
        handleNotFound();
      } else {
        this.scriptService.getOne(id).subscribe({
          next: (script) => {
            this.script = script;
            console.log(2, this.script);
            this.loading = false;
          },
          error: handleNotFound
        });
      }
    });
  }

  private reset(): void {}
}
