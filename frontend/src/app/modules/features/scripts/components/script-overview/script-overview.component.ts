import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ScriptService } from '../../../../core/services/script/script.service';
import { DetailedScript } from '../../../../shared/dtos/script-dtos';

@Component({
  selector: 'app-script-overview',
  templateUrl: './script-overview.component.html',
  styleUrls: ['./script-overview.component.scss']
})
export class ScriptOverviewComponent implements OnInit {
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
            this.loading = false;
          },
          error: handleNotFound
        });
      }
    });
  }

  private reset(): void {}
}
