import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ScriptService } from '../../../../core/services/script/script.service';
import { DetailedScript } from '../../../../shared/dtos/script-dtos';
import { ToastService } from '../../../../core/services/toast/toast.service';
import { Theme } from '../../../../shared/enums/theme.enum';

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
    private scriptService: ScriptService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.reset();
      const id = +params.get('id');
      const handleNotFound = () => {
        this.toastService.show({
          message: 'Skript konnte nicht gefunden werden.',
          theme: Theme.danger
        });
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
